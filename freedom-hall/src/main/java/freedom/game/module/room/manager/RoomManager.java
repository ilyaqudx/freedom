package freedom.game.module.room.manager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import freedom.game.module.table.entity.Player;
import freedom.game.module.table.entity.Table;
import freedom.hall.module.room.entity.Room;
import freedom.socket.command.LogicException;

@Component
public class RoomManager {

	private Map<Long,Table> userInTable = new ConcurrentHashMap<Long,Table>();
	//房间类型
	private Map<Integer,Room> roomMap = new ConcurrentHashMap<Integer,Room>();
	
	public boolean inTable(long userId)
	{
		return userInTable.containsKey(userId);
	}
	
	public Table getTable(long userId) throws LogicException
	{
		if(inTable(userId)){
			return userInTable.get(userId);
		}else
			throw new LogicException(-1, "用户不在房间中");
	}
	
	public void addRoom(List<Room> rooms)
	{
		for (Room room : rooms) 
		{
			roomMap.put(room.getType(), room);
		}
	}

	public Table entryRoom(Player player,int roomType) throws LogicException
	{
		Room room = roomMap.get(roomType);
		if(room == null)
			throw new LogicException(-1, "不存在该类房间");
		List<Table> tables = room.getTables();
		for (Table table : tables) 
		{
			if(table.getSeat())
			{
				table.getLogic().sitdown(player);
				userInTable.put(player.getId(), table);
				return table;
			}
		}
		return null;
	}
	
	public void update()
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				while(true)
				{
					for (Room room : roomMap.values()) 
					{
						List<Table> tables = room.getTables();
						for (Table table : tables) 
						{
							table.update();
						}
					}
				}
			}
		}).start();
	}
}
