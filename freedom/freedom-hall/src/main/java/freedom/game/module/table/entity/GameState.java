package freedom.game.module.table.entity;

public enum GameState {

	WAIT() {
		@Override
		void handleLogic(GameTable gameTable) 
		{
			
		}
	},
	PLAYING(){
		@Override
		void handleLogic(GameTable gameTable) {
			// TODO Auto-generated method stub
			
		}
		
	},
	FINISH(){
		@Override
		void handleLogic(GameTable gameTable) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	
	
	abstract void handleLogic(GameTable gameTable); 
}
