package freedom.game.module.table.state;

import freedom.game.module.table.entity.Table;
/**
 * 响应状态还需要再细分(摸牌响应和打牌响应还有巴杠响应)
 * */
public class ResponseState extends GameState {

	public ResponseState(Table table) {
		super(table,4);
	}

	@Override
	public void action() 
	{
		boolean timeout = logic.isTimeout();
		if(timeout)
		{
			//TODO 桌子应该记录操作玩家
			System.out.println("玩家【%s】超时,自动取消响应操作");
			//如果没有响应则代表取消操作,下一个玩家继续
			logic.resetOpts();
			logic.nextPlayerPutCard();
		}
	}

}
