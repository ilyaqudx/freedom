package freedom.game.module.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import freedom.common.constants.Consts;
import freedom.common.kit.LogKit;
import freedom.game.GameManager;
import freedom.game.module.room.sender.TableMessageSender;
import freedom.game.module.table.entity.Card;
import freedom.game.module.table.entity.Operator;
import freedom.game.module.table.entity.Player;
import freedom.game.module.table.entity.Table;
import freedom.game.module.table.state.State;
import freedom.socket.command.LogicException;

public class GameLogic implements Logic {

	
	private Table table;
	private int timeout;
	private volatile long startTime;
	private volatile long lastInTime;
	public GameLogic(Table table)
	{
		this.table = table;
	}
	@Override
	public Table getTable()
	{
		return table;
	}
	
	public void initCard()
	{
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				for (int j2 = 0; j2 < 9; j2++) {
					table.getTableCard().add(new Card(i*36 + j*9 + j2 + 1,i+1,j2+1));
				}
			}
		}
	}
	
	protected void shuffleCard()
	{
		Collections.shuffle(table.getTableCard());
		Collections.shuffle(table.getTableCard());
		Collections.shuffle(table.getTableCard());
	}
	
	public void start()
	{
		//-洗牌
		shuffleCard();
		//-发牌
		firstPutCard();
		//-通知客户端显示发牌动画
		GameManager.context.getBean(TableMessageSender.class).sendStartGameFirstPutCard(table);
	}
	
	private void firstPutCard()
	{
		int[][] first = new int[][]{
				{1,1},{1,1},{1,1},{1,3},
				{1,3},{1,3},{1,4},{1,4},
				{1,5},{1,2},{1,2},{1,2},
				{1,6}
		};
		int[][] second = new int[][]{
				{1,8},{1,2},{1,5},{1,6},
				{1,3},{1,4},{1,4},{1,8},
				{1,5},{1,5},{1,6},{1,7},
				{1,7}
		};
		int[][] up = new int[][]{
				{2,1},{2,1},{2,2},{2,2},
				{2,3},{2,3},{2,4},{2,4},
				{2,5},{2,5},{2,6},{2,6},
				{2,6}
		};
		int[][] left = new int[][]{
				{3,1},{3,1},{3,2},{3,2},
				{3,3},{3,3},{3,4},{3,4},
				{3,5},{3,5},{3,6},{3,6},
				{3,6}
		};
		List<int[][]> random = new ArrayList<int[][]>();
		random.add(first);
		random.add(second);
		random.add(up);
		random.add(left);
		for (int i = 0; i < 4; i++) 
		{
			List<Card> handCard = table.getUsers().get(i).getHandCard();
			int[][] temp = random.get(i);
			for (int j = 0; j < temp.length; j++) 
			{
				for (Card card : table.getTableCard()) {
					if(card.getColor() == temp[j][0] && card.getValue() == temp[j][1])
					{
						handCard.add(card);
						table.getTableCard().remove(card);
						break;
					}
				}
			}
		}
		/*int[] putRule = new int[]{4,4,4,1};
		for (int i = 0; i < 4; i++)
		{
			for (Player p : users)
			{
				for (int j = 0; j < putRule[i]; j++) 
				{
					p.getHandCard().add(tableCard.remove(0));
				}
			}
		}*/
	}
	
	public void sitdown(Player player)
	{
		player.setRoomId(table.getRoom().getId());
		player.setTableId(table.getId());
		player.setSeat(table.getEmptySeats().remove(0));
		table.getSitedSeats().add(player.getSeat());
		table.getUsers().add(player);
		lastInTime = System.currentTimeMillis();
	}
	
	public Player ready(long playerId)
	{
		return table.getUsers().stream().filter(p -> p.getId() == playerId).peek(p -> p.setReady(Consts.TRUE)).findFirst().get();
	}
	
	public void nextPlayerPutCard()
	{
		Player player = computeNextPlayer(table.currentPlayer);
		table.currentPlayer = player;
		setState(table.PUT_CARD);
	}

	public Player computeNextPlayer(Player operatorPlayer) {
		int seat = operatorPlayer.getSeat();
		Player player = null;
		while(null == player || player.isHu())
		{
			if(seat == table.getUsers().size() - 1)
				player = table.getUsers().get(0);
			else
				player = table.getUsers().get(seat + 1);
			seat = player.getSeat();
		}
		return player;
	}
	
	/**
	 * 指定下一玩家和状态
	 * */
	public void nextPlayer(Player nextPlayer,State state)
	{
		table.currentPlayer = nextPlayer;
		setState(state);
	}
	
	
	
	public boolean hasRealPlayer()
	{
		return table.getUsers().stream().anyMatch(p -> p.getRobot() == Consts.FALSE);
	}
	
	public void timeout(int timeout)
	{
		this.timeout = timeout;
		this.startTime = System.currentTimeMillis();
	}
	
	public void timeoutInPlaying(State state,int timeout)
	{
		//此处先设置状态,后设置超时时间,可以解决本家手动打牌后,之后的玩家(目前是机器人)马上因为超时而直接出牌,
		//打牌后状态设置为摸牌,超时时间为0(应该是0引起的,考虑是否移除摸牌状态)
		//-bug?同时另一个BUG,和牌现在不能提示.不知是不是将状态先设置的原因,之前不存在此情况
		table.state = state;
		timeout(timeout);
	}
	
	public boolean isTimeout()
	{
		return System.currentTimeMillis() - startTime >= timeout;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) 
	{
		this.timeout = timeout;
		this.startTime = System.currentTimeMillis();
	}
	public void setTable(Table table) {
		this.table = table;
	}
	@Override
	public void setState(State state)
	{
		table.state = state;
		table.state.setTimeout();
	}
	
	public void setDelayState(int timeout,State nextState)
	{
		table.state = table.DELAY;
		table.state.setTimeout(timeout);
		table.nextState = nextState;
	}
	public void resetOpts()
	{
		table.getUsers().stream().filter(p -> p.hasOpt()).forEach(p -> p.getOpts().clear());
		table.responseList.clear();
	}
	public long getLastInTime() {
		return lastInTime;
	}
	public void setLastInTime(long lastInTime) {
		this.lastInTime = lastInTime;
	}
	
	public void peng(Player player,Operator operator)
	{
		//没有从打牌者OUT中删除被碰的牌
		Card outCard = operator.getTarget();
		List<Card> pengGroup = player.getTargetHandCardList(outCard);
		player.removeTargetHandCard(pengGroup);
		pengGroup.add(outCard);
		player.getPengCardList().add(pengGroup);
		//发送碰牌通知
		GameManager.context.getBean(TableMessageSender.class).sendPengNotify(table, player);
		table.getLogic().nextPlayer(player, table.OUT_CARD);
	}

	@Override
	public void gang(Player player,Operator operator)throws LogicException
	{
		List<Card> gangGroup = null;
		if(operator.isSelf())
		{
			//获取杠牌类型
			int  mark = operator.getMark();
			if(mark == Operator.MARK_AN_GANG)
				gangGroup = anGang(player, operator);
			else if(mark == Operator.MARK_PENG_GANG)
				gangGroup = pengGang(player, operator);
		}
		else
			gangGroup = zhiGang(player,operator);

		//杠牌成功
		//发送杠牌通知
		GameManager.context.getBean(TableMessageSender.class).sendGangNotify(table, player,operator,gangGroup);
		player.setGangFlag(true);
		table.getLogic().nextPlayer(player, table.PUT_CARD);
	}
	
	
	/**直杠(手上有3张,其他玩家打一张杠)
	 * @param player
	 * @param operator
	 * @return
	 */
	private List<Card> zhiGang(Player player,Operator operator) throws LogicException
	{
		//其他玩家打牌,手上必然有3张
		LogKit.info("开始处理直杠逻辑",this.getClass());
		Card outCard = operator.getTarget();
		List<Card> gangGroup = player.getTargetHandCardList(outCard);
		if(gangGroup.size() == 3)
		{
			LogKit.info("成功找到要杠的牌,从手牌中删除这3张牌",this.getClass());
			player.removeTargetHandCard(gangGroup);
			gangGroup.add(outCard);
			LogKit.info(String.format("【%s】直杠%s",player.getName(),outCard.colorString()),this.getClass());
		}
		return gangGroup;
	}
	
	/**
	 * 已碰的牌,再摸一张
	 * */
	private List<Card> pengGang(Player player,Operator operator)throws LogicException
	{
		//检查碰杠
		/*Card gangCard = operator.getTarget();
		for (List<Card> gangGroup : player.getPengCardList()) 
		{
			if(gangGroup.size() == 3 && CardUtil.sameCard(gangGroup.get(0), gangCard))
			{
				LogKit.info(String.format("【%s】巴杠%s",player.getName(),gangCard.colorString()),this.getClass());
				gangGroup.add(gangCard);
				//处理巴杠
				player.getHandCard().remove(gangCard);
				
				return gangGroup;
			}
		}*/
		List<Card> gangGroup = player.getTargetPengOrGangCard(operator.getTarget());
		Card handCard = player.getTargetHandCard(operator.getTarget());
		player.removeTargetHandCard(Lists.newArrayList(handCard));
		gangGroup.add(handCard);
		LogKit.info(String.format("【%s】巴杠%s",player.getName(),handCard.colorString()),this.getClass());
		return gangGroup;
	}
	
	/**
	 * 手上有4张牌
	 * */
	private List<Card> anGang(Player player,Operator operator)throws LogicException
	{
		//处理暗杠
		Card gangCard = operator.getTarget();
		//找到4张杠牌
		List<Card> gangGroup = player.getTargetHandCardList(gangCard);
		//从手牌中删除杠牌
		player.removeTargetHandCard(gangGroup);
		//加入杠牌组
		player.getGangCardList().add(gangGroup);
		
		LogKit.info(String.format("【%s】暗杠%s",player.getName(),gangCard.colorString()),this.getClass());
		return gangGroup;
	}
	
	/**
	 * 胡逻辑
	 * */
	public void hu(Player player,Operator operator)throws LogicException
	{
		boolean isSelf 			= operator.isSelf();
		Card 	targetCard 		= operator.getTarget();//isSelf ? currentPlayer.getPutCard().getCard() : currentPlayer.getOutCard().getCard();
		Player  currentPlayer	= table.getCurrentPlayer();
		boolean isGangFlag		= currentPlayer.isGangFlag();
		//再次检查是否能胡
		boolean hu = CardUtil.canHu(player.getHandCard(),isSelf ? null : targetCard);
		if(hu)
		{
			int huType = isSelf? (isGangFlag ? Player.HU_TYPE_GANG_SHANG_HUA
					: Player.HU_TYPE_ZI_MO) : (isGangFlag ? Player.HU_TYPE_GANG_SHANG_PAO
							: Player.HU_TYPE_DIAN_PAO);
			player.setHu(true,currentPlayer,targetCard,huType);
			//通知其他玩家有人胡牌
			GameManager.context.getBean(TableMessageSender.class).sendHuNotify(table,player);
			//下一个玩家
			Player nextPlayer = table.getLogic().computeNextPlayer(player);
			table.getLogic().nextPlayer(nextPlayer, table.PUT_CARD);
			LogKit.info(String.format("【%s】%s%s", player.getName(),isSelf ? "自摸" : "胡牌",targetCard.colorString()), this.getClass());
			for (Card card : player.getHandCard()) 
			{
				System.err.println(card.colorString());
			}
		}
	}
	
	/**
	 * 过逻辑
	 * */
	public void guo(boolean isSelf) throws LogicException
	{
		if(isSelf)
			setState(table.OUT_CARD);
		else
			nextPlayerPutCard();
	}
	
	
	public static void main(String[] args) {
		Card outCard = new Card(1, 1, 4);
		List<Card> handCard = Lists.newArrayList(new Card(1, 1, 3),new Card(1, 1, 4),new Card(1, 1, 4),new Card(1, 1, 7),new Card(1, 1, 7));
		List<Card> pengCard = handCard.stream().filter(c -> CardUtil.sameCard(c, outCard)).collect(Collectors.toList());
		pengCard.stream().forEach(c -> handCard.remove(c));
		
		for (Card card : handCard) {
			System.out.println(card.colorString());
		}
		
		for (Card card : pengCard) {
			System.err.println(card);
		}
		
		
		
	}
	
}
