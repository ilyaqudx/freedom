package freedom.game;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import freedom.common.dao.DBFactory;
import freedom.game.module.room.manager.RoomManager;
import freedom.hall.module.room.dao.RoomDAO;
import freedom.hall.module.room.entity.Room;
@Component
public class GameManager implements ApplicationContextAware{

	public static ApplicationContext context;
	
	private RoomDAO roomDAO = DBFactory.getDAO(RoomDAO.class);
	
	@Autowired
	private RoomManager roomManager;
	
	@Override
	public void setApplicationContext(ApplicationContext application)
			throws BeansException 
	{
		context = application;
		List<Room> roomList = roomDAO.list();
		for (Room room : roomList) 
		{
			room.init();
		}
		roomManager.addRoom(roomList);
		//初始化游戏主循环
		roomManager.update();
	}

}
