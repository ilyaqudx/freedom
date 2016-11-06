package freedom.study.interview.design.state.enums;

import freedom.study.interview.design.state.GameTable;

public enum GameState implements State{

	WAIT_PLAERY(1)
	{
		@Override
		public void action(GameTable table)
		{
			
		}
	},
	
	PLAYING(2)
	{
		@Override
		public void action(GameTable table) 
		{
			
		}
		
	}
	;
	
	int value;
	private GameState(int value) 
	{
		this.value = value;
	}
}
