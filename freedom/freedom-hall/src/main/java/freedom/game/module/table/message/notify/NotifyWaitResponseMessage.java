package freedom.game.module.table.message.notify;

import java.util.List;

import freedom.game.module.table.entity.Operator;

public class NotifyWaitResponseMessage extends NotifyMessage {

	private List<Operator> opts;
	
	public List<Operator> getOpts() {
		return opts;
	}

	public void setOpts(List<Operator> opts) {
		this.opts = opts;
	}

	public NotifyWaitResponseMessage(String cmd,List<Operator> opts) 
	{
		super.setCmd(cmd);
		this.opts = opts;
	}
}
