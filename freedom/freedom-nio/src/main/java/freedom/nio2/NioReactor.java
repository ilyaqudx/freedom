package freedom.nio2;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public abstract class NioReactor implements Runnable{

	private String  name;
	protected Selector sel;
	protected AbstractNioService service;
	protected ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
		
		@Override
		public Thread newThread(Runnable r) 
		{
			return new Thread(r,name);
		}
	});
	public NioReactor(String name)
	{
		this.name    = name;
	}
	public NioReactor(String name,AbstractNioService service)
	{
		this.name    = name;
		this.service = service;
	}
	public void setService(AbstractNioService service)
	{
		this.service = service;
	}
	
	public void start()
	{
		if(service == null)
			throw new NullPointerException("service is null");
		executor.submit(this);
	}
	
	
	public abstract void open() throws IOException;
	
	public void eventLoop()
	{
		while(service.isActive())
		{
			try
			{
				sel.select(500L);
				Set<SelectionKey> keyset = sel.selectedKeys();
				keyset.forEach(this::interest);
				keyset.clear();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	protected abstract void interest(SelectionKey key);
	
	protected NioSession buildNewSession(SocketChannel channel)
	{
		return new NioSession(service, channel);
	}

	@Override
	public void run() 
	{
		try 
		{
			this.sel =  Selector.open();
			this.open();
			this.service.getProcessorPool().initPool(this.service);
			this.eventLoop();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
