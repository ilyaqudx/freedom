package freedom.game.module.table.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import freedom.game.module.room.manager.RoomManager;
import freedom.game.module.room.message.ReadyMessage;
import freedom.game.module.room.sender.TableMessageSender;
import freedom.game.module.table.entity.Player;
import freedom.game.module.table.entity.Table;
import freedom.hall.Cmd;
import freedom.socket.command.AbstractCommand;
import freedom.socket.command.LogicException;
@Service(Cmd.Req.READY)
public class ReadyCommand extends AbstractCommand<ReadyMessage> {

	@Autowired
	private RoomManager roomManager;
	@Autowired
	private TableMessageSender sender;
	
	@Override
	public ReadyMessage execute(ReadyMessage msg) throws Exception
	{
		long playerId = msg.getIn().getUserId();
		Table table = roomManager.getTable(playerId);
		if(table.getState() == Table.State.INIT)
		{
			Player player = table.ready(playerId);
			msg.setResCmd(Cmd.Res.READY);
			sender.sendReadyMessage(player, table);
		}else
			msg.setEx(new LogicException(-1, "牌桌状态不能设置准备"));
		
		return msg;
	}

}
