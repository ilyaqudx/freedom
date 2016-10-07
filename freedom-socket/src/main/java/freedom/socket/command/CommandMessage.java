package freedom.socket.command;

import io.netty.channel.ChannelHandlerContext;

public class CommandMessage<T,V> {

	protected T in;
	protected V out;
	protected short reqCmd;
	protected short resCmd;
	protected LogicException ex;
	protected ChannelHandlerContext session;
	public ChannelHandlerContext getSession() {
		return session;
	}
	public CommandMessage<T,V> setSession(ChannelHandlerContext session) {
		this.session = session;
		return this;
	}
	public short getReqCmd() {
		return reqCmd;
	}
	public void setReqCmd(short reqCmd) {
		this.reqCmd = reqCmd;
	}
	public short getResCmd() {
		return resCmd;
	}
	public CommandMessage<T, V> setResCmd(String resCmd) {
		this.resCmd = Short.parseShort(resCmd);
		return this;
	}
	public T getIn() {
		return in;
	}
	public CommandMessage<T,V> setIn(T in) {
		this.in = in;
		return this;
	}
	public V getOut() {
		return out;
	}
	public CommandMessage<T,V> setOut(V out) {
		this.out = out;
		return this;
	}
	public LogicException getEx() {
		return ex;
	}
	public CommandMessage<T,V> setEx(LogicException ex) {
		this.ex = ex;
		return this;
	}
	public boolean hasError()
	{
		return ex != null;
	}
	@Override
	public String toString() {
		return "CommandMessage [in=" + in + ", out=" + out
				+ ", ex=" + ex + "]";
	}
}
