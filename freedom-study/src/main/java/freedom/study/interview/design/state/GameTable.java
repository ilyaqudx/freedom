package freedom.study.interview.design.state;

import freedom.study.interview.design.state.enums.GameState;
import freedom.study.interview.design.state.enums.State;

public class GameTable {

	
	private State state;
	
	public GameTable()
	{
		setState(GameState.PLAYING);
	}
	
	public void setState(State state)
	{
		this.state = state;
	}
	
	public void update()
	{
		state.action(this);
	}
}
