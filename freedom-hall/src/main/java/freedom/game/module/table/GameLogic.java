package freedom.game.module.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import freedom.common.constants.Consts;
import freedom.game.GameManager;
import freedom.game.module.room.sender.TableMessageSender;
import freedom.game.module.table.entity.Card;
import freedom.game.module.table.entity.Player;
import freedom.game.module.table.entity.Table;
import freedom.game.module.table.state.State;

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
		for (Player p : table.getUsers()) 
		{
			if(p.getId() == playerId)
			{
				p.setReady(Consts.TRUE);
				return p;
			}
		}
		return null;
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
		for (Player player : table.getUsers()) 
		{
			if(player.getRobot() == Consts.FALSE)
				return true;
		}
		return false;
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
		for (Player p : table.getUsers()) 
		{
			if(p.hasOpt())
			{
				p.getOpts().clear();
			}
		}
	}
	public long getLastInTime() {
		return lastInTime;
	}
	public void setLastInTime(long lastInTime) {
		this.lastInTime = lastInTime;
	}
}
