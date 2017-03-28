package freedom.game.module.table.entity;

import freedom.game.module.table.MajongService;

/**
 * 麻将游戏逻辑
 * */
public abstract class MajongGameLogic implements GameLogic {

	
	public  MajongGameLogic() 
	{
		
	}
	
	@Override
	public void handleLogic(GameTable gameTable)
	{
		int state = gameTable.getMajorState();
	}
	
	public void putCardLogic(GameTable gameTable)
	{
		final AbstractGameTable table = (AbstractGameTable) gameTable;
		Card card = table.getOneCard();
		if (card == null) 
		{
			// 进入结算
			table.setState(table.getMajorState(), 0);
		} 
		else 
		{
			table.getCurrentPlayer().putCard(card);
			boolean hasResponse = MajongService.hasResponseAfterPutCard(table.getCurrentPlayer());
			if (hasResponse) {
				table.setState(table.getMajorState(), AbstractGameTable.SUB_RESPONSE);
			} else {
				table.setState(table.getMajorState(), AbstractGameTable.SUB_OUT);
			}
		}
	}
	
	public void outCardLogic(GameTable table)
	{
		
	}

	/**
	 * 选择定缺逻辑
	 * */
	public void selectDingQue(GameTable gameTable) 
	{
		
	}
	/**
	 * 换三张逻辑
	 * */
	public void huanSanZhangLogic(GameTable table)
	{
		
	}
	public abstract void resposneLogic(GameTable table);
	public abstract void huLogic(GameTable table);
	public abstract void gangLogic(GameTable table);
	public abstract void pengLogic(GameTable table);
	
	public abstract void settleLogic(GameTable table);
}
