package freedom.nio2;

public class NioProcessorPool {

	private NioProcessor[] pool;
	public NioProcessorPool(int capacity)
	{
		this.pool	  = new NioProcessor[capacity > 0 ? capacity : Runtime.getRuntime().availableProcessors() + 1];
	}
	
	public void initPool(AbstractNioService service)
	{
		for (int i = 0; i < pool.length; i++)
		{
			pool[i] = new NioProcessor(service);
		}
	}
	
	public NioProcessor getProcessor(NioSession session)
	{
		return pool[(int) (session.getId() % pool.length)];
	}
}
