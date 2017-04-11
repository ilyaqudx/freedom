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
		try
		{
			Selector sel = Selector.open();
			ServerSocketChannel channel = ServerSocketChannel.open();
			channel.setOption(StandardSocketOptions.SO_RCVBUF, 32 * 1024);
			channel.setOption(StandardSocketOptions.SO_RCVBUF, 32 * 1024);
			channel.configureBlocking(false);
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
			addNewSession(channel);
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
		catch (Exception e) {
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
		//这儿又是自己把自己坑了
		//先绑定session再添加到nio,后放时经常出现STORAGETASK中的SIZE为0的情况,导致LENGTH也为0.UNDER_BUFFER_EXCEPTION,
		//这儿其实就是内存可见性导致的问题.session task is not null .but task size is 0.
		//试验一下.将task size set final 解决了SIZE为0的问题
		//试验2:将task size set volatile能否解决SIZE为0的问题?我觉得是可以的,但事实是不行的.
		//本身这儿的逻辑就应该先绑定TASK再放入NIO线程去处理.避免这种并发引起的数据问题.
		//这是一个很好的例子.并发的经验发生了内存可见性问题
		bindStorageTask(session);
		processor.addNewSession(session);
		return session;
	}

	private void bindStorageTask(NioSession session) throws IOException
	{
		session.task = StorageTaskPool.I.obtain();
		session.task.client_ip = ((InetSocketAddress)session.getChannel().getRemoteAddress())
				.getHostName();
		session.task.session = session;
	}
}
