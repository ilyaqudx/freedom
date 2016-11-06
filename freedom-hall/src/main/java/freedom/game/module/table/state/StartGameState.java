package freedom.game.module.table.state;

import freedom.game.GameManager;
import freedom.game.module.room.sender.TableMessageSender;
import freedom.game.module.table.entity.Table;

public class StartGameState extends GameState {

	public StartGameState(Table table)
	{
		super(table,1);
	}

	@Override
	public void action() 
	{
		GameManager.context.getBean(TableMessageSender.class).sendStartSelectLackCard(table);
		logic.setState(table.SELECT_LACK);
	}

}
