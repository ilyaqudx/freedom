package freedom.game.module.table.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import freedom.common.constants.Consts;
import freedom.game.GameManager;
import freedom.game.module.room.sender.TableMessageSender;
import freedom.game.module.table.entity.Table.PlayingState;
import freedom.game.module.table.entity.Table.State;

public class Logic {

	private Table table;
	private volatile long lastInTime;
	public Logic(Table table)
	{
		this.table = table;
	}
	
	protected void initCard()
	{
		for (int i = 0; i < 3; i++) 
		{
			for (int j = 0; j < 4; j++) 
			{
				for (int j2 = 0; j2 < 9; j2++) 
				{
					table.tableCard.add(new Card(i*36 + j*9 + j2 + 1,i+1,j2+1));
				}
			}
		}
	}
	
	public void start()
	{
		//-洗牌
		shuffleCard();
		//-发牌
		firstPutCard();
		//-通知客户端显示发牌动画
		GameManager.context.getBean(TableMessageSender.class).sendStartGameFirstPutCard(this.table);
	}
	
	protected void shuffleCard()
	{
		Collections.shuffle(table.tableCard);
		Collections.shuffle(table.tableCard);
		Collections.shuffle(table.tableCard);
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
			List<Card> handCard = table.users.get(i).getHandCard();
			int[][] temp = random.get(i);
			for (int j = 0; j < temp.length; j++) 
			{
				for (Card card : table.tableCard) {
					if(card.getColor() == temp[j][0] && card.getValue() == temp[j][1])
					{
						handCard.add(card);
						table.tableCard.remove(card);
						break;
					}
				}
			}
		}
	}
	
	
	public void sitdown(Player player)
	{
		player.setRoomId(table.getRoom().getId());
		player.setTableId(table.getId());
		player.setSeat(table.emptySeats.remove(0));
		table.sitedSeats.add(player.getSeat());
		table.users.add(player);
		lastInTime = System.currentTimeMillis();
	}
	
	public Player ready(long playerId)
	{
		for (Player p : table.users) 
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
		timeoutInPlaying(PlayingState.PUT_CARD, 0);
	}

	public Player computeNextPlayer(Player operatorPlayer)
	{
		int seat = operatorPlayer.getSeat();
		Player player = null;
		while(null == player || player.isHu())
		{
			if(seat == table.users.size() - 1)
				player = table.users.get(0);
			else
				player = table.users.get(seat + 1);
			seat = player.getSeat();
		}
		return player;
	}
	
	
	public void timeout(int timeout)
	{
		table.timeout = timeout;
		table.startTime = System.currentTimeMillis();
	}
	
	public void timeoutInPlaying(PlayingState playingState,int timeout)
	{
		//此处先设置状态,后设置超时时间,可以解决本家手动打牌后,之后的玩家(目前是机器人)马上因为超时而直接出牌,
		//打牌后状态设置为摸牌,超时时间为0(应该是0引起的,考虑是否移除摸牌状态)
		//-bug?同时另一个BUG,和牌现在不能提示.不知是不是将状态先设置的原因,之前不存在此情况
		table.playingState = playingState;
		timeout(timeout);
	}
	
	void delay(int timeout,State nextState)
	{
		table.state = State.DELAY;
		timeout(timeout);
		table.nextState = nextState;
	}
	
	boolean isTimeout()
	{
		return System.currentTimeMillis() - table.startTime >= table.timeout;
	}
	
	void resetOpts()
	{
		for (Player p : table.users) 
		{
			if(p.hasOpt())
			{
				p.getOpts().clear();
			}
		}
	}
	
	private int playingStateTimeout(PlayingState state)
	{
		int timeout = 10;
		switch (state) {
		case PUT_CARD:
			timeout = 0;
			break;
		case WAIT_PLAYER_OUT:
			timeout = 10;
		case WAIT_RESPONSE:
			timeout = 10;
		}
		return timeout * 1000;
	}
	
	/**
	 * 指定下一玩家和状态
	 * */
	public void nextPlayer(Player nextPlayer,PlayingState playingState)
	{
		table.currentPlayer = nextPlayer;
		timeoutInPlaying(playingState,playingStateTimeout(playingState));
	}
	
	
	
	boolean hasRealPlayer()
	{
		for (Player player : table.users) 
		{
			if(player.getRobot() == Consts.FALSE)
				return true;
		}
		return false;
	}
}
