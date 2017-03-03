package freedom.nio2;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NioProcessorPool {

	public static final AtomicInteger processorId = new AtomicInteger(1);
	private NioProcessor[] pool;
	private Executor executor = Executors.newCachedThreadPool(new ThreadFactory() {
		
		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r,"NioProcessor-" + processorId.getAndIncrement());
		}
	});
	public static final int DEFAULT_PROCESSORT_COUNT = Runtime.getRuntime().availableProcessors() + 1;
	public NioProcessorPool(AbstractNioService service)
	{
		this(DEFAULT_PROCESSORT_COUNT,service);
	}
	public NioProcessorPool(int capacity,AbstractNioService service)
	{
		if(capacity <= 0)
			throw new IllegalArgumentException("capacity must > 0");
		this.pool	  = new NioProcessor[capacity];
		for (int i = 0; i < pool.length; i++)
		{
			pool[i] = new NioProcessor(i,executor,service);
		}
	}
	
	public NioProcessor getProcessor(NioSession session)
	{
		return pool[(int) (session.getId() % pool.length)];
	}
}
