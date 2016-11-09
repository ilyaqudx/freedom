package freedom.nio.filter;

import freedom.nio.IoSession;
import freedom.nio.WriteRequest;
import freedom.nio.filter.DefaultFilterChain.FilterEntry;

public class LoggingFilter implements Filter {

	@Override
	public void connected(FilterEntry nextFilter, IoSession session, Object msg)
	{
		
	}

	@Override
	public void received(FilterEntry nextFilter, IoSession session, Object msg) 
	{
		//System.out.println("logging filter");
		nextFilter.fireRead(session, msg);
	}

	@Override
	public void disconnected(FilterEntry nextFilter, IoSession session,
			Object msg)
	{
		
	}

	@Override
	public void idle(FilterEntry nextFilter, IoSession session, Object msg)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(FilterEntry nextFilter, IoSession session, WriteRequest request)
	{
		nextFilter.fireWrite(session,request);
	}

	@Override
	public void sent(FilterEntry nextFilter, IoSession session, Object msg)
	{
		nextFilter.fireSent(session, (WriteRequest)msg);
	}
}
