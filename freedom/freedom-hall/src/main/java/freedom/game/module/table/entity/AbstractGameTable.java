package freedom.game.module.table.entity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractGameTable implements GameTable {

	/**
	 * 牌桌大的状态分为三个
	 * 1-等待
	 * 2-游戏中(此状态对应的子状态为子类去实现)
	 * 3-结束
	 * */
	public static final int MAIN_STATE_WAIT = 1,MAIN_STATE_PLAING = 2,MAIN_STATE_FINISH = 3;
	
	public static final int SUB_PUT  = 1,SUB_OUT = 2,SUB_RESPONSE = 3;
	
	//牌桌规则
	private GameRule gameRule;
	//牌桌逻辑
	private GameLogic gameLogic;
	//牌桌玩家
	private List<Player> players;
	//牌桌开始时间
	private long startTime;
	//牌桌结束时间
	private long finishTime;
	//牌桌的超时时间
	private int  timeout;
	//计算超时的开始时间
	private long timeoutStartTime;
	//牌桌主状态
	private int majorState;
	//牌桌子状态
	private int subState;
	//牌桌上的牌
	private LinkedList<Card> tableCard;
	//当前操作玩家
	private Player currentPlayer;
	public AbstractGameTable(GameRule gameRule,GameLogic gameLogic)
	{
		this.gameRule  = gameRule;
		this.gameLogic = gameLogic;
		this.players = new ArrayList<Player>(gameRule.getChairCount());
	}
	
	@Override
	public void start() 
	{
		this.startTime = System.currentTimeMillis();
	}
	
	@Override
	public void run()
	{
		if(majorState == MAIN_STATE_WAIT)
			waiting();
		else if(majorState == MAIN_STATE_PLAING){
			gameLogic.handleLogic(this);
		}
	}

	@Override
	public void waiting() 
	{
		
	}

	@Override
	public void playing() 
	{
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		this.finishTime = System.currentTimeMillis();
	}

	public Card getOneCard()
	{
		if(tableCard.isEmpty())
			return null;
		return tableCard.removeFirst();
	}
	
	@Override
	public int getPlayerCount() 
	{
		return players.size();
	}

	@Override
	public int getMajorState() 
	{
		return majorState;
	}
	@Override
	public int getSubState()
	{
		return subState;
	}

	@Override
	public boolean isTimeout() 
	{
		return System.currentTimeMillis() - timeoutStartTime >= timeout;
	}

	@Override
	public List<Player> getPlayers() 
	{
		return players;
	}

	@Override
	public Player getCurrentPlayer() 
	{
		return currentPlayer;
	}

	@Override
	public void addOperator(Operator operator) 
	{
		
	}

	@Override
	public void addOutCard(Card card)
	{
		
	}

	@Override
	public void addOutCard(List<Card> cards)
	{
		
	}

	@Override
	public List<Card> getTableCard() 
	{
		return tableCard;
	}

	@Override
	public Player getNextPlayer() 
	{
		//TODO 胡牌的玩家需要跳过
		int seat = currentPlayer.getSeat();
		final int chairCount = gameRule.getChairCount();
		seat = chairCount == seat + 1  ? 0 : ++seat;
		return players.get(seat);
	}

	@Override
	public void setState(int majorState,int subState) 
	{
		this.majorState = majorState;
		this.subState   = subState;
		this.timeoutStartTime = System.currentTimeMillis();
	}
}
