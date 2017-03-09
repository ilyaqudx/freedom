package freedom.nio2;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicReference;

public abstract class NioReactor implements Runnable{

	protected String  name;
	protected Selector sel;
	protected AbstractNioService service;
	protected volatile boolean running = true;
	protected AtomicReference<NioReactor> reference = new AtomicReference<>();
	protected ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
		
		@Override
		public Thread newThread(Runnable r) 
		{
			return new Thread(r,name);
		}
	});
	
	public NioReactor(String name,AbstractNioService service)
	{
		this.name    = name;
		this.service = service;
		this.sel     = SelectorFactory.open();
	}
	
	public void start()
	{
		try 
		{
			NioReactor reactor = reference.get();
			synchronized (service) 
			{
				if(reactor == null){
					if(reference.compareAndSet(null, this)){
						this.openSocket();
						executor.submit(this);
					}
				}
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	public void eventLoop()
	{
		while(service.isActive() && running)
		{
			try
			{
				sel.select(100L);
				Set<SelectionKey> keyset = sel.selectedKeys();
				keyset.forEach(this::interest0);
				keyset.clear();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public void run() 
	{
		this.eventLoop();
	}
	
	
	public abstract void openSocket() throws IOException;
	
	protected abstract void interest(SelectionKey key) throws IOException;
	
	protected void interest0(SelectionKey key)
	{
		try 
		{
			boolean isAcceptor  = this instanceof AcceptorReactor;
			if(isAcceptor){
				if(key.isAcceptable())
					interest(key);
			}
			else{
				if(key.isConnectable())
					interest(key);
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	protected void exitThread()
	{
		if(this.reference.compareAndSet(this, null))
			this.running = false;
	}
}
