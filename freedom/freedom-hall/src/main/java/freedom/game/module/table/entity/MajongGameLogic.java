package freedom.game.module.table.entity;

import freedom.game.module.table.MajongService;

public abstract class MajongGameLogic implements GameLogic {

	
	public  MajongGameLogic() 
	{
		
	}
	
	@Override
	public void handleLogic(GameTable gameTable)
	{
		int state = gameTable.getMajorState();
	}

	/**
	 * 选择定缺逻辑
	 * */
	class SelectDingQueLogic implements GameLogic{

		@Override
		public void handleLogic(GameTable gameTable) 
		{
			
		}
	}
	/**
	 * 换三张逻辑
	 * */
	class HuanSanZhangLogic implements GameLogic{

		@Override
		public void handleLogic(GameTable gameTable) {
			
		}
	}
	class PutCardLogic implements GameLogic{

		@Override
		public void handleLogic(GameTable gameTable) 
		{
			final AbstractGameTable table = (AbstractGameTable) gameTable; 
			Card  card = table.getOneCard();
			if(card == null){
				//进入结算
				table.setState(table.getMajorState(), 0);
			}else{
				table.getCurrentPlayer().putCard(card);
				boolean hasResponse = MajongService.hasResponseAfterPutCard(table.getCurrentPlayer());
				if(hasResponse){
					table.setState(table.getMajorState(), AbstractGameTable.SUB_RESPONSE);
				}else{
					table.setState(table.getMajorState(), AbstractGameTable.SUB_OUT);
				}
			}
		}
	}
	class OutCardLogic implements GameLogic{

		@Override
		public void handleLogic(GameTable gameTable) {
			// TODO Auto-generated method stub
			
		}
	}
	class ResponseLogic implements GameLogic{

		@Override
		public void handleLogic(GameTable gameTable) {
			// TODO Auto-generated method stub
			
		}
	}
	class HuLogic implements GameLogic{

		@Override
		public void handleLogic(GameTable gameTable) {
			// TODO Auto-generated method stub
			
		}
	}
	
	class SettleLogic implements GameLogic{

		@Override
		public void handleLogic(GameTable gameTable) 
		{
			
		}
	}
}
