package freedom.jdfs.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioAcceptor {

	private InetSocketAddress address;
	private int backlog = 50;
	private volatile boolean running;
	private int processor = Runtime.getRuntime().availableProcessors() + 1;
	private NioProcessor[] processors;
	public NioAcceptor(InetSocketAddress address)
	{
		this.address = address;
		//初始化processor
		for (int i = 0; i < processor; i++)
			processors[i] = new NioProcessor(i+1);
	}
	
	public void start() throws Exception
	{
		synchronized (this) 
		{
			if(running)
				throw new Exception("服务器正在启动中");
			running = true;
		}
		try
		{
			Selector sel = Selector.open();
			ServerSocketChannel channel = ServerSocketChannel.open();
			channel.configureBlocking(false);
			channel.setOption(StandardSocketOptions.SO_LINGER, 16384);
			channel.setOption(StandardSocketOptions.SO_SNDBUF, 16384);
			channel.bind(address, backlog);
			
			channel.register(sel, SelectionKey.OP_ACCEPT);
			
			startup(sel);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void startup(Selector sel) throws Exception 
	{
		while(running)
		{
			sel.select(1000);
			Iterator<SelectionKey> keys = sel.selectedKeys().iterator();
			while(keys.hasNext())
			{
				SelectionKey key = keys.next();
				if(key.isAcceptable())
				   accept(key);
				else if(key.isReadable())
					read(key);
				keys.remove();
			}
		}
	}
	
	public void shutdown()
	{
		synchronized (this) {
			if(!running)
				return;
			running = false;
		}
	}

	private void read(SelectionKey key) 
	{
		
	}

	private void accept(SelectionKey key)
	{
		SocketChannel channel = null;
		try 
		{
			channel = (SocketChannel)key.channel();
			NioSession session = addNewSession(channel);
			key.attach(session);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			try
			{
				channel.close();
			}
			catch (IOException ex) 
			{
				ex.printStackTrace();
			}
		}
	}
	
	private NioSession newSession(SocketChannel channel) throws IOException
	{
		channel.configureBlocking(false);
		return new NioSession(channel);
	}
	
	private NioProcessor getProcessor(NioSession session)
	{
		NioProcessor processor = session.getProcessor();
		if(null == processor)
		{
			processor = processors[(int) (session.id % NioAcceptor.this.processor)];
			session.setProcessor(processor);
		}
		return processor;
	}
	
	private NioSession addNewSession(SocketChannel channel) throws IOException
	{
		NioSession session = newSession(channel);
		NioProcessor processor = getProcessor(session);
		processor.addNewSession(session);
		return session;
	}
}
