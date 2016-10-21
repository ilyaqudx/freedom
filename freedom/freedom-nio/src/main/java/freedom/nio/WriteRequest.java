package freedom.nio;

import freedom.nio.future.WriteFuture;

public class WriteRequest {

	private IoSession session;
	private WriteFuture future;
	private Object msg;
	public WriteRequest(IoSession session, WriteFuture future, Object msg)
	{
		this.session = session;
		this.future = future;
		this.msg = msg;
	}
	public IoSession getSession()
	{
		return session;
	}
	public void setSession(IoSession session)
	{
		this.session = session;
	}
	public WriteFuture getFuture()
	{
		return future;
	}
	public void setFuture(WriteFuture future)
	{
		this.future = future;
	}
	public Object getMsg()
	{
		return msg;
	}
	public void setMsg(Object msg)
	{
		this.msg = msg;
	}
	
	
}
