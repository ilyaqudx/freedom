package freedom.nio.future;

import freedom.nio.IoSession;

public class WriteFutureListener implements IoFutureListener<Object>{

	@Override
	public void success(IoSession session, Object v) 
	{
		//can remove write request from queue
	}

	@Override
	public void fail(IoSession session, String msg)
	{
		//can execute resend policy
	}

}
