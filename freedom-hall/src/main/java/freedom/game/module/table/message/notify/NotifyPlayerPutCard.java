package freedom.game.module.table.message.notify;

import freedom.game.module.table.entity.Card;

public class NotifyPlayerPutCard extends NotifyMessage {

	private long playerId;
	private Card card;
	public NotifyPlayerPutCard(String cmd,long playerId, Card card) {
		super.setCmd(cmd);
		this.playerId = playerId;
		this.card = card;
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
	
	
}
