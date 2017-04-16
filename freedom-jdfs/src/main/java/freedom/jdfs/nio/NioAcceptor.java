package freedom.jdfs.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import freedom.jdfs.storage.StorageTaskPool;

public class NioAcceptor {

	private InetSocketAddress address;
	/**等待队列.任务繁忙时,排队队列.可有效减少connect refuse exception*/
	private int backlog = 150;
	private volatile boolean running;
	private int processor = Runtime.getRuntime().availableProcessors() + 1;
	public static NioProcessor[] processors;
	public NioAcceptor(InetSocketAddress address)
	{
		this.address = address;
		//初始化processor
		processors = new NioProcessor[processor];
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
		Selector sel = Selector.open();
		ServerSocketChannel channel = ServerSocketChannel.open();
		channel.configureBlocking(false);
		channel.bind(address, backlog);
		channel.register(sel, SelectionKey.OP_ACCEPT);
		startup(sel);
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

	private void accept(SelectionKey key)
	{
		SocketChannel channel = null;
		try 
		{
			channel = ((ServerSocketChannel)key.channel()).accept();
			channel.setOption(StandardSocketOptions.SO_RCVBUF, 32 * 1024);
			channel.setOption(StandardSocketOptions.SO_RCVBUF, 32 * 1024);
			addNewSession(channel);
		} 
		catch (IOException e)
		{
			try
			{
				if(channel != null)
				{
					channel.close();
				}
			}
			catch (IOException ex) 
			{
				ex.printStackTrace();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
		bindStorageTask(session);
		processor.addNewSession(session);
		return session;
	}

	private void bindStorageTask(NioSession session) throws IOException
	{
		session.task = StorageTaskPool.I.obtain();
		session.task.clientIp = ((InetSocketAddress)session.getChannel().getRemoteAddress()).getHostName();
		session.task.session = session;
	}
}
