package freedom.game.module.table.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import freedom.game.module.room.manager.RoomManager;
import freedom.game.module.table.entity.Player;
import freedom.game.module.table.entity.Table;
import freedom.game.module.table.message.command.SelectLackCardMessage;
import freedom.game.module.table.state.SelectLackCardState;
import freedom.hall.Cmd;
import freedom.socket.command.AbstractCommand;
import freedom.socket.command.LogicException;

@Service(Cmd.Req.SELECT_LACK_CARD)
public class SelectLackCardCommand extends AbstractCommand<SelectLackCardMessage> {

	@Autowired
	private RoomManager roomManager;
	@Override
	public SelectLackCardMessage execute(SelectLackCardMessage msg)
			throws Exception
	{
		long playerId = msg.getIn().getUserId();
		Table table = roomManager.getTable(playerId);
		if(null == table)
			msg.setEx(new LogicException(-1, "玩家不在牌桌中"));
		else if(!(table.getState() instanceof SelectLackCardState))
			msg.setEx(new LogicException(-1, "当前牌桌状态不能选择缺门"));
		else
		{
			Player player = table.getPlayer(playerId);
			if(!player.isSelectedLackColor())
			{
				int color = msg.getIn().getColor();
				if(color > 0 && color < 4)
					player.setSelectLackColor(color);
				else
					msg.setEx(new LogicException(-1, "选择的缺门不存在 : " + color));
			}
			else
				msg.setEx(new LogicException(-1, "不能重复选择缺门"));
		}
		return msg;
	}
}
