package freedom.hall.module.room.message;

import java.util.ArrayList;
import java.util.List;

import freedom.hall.module.room.message.SelectRoomMessage.In;
import freedom.hall.module.room.message.SelectRoomMessage.Out;
import freedom.hall.module.room.vo.RoomVO;
import freedom.socket.command.CommandMessage;

public class SelectRoomMessage extends CommandMessage<In,Out> {

	public static class In{
		private int roomType;

		public int getRoomType() {
			return roomType;
		}

		public void setRoomType(int roomType) {
			this.roomType = roomType;
		}
		
	}
	
	public static class Out{
		private List<RoomVO> roomList = new ArrayList<RoomVO>();
		
		public Out(List<RoomVO> roomList) {
			this.roomList = roomList;
		}

		public List<RoomVO> getRoomList() {
			return roomList;
		}

		public void setRoomList(List<RoomVO> roomList) {
			this.roomList = roomList;
		}

		@Override
		public String toString() {
			return "Out [roomList=" + roomList + "]";
		}
		
	}
}
