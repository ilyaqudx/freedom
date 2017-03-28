package freedom.game.module.table.entity;

import java.util.List;

public interface GameTable {

	public void start();
	
	public void run();
	
	public void waiting();
	
	public void playing();
	
	public void finish();
	
	/**
	 * 获取牌桌上剩余的牌
	 * */
	public List<Card> getTableCard();
	/**
	 * 获取玩家数量
	 * */
	public int getPlayerCount();
	/**
	 * 获取牌桌主状态
	 * */
	public int getMajorState();
	/**
	 * 获取牌桌子状态
	 * */
	public int getSubState();
	/**
	 * 是否超时
	 * */
	public boolean isTimeout();
	/**
	 * 获取桌子所有玩家
	 * */
	public List<Player> getPlayers();
	/**
	 * 获取当前玩家
	 * */
	public Player getCurrentPlayer();
	/**
	 * 获取下一个操作玩家
	 * */
	public Player getNextPlayer();
	/**
	 * 设置牌桌状态
	 * */
	public void   setState(int majorState,int subState);
	/**
	 * 记录操作
	 * */
	public void addOperator(Operator operator);
	/**
	 * 记录出牌
	 * */
	public void addOutCard(Card card);
	/**
	 * 记录出牌
	 * */
	public void addOutCard(List<Card> cards);
}
