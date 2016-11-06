package freedom.game.module.table.state;

import freedom.game.module.table.Logic;
import freedom.game.module.table.entity.Table;

public abstract class GameState implements State {

	protected Table table;
	protected Logic logic;
	protected int   v;
	public GameState(Table table,int v)
	{
		this.table = table;
		this.logic = table.getLogic();
		this.v     = v;
	}

	@Override
	public int V()
	{
		return v;
	}

	@Override
	public void setTimeout()
	{
		setTimeout(3000);
	}

	@Override
	public void setTimeout(int timeout)
	{
		logic.setTimeout(timeout);
	}
	
	
	

}
