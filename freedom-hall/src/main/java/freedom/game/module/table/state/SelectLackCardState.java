package freedom.game.module.table.state;

import java.util.List;
import java.util.Random;

import freedom.game.GameManager;
import freedom.game.module.room.sender.TableMessageSender;
import freedom.game.module.table.entity.Player;
import freedom.game.module.table.entity.Table;

public class SelectLackCardState extends GameState {

	public SelectLackCardState(Table table)
	{
		super(table,2);
	}

	@Override
	public void action()
	{
		boolean isTimeout = logic.isTimeout();
		boolean allSelected = true;
		List<Player> users = table.getUsers();
		for (Player p : table.getUsers()) {
			if(!p.isSelectedLackColor())
			{
				if(isTimeout){
					//已超时,系统默认选择缺门
					p.setSelectLackColor(new Random().nextInt(3) + 1);
				}
				else
				{
					allSelected = false;
					break;
				}
			}
		}
		
		if(allSelected)
		{
			//选择缺门结束
			GameManager.context.getBean(TableMessageSender.class)
			.sendSelectLackCardResult(table);
			table.currentPlayer = users.get(0);
			logic.setDelayState(2000, table.PUT_CARD);
		}
	}
}
