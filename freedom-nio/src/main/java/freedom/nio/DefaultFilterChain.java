package freedom.nio;

import java.io.IOException;
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
		public void write(FilterEntry nextFilter, IoSession session, Object msg)
		{
			if(msg instanceof ByteBuffer)
			{
				ByteBuffer buffer = (ByteBuffer) msg;
				if(buffer.hasRemaining())
				{
					try 
					{
						session.getChannel().write(buffer);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
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
		public void write(FilterEntry nextFilter,IoSession session,Object msg)
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
	public void fireWrite( Object data)
	{
		tailor.pre.fireWrite(session, data);
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
		public void fireWrite(IoSession session,Object msg) 
		{
			this.filter.write(pre, session, msg);
		}
	}


	@Override
	public List<FilterEntry> getEntries()
	{
		return entries;
	}
}
