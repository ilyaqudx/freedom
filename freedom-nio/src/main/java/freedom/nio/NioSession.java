package freedom.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class NioSession implements IoSession{

	private SocketChannel channel;
	private IoHandler handler;
	private FilterChain filterChain;
	private SelectionKey key;
	private Map<String,Object> attrs = new ConcurrentHashMap<String,Object>();
	public static final String FRAGMENT = "fragment";
	private long sessionId;
	public static final AtomicLong id = new AtomicLong(0);
	
	public NioSession(SocketChannel channel,IoHandler handler,FilterChain filterChain)
	{
		this.sessionId      = id.incrementAndGet();
		this.channel 		= channel;
		this.handler 		= handler;
		this.filterChain 	= new DefaultFilterChain(this,filterChain);
	}
	
	public boolean hasFragMent()
	{
		return attrs.get(FRAGMENT) != null;
	}
	
	/**
	 * Exception in thread "pool-2-thread-1" java.nio.BufferOverflowException
	 * */
	public void storeFragment(ByteBuffer buffer)
	{
		
		ByteBuffer fragment = ByteBuffer.allocate(buffer.limit() - buffer.position());
		fragment.put(buffer.array(), buffer.position(),fragment.capacity());
		attrs.put(FRAGMENT, fragment);
	}
	
	public void write(Object message)throws IOException
	{
		filterChain.fireWrite(message);
	}

	@Override
	public IoHandler getHandler() 
	{
		return handler;
	}

	@Override
	public FilterChain getFilterChain()
	{
		return filterChain;
	}

	@Override
	public void regiest(Selector sel) throws ClosedChannelException
	{
		setSelectionKey(channel.register(sel, SelectionKey.OP_READ,this));
	}

	public SelectionKey getSelectionKey()
	{
		return this.key;
	}
	
	private void setSelectionKey(SelectionKey key)
	{
		this.key = key;
	}

	@Override
	public long getId() 
	{
		return sessionId;
	}

	@Override
	public Object getAttr(String attr) {
		// TODO Auto-generated method stub
		return attrs.get(attr);
	}

	@Override
	public void setAttr(String attr, Object value) {
		// TODO Auto-generated method stub
		attrs.put(attr, value);
	}

	@Override
	public SocketChannel getChannel() {
		// TODO Auto-generated method stub
		return channel;
	}

	@Override
	public boolean hasFragment() {
		// TODO Auto-generated method stub
		return attrs.get(FRAGMENT) != null;
	}

	@Override
	public int read(ByteBuffer buffer) throws IOException
	{
		return channel.read(buffer);
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		channel.close();
	}

	@Override
	public void clearFragment()
	{
		attrs.remove(FRAGMENT);
	}
	
	
}
