package freedom.hall.module.room.dao;

import java.util.List;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.SQL;

import freedom.hall.module.room.entity.Room;


@DB
public interface RoomDAO {

	@SQL("select * from Room")
	public List<Room> list();
	@SQL("select * from Room where type = :1")
	public List<Room> listByType(int type);
}
