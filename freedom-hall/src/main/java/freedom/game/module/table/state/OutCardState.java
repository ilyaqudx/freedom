/**
 * 
 */
package freedom.game.module.table.state;

import java.util.List;

import freedom.game.GameManager;
import freedom.game.module.room.sender.TableMessageSender;
import freedom.game.module.table.CardUtil;
import freedom.game.module.table.entity.Card;
import freedom.game.module.table.entity.Player;
import freedom.game.module.table.entity.Table;

/**
 * @author Administrator
 *
 */
public class OutCardState extends GameState {

	public OutCardState(Table table) {
		super(table,5);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action()
	{
		boolean isTimeout = logic.isTimeout();
		if(isTimeout)
		{
			//如果超时还没有打牌,则摸什么打什么
			Card outCard = table.currentPlayer.outCard();
			GameManager.context.getBean(TableMessageSender.class)
			.sendPlayerOutCard(table);
			System.out.println(String.format("玩家【%s】出牌超时,自动出牌 : %s", table.getCurrentPlayer().getName(),outCard.colorString()));
			
			//打牌后判断是否有玩家可以有响应(胡/杠/碰)
			List<Player> responsePlayers = CardUtil.hasResponseAfterOut(table);
			if(responsePlayers.isEmpty())
				logic.nextPlayerPutCard();
			else
			{
				//发送等待响应消息
				GameManager.context.getBean(TableMessageSender.class)
				.sendWaitResponseAfterOutCard(table);
				logic.setState(table.RESPONSE);
			}
				
		}
	

	}

}
