package freedom.nio;

import freedom.nio.DefaultFilterChain.FilterEntry;

public interface Filter {

	public void connected(FilterEntry nextFilter,IoSession session,Object msg);
	
	public void received(FilterEntry nextFilter,IoSession session,Object msg);

	public void disconnected(FilterEntry nextFilter,IoSession session,Object msg);
	
	public void idle(FilterEntry nextFilter,IoSession session,Object msg);
	
	public void write(FilterEntry nextFilter,IoSession session,WriteRequest request);
	
	public void sent(FilterEntry nextFilter,IoSession sesion,Object msg);
}
