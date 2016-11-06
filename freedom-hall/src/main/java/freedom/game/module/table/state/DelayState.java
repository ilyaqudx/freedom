package freedom.game.module.table.state;

import freedom.game.module.table.entity.Table;
/**
 * 再考虑一下,该状态是否需要设计成添加监听器,然后执行回调
 * 
 * 
 * 如果设计成超时后可执行回调,那么则可以取消瞬时状态(比如START GAME,PUT_CARD)
 * */
public class DelayState extends GameState {

	public DelayState(Table table)
	{
		super(table,-1);
	}

	@Override
	public void action()
	{
		if(logic.isTimeout())
		{
			logic.setState(table.nextState);
		}
	}
}
