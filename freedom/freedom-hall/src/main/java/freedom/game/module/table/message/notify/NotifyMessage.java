package freedom.game.module.table.message.notify;

public class NotifyMessage {

	protected short cmd;
	protected int   timeout = 10;
	
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public NotifyMessage() 
	{
		
	}
	public NotifyMessage(String cmd) 
	{
		this.cmd = Short.parseShort(cmd);
	}

	public short getCmd() {
		return cmd;
	}

	public NotifyMessage setCmd(String cmd) {
		this.cmd = Short.parseShort(cmd);
		return this;
	}

	@Override
	public String toString() {
		return "NotifyMessage [cmd=" + cmd + "]";
	}
	
}
