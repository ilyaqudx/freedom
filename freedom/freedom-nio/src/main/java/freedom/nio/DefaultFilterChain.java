package freedom.nio;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.util.Assert;

/**
 * 过滤器链(在收到连接请求/有收到到来进行一系列过滤操作)
 * */
public class DefaultFilterChain implements FilterChain {

	private IoSession session;
	private List<FilterEntry> entries = new CopyOnWriteArrayList<FilterEntry>();
	private FilterEntry head   = new FilterEntry("head",new Filter() {
		
		@Override
		public void write(FilterEntry nextFilter, IoSession session, WriteRequest request)
		{
			Object msg = request.getMsg();
			if(msg instanceof ByteBuffer)
			{
				ByteBuffer buffer = (ByteBuffer) msg;
				int bufferLen= buffer.remaining();
				if(bufferLen > 0)
				{
					session.getWriteRequestQueue().offer(request);
					session.getProcessor().addPendingWriteSession(session);
					/*int retCount = 0;
						while(buffer.hasRemaining())
						{
							int writen = session.getChannel().write(buffer);
							retCount += writen;
							System.out.println(writen);
						}
						
						if(retCount == bufferLen)
						{
							session.getFilterChain().fireSent(retCount);
							request.getFuture().set(retCount);
						}*/
				}
			}
		}
		
		@Override
		public void received(FilterEntry nextFilter, IoSession session,
				Object msg)
		{
			nextFilter.fireRead(session, msg);
		}
		
		@Override
		public void idle(FilterEntry nextFilter, IoSession session, Object msg) 
		{
			
		}
		
		@Override
		public void disconnected(FilterEntry nextFilter, IoSession session,
				Object msg)
		{
			
		}
		
		@Override
		public void connected(FilterEntry nextFilter, IoSession session,
				Object msg) 
		{
			
		}

		@Override
		public void sent(FilterEntry nextFilter, IoSession sesion, Object msg)
		{
			
		}
	}, null, null);
	private FilterEntry tailor = new FilterEntry("tailor",new Filter() {

		@Override
		public void connected(FilterEntry nextFilter,IoSession session,Object msg)
		{
			 session.getHandler().connected(session, msg);
		}

		@Override
		public void received(FilterEntry nextFilter,IoSession session,Object msg)
		{
			 session.getHandler().received(session, msg);
		}

		@Override
		public void disconnected(FilterEntry nextFilter,IoSession session,Object msg)
		{
			 session.getHandler().disconnected(session, msg);
		}

		@Override
		public void idle(FilterEntry nextFilter,IoSession session,Object msg)
		{
			session.getHandler().idle(session, msg);
		}

		@Override
		public void write(FilterEntry nextFilter, IoSession session, WriteRequest request) {
			session.getHandler().write(session, request);
			
		}

		@Override
		public void sent(FilterEntry nextFilter, IoSession sesion, Object msg)
		{
			session.getHandler().write(session, msg);
		}
		
		
	}, head, null){

	};
	public DefaultFilterChain()
	{
		//problem 4 
	}
	public DefaultFilterChain(IoSession session,FilterChain filterChain)
	{
		this.session   = session;
		this.head.next = tailor;
		for(FilterEntry e : filterChain.getEntries())
		{
			addLast(e.name,e.filter);
		}
	}
	
	public void addLast(String name , Filter filter)
	{
		Assert.notNull(filter,String.format("%s must be not null", name));
		FilterEntry pre  = tailor.pre;
		FilterEntry last = new FilterEntry(name,filter, pre, tailor);
		pre.next   = last;
		tailor.pre = last;
		entries.add(last);
	}
	
	@Override
	public void fireConnect(Object data)
	{
		
	}
	
	@Override
	public void fireRead(Object data) 
	{
		head.fireRead(session, data);
	}

	@Override
	public void fireWrite( WriteRequest request)
	{
		tailor.pre.fireWrite(session, request);
	}
	

	public static class FilterEntry
	{
		public String name;
		public FilterEntry pre;
		public FilterEntry next;
		private Filter filter;
		public FilterEntry(String name,Filter filter,FilterEntry pre,FilterEntry next)
		{
			this.name   = name;
			this.pre    = pre;
			this.next   = next;
			this.filter = filter;
		}
		public FilterEntry getPre() {
			return pre;
		}
		public void setPre(FilterEntry pre) {
			this.pre = pre;
		}
		public FilterEntry getNext() {
			return next;
		}
		public void setNext(FilterEntry next) {
			this.next = next;
		}
		public Filter getFilter() {
			return filter;
		}
		public void setFilter(Filter filter) {
			this.filter = filter;
		}
		@Override
		public String toString() {
			return "FilterEntry [pre=" + pre + ", next=" + next + ", filter="
					+ filter + "]";
		}
		public void fireConnect(IoSession session,Object msg)
		{
			this.filter.connected(next, session, msg);
		}
		public void fireDisconnect(IoSession session,Object msg)
		{
			this.filter.disconnected(next, session, msg);
		}
		public void fireRead(IoSession session,Object msg)
		{
			this.filter.received(next, session, msg);
		}
		public void fireWrite(IoSession session,WriteRequest request) 
		{
			this.filter.write(pre, session, request);
		}
		public void fireSent(IoSession session,int len)
		{
			this.filter.sent(next, session, len);
		}
	}


	@Override
	public List<FilterEntry> getEntries()
	{
		return entries;
	}
	@Override
	public void fireSent(int len)
	{
		head.fireSent(session, len);
	}
}
