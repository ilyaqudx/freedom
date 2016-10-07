package freedom.game.module.table.message.notify;

import freedom.game.module.table.entity.Card;

public class NotifyHuMessage extends NotifyMessage {

	private long playerId;
	private int  huType;
	private Card huCard;
	
	public NotifyHuMessage(String cmd,long playerId,int huType,Card huCard) 
	{
		super(cmd);
		this.playerId = playerId;
		this.huType   = huType;
		this.huCard   = huCard;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getHuType() {
		return huType;
	}

	public void setHuType(int huType) {
		this.huType = huType;
	}

	public Card getHuCard() {
		return huCard;
	}

	public void setHuCard(Card huCard) {
		this.huCard = huCard;
	}

	@Override
	public String toString() {
		return "NotifyHuMessage [playerId=" + playerId + ", huType=" + huType
				+ ", huCard=" + huCard + "]";
	}
}
