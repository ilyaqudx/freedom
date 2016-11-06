package freedom.game.module.table.state;

import freedom.game.GameManager;
import freedom.game.module.room.sender.TableMessageSender;
import freedom.game.module.table.CardUtil;
import freedom.game.module.table.entity.Card;
import freedom.game.module.table.entity.Table;

public class PutCardState extends GameState {

	public PutCardState(Table table)
	{
		super(table,3);
	}

	@Override
	public void action()
	{
		if(!table.tableCard.isEmpty())
		{
			Card card = table.tableCard.remove(0);
			table.currentPlayer.putCard(card);
			
			//摸牌
			GameManager.context.getBean(TableMessageSender.class)
			.sendPutCardInPlaying(table);
			System.out.println("玩家" +table.currentPlayer.getName()+ "摸牌成功");
			if(CardUtil.hasResponseAfterPutCard(table.currentPlayer))
			{
				GameManager.context.getBean(TableMessageSender.class).sendWaitResponseAfterPutCard(table);
				logic.setState(table.RESPONSE);
			}
			else
				logic.setState(table.OUT_CARD);
		}
		else
		{
			logic.setState(table.SETTLEMENT);
			System.out.println("牌摸完了,进入结算");
		}
	}

}
