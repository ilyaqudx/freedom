package freedom.game.module.table;

import freedom.game.module.table.entity.Operator;
import freedom.game.module.table.entity.Player;
import freedom.game.module.table.entity.Table;
import freedom.game.module.table.state.State;
import freedom.socket.command.LogicException;

public interface Logic {

	public Table getTable();

	public void initCard();

	public boolean isTimeout();

	public void setState(State nextState);

	public void setTimeout(int timeout);

	public void nextPlayerPutCard();

	public int getTimeout();

	public void setDelayState(int delayTimeout, State callbackState);

	public void resetOpts();

	public void start();

	public boolean hasRealPlayer();

	public void sitdown(Player player);

	public Player ready(long playerId);

	public Player computeNextPlayer(Player player);

	public void nextPlayer(Player nextPlayer, State state);

	public long getLastInTime();

	public void peng(Player player,Operator operator)throws LogicException;
	
	public void gang(Player player,Operator operator)throws LogicException;
	
	public void hu(Player player,Operator operator)throws LogicException;
	
	public void guo(boolean isSelf)throws LogicException;
}
