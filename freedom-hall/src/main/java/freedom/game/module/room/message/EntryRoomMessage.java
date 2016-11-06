package freedom.game.module.room.message;

import freedom.game.module.room.message.EntryRoomMessage.In;
import freedom.game.module.room.message.EntryRoomMessage.Out;
import freedom.game.module.table.entity.TableScene;
import freedom.socket.command.CommandMessage;

public class EntryRoomMessage extends CommandMessage<In,Out> {

	public static final class In
	{
		private long userId;
		private int  roomType;
		public long getUserId() {
			return userId;
		}
		public void setUserId(long userId) {
			this.userId = userId;
		}
		public int getRoomType() {
			return roomType;
		}
		public void setRoomType(int roomType) {
			this.roomType = roomType;
		}
		@Override
		public String toString() {
			return "In [userId=" + userId + ", roomType=" + roomType + "]";
		}
		
	}
	
	public static final class Out
	{
		private TableScene scene;

		public Out(TableScene scene) 
		{
			this.scene = scene;
		}

		public TableScene getScene()
		{
			return scene;
		}

		public void setScene(TableScene scene)
		{
			this.scene = scene;
		}
		
	}
}
