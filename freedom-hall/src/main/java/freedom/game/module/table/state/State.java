package freedom.game.module.table.state;

public interface State {

	public static final int WAIT_PLAYER = 0,START_GAME = 1,SELECT_LACK = 2,PUT_CARD = 3,RESPONSE = 4,OUT_CARD = 5,SETTLEMENT = 7,DELAY = -1;
	
	public int  V();
	
	public void action();
	
	public void setTimeout();

	public void setTimeout(int timeout);
}
