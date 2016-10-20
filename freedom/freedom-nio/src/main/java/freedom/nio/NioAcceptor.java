package freedom.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.Executors;

import freedom.nio.codec.JSONCodec;
import freedom.nio.codec.ProtocolCodecFilter;
import freedom.nio.processor.DefaultIoProcessorPool;
import freedom.nio.processor.IoProcessor;
import freedom.nio.processor.IoProcessorPool;

public class NioAcceptor implements Acceptor {

	private InetSocketAddress localAddress;
	private Selector selector;
	private volatile boolean running;
	private IoHandler handler;
	private FilterChain filterChain;
	private IoProcessorPool<IoProcessor> processorPool;
	public NioAcceptor(InetSocketAddress localAddress,IoHandler handler)
	{
		this.handler       = handler; 
		this.localAddress  = localAddress;
		this.filterChain   = new DefaultFilterChain();
		this.processorPool = new DefaultIoProcessorPool();
		this.filterChain.addLast("logging", new LoggingFilter());
		this.filterChain.addLast("codec", new ProtocolCodecFilter(new JSONCodec()));
	}
	
	@Override
	public void start() throws IOException
	{
		if(running)return;
		this.running  = true;
		this.selector = Selector.open();
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		serverChannel.bind(localAddress);
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		acceptEventLoop();
	}

	private void acceptEventLoop()
	{
		Executors.newSingleThreadExecutor().submit(new Runnable() 
		{
			@Override
			public void run() {
				while(running)
				{
					try {
						int keys = selector.select(1000);
						if(keys > 0)
						{
							Iterator<SelectionKey> it = selector.selectedKeys().iterator();
							while(it.hasNext())
							{
								SelectionKey key = it.next();
								if(key.isAcceptable())
								{
									doAccept(key);
								}
								it.remove();
							}
						}
					} 
					catch (ClosedChannelException e) 
					{
						e.printStackTrace();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
			}

		});
	}
	
	private void newSession(SocketChannel channel) throws Exception
	{
		NioSession session = new NioSession(channel,handler,filterChain);
		IoProcessor processor = processorPool.getProcessor(session);
		processor.registSession(session);
	}
	
	private void doAccept(SelectionKey key)
	{
		try
		{
			SocketChannel channel = ((ServerSocketChannel)key.channel()).accept();
			channel.configureBlocking(false);
			newSession(channel);
			//filterChain.fireConnect(null);
		} 
		catch (ClosedChannelException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	@Override
	public void shutdown() 
	{
		if(!running)
			return;
		this.running = false;
	}

	@Override
	public boolean isRunning()
	{
		return running;
	}

	@Override
	public IoHandler getHandler() 
	{
		return this.handler;
	}

}
