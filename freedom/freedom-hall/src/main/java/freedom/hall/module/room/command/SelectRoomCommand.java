package freedom.hall.module.room.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import freedom.common.dao.DBFactory;
import freedom.hall.Cmd;
import freedom.hall.module.room.dao.RoomDAO;
import freedom.hall.module.room.entity.Room;
import freedom.hall.module.room.message.SelectRoomMessage;
import freedom.hall.module.room.message.SelectRoomMessage.Out;
import freedom.hall.module.room.vo.RoomVO;
import freedom.socket.command.AbstractCommand;
import freedom.socket.command.LogicException;

@Service(Cmd.Req.SELECT_ROOM)
public class SelectRoomCommand extends AbstractCommand<SelectRoomMessage>{

	private RoomDAO roomDAO = DBFactory.getDAO(RoomDAO.class);
	
	@Override
	public SelectRoomMessage execute(SelectRoomMessage msg) throws Exception 
	{
		int roomType = msg.getIn().getRoomType();
		if(roomType == Room.TYPE_XLCH)
		{
			List<Room> roomList = roomDAO.listByType(roomType);
			List<RoomVO> voList = new ArrayList<RoomVO>(roomList.size());
			for (Room room : roomList) 
			{
				RoomVO vo = new RoomVO();
				BeanUtils.copyProperties(room, vo);
				vo.setOnline(new Random().nextInt(100000));
				voList.add(vo);
			}
			Out out = new Out(voList);
			msg.setOut(out).setResCmd(Cmd.Res.SELECT_ROOM);
		}
		else
			msg.setEx(new LogicException(-1, "不存在的房间类型"));
		return msg;
	}

}
