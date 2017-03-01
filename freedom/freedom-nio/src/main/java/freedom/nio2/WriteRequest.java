package freedom.nio2;

public class WriteRequest {

	private Object msg;
	private WriteFuture future;
	
	public WriteRequest(Object msg,WriteFuture future)
	{
		this.msg = msg;
		this.future = future;
	}

	public Object getMsg() {
		return msg;
	}

	public void setMsg(Object msg) {
		this.msg = msg;
	}

	public WriteFuture getFuture() {
		return future;
	}

	public void setFuture(WriteFuture future) {
		this.future = future;
	}
}
