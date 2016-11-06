package freedom.game.module.room.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import freedom.common.dao.DBFactory;
import freedom.common.moudle.user.User;
import freedom.common.moudle.user.UserDAO;
import freedom.game.module.room.manager.RoomManager;
import freedom.game.module.room.message.EntryRoomMessage;
import freedom.game.module.room.message.EntryRoomMessage.Out;
import freedom.game.module.room.sender.TableMessageSender;
import freedom.game.module.table.entity.Player;
import freedom.game.module.table.entity.Table;
import freedom.game.module.table.entity.TableScene;
import freedom.hall.Cmd.Req;
import freedom.hall.Cmd.Res;
import freedom.socket.command.AbstractCommand;
import freedom.socket.command.LogicException;

@Component(Req.ENTRY_ROOM)
public class EntryRoomCommand extends AbstractCommand<EntryRoomMessage> {

	@Autowired
	private RoomManager roomManager;
	UserDAO userDAO = DBFactory.getDAO(UserDAO.class);
	@Autowired
	private TableMessageSender sender;
	@Override
	public EntryRoomMessage execute(EntryRoomMessage msg) throws Exception 
	{
		long userId  = msg.getIn().getUserId();
		int  roomType= msg.getIn().getRoomType();
		boolean isPlaying = roomManager.inTable(userId);
		if(isPlaying)
		{
			msg.setEx(new LogicException(-1, "您已经在游戏中,不能再进入其他房间"));
		}
		else
		{
			User user = userDAO.get(userId);
			if(user != null)
			{
				//选择空余桌子
				try {
					final Player player = new Player(user);
					final Table table = roomManager.entryRoom(player,roomType);
					if(null != table)
					{
						TableScene scene = table.getScene();
						Out out = new Out(scene);
						msg.setOut(out).setResCmd(Res.ENTRY_ROOM);
						//牌桌通知消息
						sender.sendEntryRoomMessage(player, table);
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									Thread.sleep(1000);
									sender.sendReadyMessage(player, table);
								} catch (InterruptedException e) 
								{
									e.printStackTrace();
								}
								
							}
						}).start();
					}
					else
						msg.setEx(new LogicException(-2,"未找到空闲牌桌"));
				} catch (LogicException e) 
				{
					msg.setEx(e);
				}
			}else
				msg.setEx(new LogicException(-1, "用户不存在"));
		}
		return msg;
	}

}
