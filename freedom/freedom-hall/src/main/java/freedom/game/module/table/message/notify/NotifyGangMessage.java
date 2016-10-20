package freedom.game.module.table.message.notify;

import java.util.ArrayList;
import java.util.List;

import freedom.game.module.table.entity.Card;

/**
 * 杠牌消息
 * */
public class NotifyGangMessage extends NotifyMessage {

	public NotifyGangMessage(String cmd,long playerId,List<Card> gandCard,int mark) 
	{
		super(cmd);
		this.gangCard = gandCard;
		this.playerId = playerId;
		this.mark     = mark;
	}

	private long playerId;
	private List<Card> gangCard = new ArrayList<Card>(4);
	private int mark;
	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public List<Card> getGangCard() {
		return gangCard;
	}
	public void setGangCard(List<Card> gangCard) {
		this.gangCard = gangCard;
	}
	@Override
	public String toString() {
		return "NotifyGangMessage [playerId=" + playerId + ", gangCard=" + gangCard + ", mark=" + mark + "]";
	}
}
