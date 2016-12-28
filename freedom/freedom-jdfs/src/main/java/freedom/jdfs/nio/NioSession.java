package freedom.jdfs.nio;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class NioSession {

	private SocketChannel channel;
	public final long id;
	public final String name;
	private NioProcessor processor;
	protected static final AtomicLong unionIdCreater = new AtomicLong(0);
	private Map<String,Object> attrs = new HashMap<String,Object>();
	static final String FRAGMENT = "fragment";
	public NioSession(SocketChannel channel)
	{
		this.channel = channel;
		this.id      = unionIdCreater.incrementAndGet();
		this.name    = "NioSession-" + id;
	}

	public void setProcessor(NioProcessor processor) {
		this.processor = processor;
	}
	public NioProcessor getProcessor()
	{
		return processor;
	}
	
	public void registerRead(Selector sel)
	{
		if(channel.isConnected())
		{
			try
			{
				channel.register(sel, SelectionKey.OP_READ);
			}
			catch (ClosedChannelException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public SocketChannel getChannel()
	{
		return this.channel;
	}
	
	public void storeFragment(ByteBuffer fragment)
	{
		if(fragment.hasRemaining())
		{
			attrs.put(FRAGMENT, ByteBuffer.wrap(fragment.array(), fragment.position(), fragment.remaining()));
		}
	}
	
	public boolean hasFragment()
	{
		return null != attrs.get(FRAGMENT);
	}
	
	public ByteBuffer pollFragment()
	{
		return (ByteBuffer) attrs.get(FRAGMENT);
	}
}
