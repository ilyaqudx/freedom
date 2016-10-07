package freedom.nio;

import freedom.nio.DefaultFilterChain.FilterEntry;

public class LoggingFilter implements Filter {

	@Override
	public void connected(FilterEntry nextFilter, IoSession session, Object msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void received(FilterEntry nextFilter, IoSession session, Object msg) 
	{
		//System.out.println("logging filter");
		nextFilter.fireRead(session, msg);
	}

	@Override
	public void disconnected(FilterEntry nextFilter, IoSession session,
			Object msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void idle(FilterEntry nextFilter, IoSession session, Object msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(FilterEntry nextFilter, IoSession session, Object msg) {
		// TODO Auto-generated method stub
		
	}



}
