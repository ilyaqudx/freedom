package freedom.game.module.table.message.notify;

import java.util.ArrayList;
import java.util.List;

import freedom.game.module.table.entity.Card;

public class NotifyPengMessage extends NotifyMessage {

	public NotifyPengMessage(String cmd,long playerId,List<Card> pendCard) 
	{
		super(cmd);
		this.pengCard = pendCard;
		this.playerId = playerId;
	}

	private long playerId;
	private List<Card> pengCard = new ArrayList<Card>(2);
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public List<Card> getPengCard() {
		return pengCard;
	}
	public void setPengCard(List<Card> pengCard) {
		this.pengCard = pengCard;
	}
	@Override
	public String toString() {
		return "NotifyPengMessage [playerId=" + playerId + ", pengCard="
				+ pengCard + "]";
	}
	
	
}
