package freedom.hall.module.room.vo;

import freedom.hall.module.room.entity.Room;

public class RoomVO extends Room {

	private int online;

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	@Override
	public String toString() {
		return "RoomVO [online=" + online + "]";
	}
}
