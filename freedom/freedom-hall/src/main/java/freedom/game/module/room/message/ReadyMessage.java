package freedom.game.module.room.message;

import freedom.game.module.room.message.ReadyMessage.In;
import freedom.game.module.room.message.ReadyMessage.Out;
import freedom.socket.command.CommandMessage;

public class ReadyMessage extends CommandMessage<In,Out> {
	
	public static final class In{
		private long userId;

		public long getUserId() {
			return userId;
		}

		public void setUserId(long userId) {
			this.userId = userId;
		}
		
	}
	
	public static final class Out{
		private long userId;
		private int seat;
		public long getUserId() {
			return userId;
		}
		public void setUserId(long userId) {
			this.userId = userId;
		}
		public int getSeat() {
			return seat;
		}
		public void setSeat(int seat) {
			this.seat = seat;
		}
	}
}
