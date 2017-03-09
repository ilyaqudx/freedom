package freedom.cache.client;

import freedom.nio2.IdleState;
import freedom.nio2.NioHandler;
import freedom.nio2.NioSession;

public class NioClientHandler implements NioHandler {

	@Override
	public void onCreated(NioSession session) 
	{
		System.out.println("成功连接服务器");
	}

	@Override
	public void onReceived(NioSession session, Object msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWritten(NioSession session) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onIdle(NioSession session, IdleState state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onException(NioSession session, Throwable throwable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClosed(NioSession abstractNioSession) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWriteSuspend(NioSession session, long qps) {
		// TODO Auto-generated method stub

	}

}
