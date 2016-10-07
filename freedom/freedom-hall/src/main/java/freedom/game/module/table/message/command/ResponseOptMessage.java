package freedom.game.module.table.message.command;

import freedom.game.module.table.message.command.ResponseOptMessage.In;
import freedom.game.module.table.message.command.ResponseOptMessage.Out;
import freedom.socket.command.CommandMessage;

public class ResponseOptMessage extends CommandMessage<In,Out> {

	public static final class In{
		private int  opt;
		private int  cardId;
		private long playerId;
		public int getCardId() {
			return cardId;
		}
		public void setCardId(int cardId) {
			this.cardId = cardId;
		}
		public int getOpt() {
			return opt;
		}
		public void setOpt(int opt) {
			this.opt = opt;
		}
		public long getPlayerId() {
			return playerId;
		}
		public void setPlayerId(long playerId) {
			this.playerId = playerId;
		}
		@Override
		public String toString() {
			return "In [opt=" + opt + ", playerId=" + playerId + "]";
		}
	}
	
	public static final class Out{
		
	}
}
