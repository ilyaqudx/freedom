package freedom.game.module.table.entity;
/**
 * 摸到的牌的信息
 * */
public class InCardInfo {

	//玩家
	private long playerId;
	//牌
	private Card card;
	//开杠摸牌
	private boolean gang;
	public InCardInfo(long playerId, Card card, boolean gang)
	{
		this.playerId = playerId;
		this.card = card;
		this.gang = gang;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public Card getCard() {
		return card;
	}
	public void setCard(Card card) {
		this.card = card;
	}
	public boolean isGang() {
		return gang;
	}
	public void setGang(boolean gang) {
		this.gang = gang;
	}
}
