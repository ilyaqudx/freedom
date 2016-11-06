package freedom.game.module.table.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import freedom.game.module.room.manager.RoomManager;
import freedom.game.module.room.sender.TableMessageSender;
import freedom.game.module.table.entity.Card;
import freedom.game.module.table.entity.Player;
import freedom.game.module.table.entity.Table;
import freedom.game.module.table.message.command.OutCardMessage;
import freedom.game.module.table.state.OutCardState;
import freedom.hall.Cmd;
import freedom.socket.command.AbstractCommand;
import freedom.socket.command.LogicException;
@Service(Cmd.Req.OUT_CARD)
public class OutCardCommand extends AbstractCommand<OutCardMessage> {

	@Autowired
	private RoomManager roomManager;
	@Autowired
	private TableMessageSender sender;
	
	@Override
	public OutCardMessage execute(OutCardMessage msg) throws Exception 
	{
		long playerId = msg.getIn().getPlayerId();
		int  cardId   = msg.getIn().getCardId();
		Table table = roomManager.getTable(playerId);
		if(table == null)
			msg.setEx(new LogicException(-1, "玩家不在房间中"));
		else if(!(table.getState() instanceof OutCardState))
			msg.setEx(new LogicException(-1, "当前状态不能出牌 : " + table.getState().V()));
		else if(table.getCurrentPlayer().getId() != playerId)
			msg.setEx(new LogicException(-1, "没有轮到该玩家出牌"));
		else{
			Player player = table.getPlayer(playerId);
			Card card = player.getCard(cardId);
			if(card != null)
			{
				player.outCard(card,player.isGangFlag());
				sender.sendPlayerOutCard(table);
				table.getLogic().nextPlayerPutCard();
				System.out.println(String.format("玩家【%s】出牌: %s", player.getName(),card.colorString()));
			}else
				msg.setEx(new LogicException(-1, "玩家没有这张牌"));
		}
		return msg;
	}

}
