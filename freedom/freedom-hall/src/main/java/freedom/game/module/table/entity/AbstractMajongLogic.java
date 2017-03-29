package freedom.game.module.table.entity;

import freedom.game.module.room.sender.TableMessageSender;
import freedom.game.module.table.MajongService;

/**
 * 麻将游戏基础逻辑
 * */
public abstract class AbstractMajongLogic implements GameLogic {

	public  AbstractMajongLogic() 
	{
		
	}
	
	@Override
	public void handleLogic(GameTable gameTable)
	{
		int state = gameTable.getMajorState();
		switch (state) {
		case AbstractGameTable.SUB_PUT:
			putCardLogic(gameTable);
			break;
		case AbstractGameTable.SUB_OUT:
			outCardLogic(gameTable);
			break;
		case AbstractGameTable.SUB_RESPONSE:
			responseLogic(gameTable);
			break;
		}
	}
	
	public void putCardLogic(GameTable gameTable)
	{
		AbstractGameTable table = (AbstractGameTable) gameTable;
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
		//自动出牌
		if(table.isTimeout())
		{
			table.getCurrentPlayer().autoOutCard();
			System.out.println("超时由AI自动出牌 :【" +table.getCurrentPlayer().getOutCard()+ "】");
		}
		//玩家选择的牌
		else if(table.getCurrentPlayer().isOperator)
		{
			final Player p = table.getCurrentPlayer();
			p.outCard(p.getOutCard(),p.isGangFlag());
			TableMessageSender.I.sendPlayerOutCard(table);
			table.nextPlayerPutCard();
			System.out.println("玩家出牌成功:【" +p.getOutCard()+ "】");
		}
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
	
	
	/**
	 * 响应逻辑(吃/碰/杠/胡/过)
	 * */
	public abstract void responseLogic(GameTable table);
	
	/**
	 * 胡牌逻辑
	 * */
	public abstract void huLogic(GameTable table);
	/**
	 * 杠牌逻辑
	 * */
	public abstract void gangLogic(GameTable table);
	/**
	 * 碰牌逻辑
	 * */
	public abstract void pengLogic(GameTable table);
	/**
	 * 结算逻辑
	 * */
	public abstract void settleLogic(GameTable table);
}
