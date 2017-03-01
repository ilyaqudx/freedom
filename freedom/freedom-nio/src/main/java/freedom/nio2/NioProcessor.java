package freedom.nio2;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class NioProcessor implements Runnable{

	private static final AtomicInteger processorId = new AtomicInteger(1);
	private int        id;
	private Selector   sel;
	private AbstractNioService service;
	private AtomicReference<NioProcessor> reference;
	private Queue<NioSession> newSessionQueue ;
	private Queue<NioSession> flushSessionQueue;
	private long lastCheckIdleTime;
	private List<NioSession> allSessions = new LinkedList<NioSession>();
	
	static final ExecutorService executor = Executors.newCachedThreadPool(new ThreadFactory() {
		
		@Override
		public Thread newThread(Runnable r) {
			// TODO Auto-generated method stub
			return new Thread(r,"NioProcessor-" + processorId.incrementAndGet());
		}
	});
	
	public NioProcessor(AbstractNioService service)
	{
		this.id        = processorId.getAndIncrement();
		this.sel       = SelectorFactory.open();
		this.service   = service;
		this.reference = new AtomicReference<NioProcessor>();
		this.newSessionQueue = new ConcurrentLinkedQueue<NioSession>();
		this.flushSessionQueue = new ConcurrentLinkedQueue<NioSession>();
	}
	
	public void regiestSession(NioSession session)
	{
		allSessions.add(session);
		newSessionQueue.add(session);
		NioProcessor v = reference.get();
		if(v == null){
			if(reference.compareAndSet(null,this))
				this.startup();
		}
		sel.wakeup();
	}
	
	public void addFlushSession(NioSession session) 
	{
		flushSessionQueue.add(session);
	}
	
	private void startup()
	{
		/*Executors.newSingleThreadExecutor(new ThreadFactory() {
			
			@Override
			public Thread newThread(Runnable r) {
				// TODO Auto-generated method stub
				return new Thread(r,"NioProcessor-" + NioProcessor.this.id);
			}
		}).submit(this);*/
		executor.execute(this);
	}
	
	@Override
	public void run()
	{
		while(service.isActive())
		{
			try
			{
				/***此处写了之后sel的write_opt没有被清楚,不停的进行写事件.为什么?
				 * 
				 * NIO的事件处理机制
				 * readable  : 在注册了读操作的情况的,如果接收缓冲区中有数据,则为可读状态,即readable
				 * writeable : 当注册了写操作的情况下,如果写缓存区中有多余空间,则为可写,sel.select时则会一直返回可写事件.
				 * 所以一般情况下写是不需要注册的,如果非要注册写事件,那么在将数据发送完成之后,应立即取消写事件.转换为读事件.
				 * 否则可能会限入死循环中.
				 * 
				 */
				int n = sel.select(500L);
				
				if(n > 0){
					process();
				}
				
				handleNewSession();
				
				notifyIdleSession();
				
				flush0();
				
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void notifyIdleSession() 
	{
		long now = System.currentTimeMillis();
		if(now - lastCheckIdleTime >= 1000)
		{
			this.lastCheckIdleTime = now;
			for (NioSession session : allSessions) 
			{
				session.notifyIdle(now);
			}
		}
	}

	private void process() 
	{
		Set<SelectionKey> keys = sel.selectedKeys();
		
		keys.forEach(this::processKey);
		
		keys.clear();
	}
	
	private void handleNewSession()
	{
		for (NioSession session = newSessionQueue.poll();session != null;session = newSessionQueue.poll())
		{
			try 
			{
				session.regiestRead(sel);
			} 
			catch (ClosedChannelException e) 
			{
				session.close();
				e.printStackTrace();
			}
		}
	}

	private void processKey(SelectionKey key)
	{
		if(key.isReadable())
			read0(key);
	}

	private void flush0() 
	{
		for (NioSession session = flushSessionQueue.poll();session != null;session = flushSessionQueue.poll()) {
			while(!session.getSendPacketQueue().isEmpty()){
				session.flush();
			}
		}
	}

	private void read0(SelectionKey key)
	{
		SessionContext.attachment(key).read();
	}

	public Selector getSelector() 
	{
		return sel;
	}
}
