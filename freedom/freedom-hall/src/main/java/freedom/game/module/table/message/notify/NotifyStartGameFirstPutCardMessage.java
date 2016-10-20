package freedom.game.module.table.message.notify;

import java.util.List;

import freedom.game.module.table.entity.Card;

public class NotifyStartGameFirstPutCardMessage extends NotifyMessage {

	private int firstSeat;
	private List<Card> handCard;
	public int getFirstSeat() {
		return firstSeat;
	}
	public void setFirstSeat(int firstSeat) {
		this.firstSeat = firstSeat;
	}
	public List<Card> getHandCard() {
		return handCard;
	}
	public void setHandCard(List<Card> handCard) {
		this.handCard = handCard;
	}
	
	
}
