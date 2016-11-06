package freedom.game.module.table.entity;

import java.util.List;

public class TableScene 
{
	private long roomId;
	private long tableId;
	private int timeout;
	private freedom.game.module.table.state.State state;
	private long currentPlayer;
	private List<Player> players;
	private List<Card> tableCard;
	
	public TableScene(Table table)
	{
		this.roomId = table.getRoom().getId();
		this.tableId= table.getId();
		this.state = table.getState();
		this.players = table.getUsers();
		this.tableCard = table.getTableCard();
		//this.currentPlayer = table.getCurrentPlayer().getId();
		this.timeout = table.timeout;
	}
	
	public long getRoomId() {
		return roomId;
	}
	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}
	public long getTableId() {
		return tableId;
	}
	public void setTableId(long tableId) {
		this.tableId = tableId;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public freedom.game.module.table.state.State getState() {
		return state;
	}
	public void setState(freedom.game.module.table.state.State state) {
		this.state = state;
	}
	public long getCurrentPlayer() {
		return currentPlayer;
	}
	public void setCurrentPlayer(long currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	public List<Card> getTableCard() {
		return tableCard;
	}
	public void setTableCard(List<Card> tableCard) {
		this.tableCard = tableCard;
	}
}
