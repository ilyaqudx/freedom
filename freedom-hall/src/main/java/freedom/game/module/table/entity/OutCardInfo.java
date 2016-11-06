package freedom.game.module.table.entity;

public class OutCardInfo {

	private long playerId;
	private Card card;
	private boolean gang;
	public OutCardInfo(long playerId, Card card, boolean gang)
	{
		this.playerId = playerId;
		this.card = card;
		this.gang = gang;
	}
	public long getPlayerId() 
	{
		return playerId;
	}
	public void setPlayerId(long playerId) 
	{
		this.playerId = playerId;
	}
	public Card getCard()
	{
		return card;
	}
	public void setCard(Card card)
	{
		this.card = card;
	}
	public boolean isGang() 
	{
		return gang;
	}
	public void setGang(boolean gang) 
	{
		this.gang = gang;
	}
	
	
}
