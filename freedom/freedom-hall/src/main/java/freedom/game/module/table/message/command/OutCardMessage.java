package freedom.game.module.table.message.command;

import freedom.game.module.table.message.command.OutCardMessage.In;
import freedom.game.module.table.message.command.OutCardMessage.Out;
import freedom.socket.command.CommandMessage;

public class OutCardMessage extends CommandMessage<In,Out> {

	public static final class In{
		private long playerId;
		private int  cardId;
		public long getPlayerId() {
			return playerId;
		}
		public void setPlayerId(long playerId) {
			this.playerId = playerId;
		}
		public int getCardId() {
			return cardId;
		}
		public void setCardId(int cardId) {
			this.cardId = cardId;
		}
		
	}
	
	public static final class Out{
		private int success;
		
		public Out(int success) {
			this.success = success;
		}

		public int getSuccess() {
			return success;
		}

		public void setSuccess(int success) {
			this.success = success;
		}

		@Override
		public String toString() {
			return "Out [success=" + success + "]";
		}
	}
}
