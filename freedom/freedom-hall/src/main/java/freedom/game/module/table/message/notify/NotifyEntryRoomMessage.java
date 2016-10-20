package freedom.game.module.table.message.notify;

import freedom.game.module.table.entity.Player;

public class NotifyEntryRoomMessage extends NotifyMessage{

	private Player player;

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public String toString() {
		return "NotifyEntryRoomMessage []";
	}
	
}
