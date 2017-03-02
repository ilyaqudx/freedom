package freedom.nio2;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NioProcessorPool {

	private NioProcessor[] pool;
	private Executor executor = Executors.newCachedThreadPool();
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
			pool[i] = new NioProcessor(executor,service);
		}
	}
	
	public NioProcessor getProcessor(NioSession session)
	{
		return pool[(int) (session.getId() % pool.length)];
	}
}
