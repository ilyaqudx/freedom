package freedom.game.module.table.message.command;

import freedom.game.module.table.message.command.SelectLackCardMessage.In;
import freedom.game.module.table.message.command.SelectLackCardMessage.Out;
import freedom.socket.command.CommandMessage;

public class SelectLackCardMessage extends CommandMessage<In,Out> {

	public static final class In{
		private long userId;
		private int  color;
		public long getUserId() {
			return userId;
		}
		public void setUserId(long userId) {
			this.userId = userId;
		}
		public int getColor() {
			return color;
		}
		public void setColor(int color) {
			this.color = color;
		}
	}
	
	public static final class Out
	{
		
	}
}
