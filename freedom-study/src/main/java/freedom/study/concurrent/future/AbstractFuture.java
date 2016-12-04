package freedom.study.concurrent.future;

public class AbstractFuture<T> implements Future<T> {

	private boolean ready;
	private T value;

	@Override
	public boolean isDone() 
	{
		return ready;
	}

	@Override
	public T get() 
	{
		return get(15 * 1000);
	}

	public synchronized T get(int timeout) 
	{
		if (!ready)
		{
			try
			{
				this.wait(timeout);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		return value;
	}

	@Override
	public synchronized void set(T v) 
	{
		if (ready)
			return;
		this.ready = true;
		this.value = v;
		this.notify();
	}

}
