package freedom.game.module.table.message.notify;

/**
 * 广播准备消息
 * */
public class NotifyReadyMessage extends NotifyMessage{

	//准备玩家ID
	private long playerId;
	private int  seat;
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getSeat() {
		return seat;
	}
	public void setSeat(int seat) {
		this.seat = seat;
	}
	@Override
	public String toString() {
		return "NotifyReadyMessage [playerId=" + playerId + ", seat=" + seat
				+ "]";
	}
}
