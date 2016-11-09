package freedom.nio.future;

import freedom.nio.IoSession;

public class WriteFuture implements IoFuture<Integer> {

	private IoFutureListener<Integer> listener;
	private IoSession session;
	private Integer value;
	private volatile boolean done;
	public WriteFuture(IoSession session)
	{
		this.session = session;
	}

	@Override
	public Integer get() 
	{
		synchronized (this) 
		{
			if(!isDone())
				try 
				{
					this.wait();
				} 
				catch (InterruptedException e) 
				{
					this.notifyAll();
				}
		}
		return value;
	}

	@Override
	public Integer get(long timeout) 
	{
		synchronized (this) 
		{
			if(!isDone())
				try 
				{
					this.wait(timeout);
				} 
				catch (InterruptedException e) 
				{
					this.notifyAll();
				}
		}
		return value;
	}

	@Override
	public void await()
	{
		
	}

	@Override
	public void await(long timeout)
	{
		//this.timeout = Utils.checkRange(timeout,0, Integer.MAX_VALUE);
	}

	@Override
	public boolean isDone()
	{
		return done;
	}

	@Override
	public void addListener(IoFutureListener<Integer> listener) 
	{
		this.listener = listener;
	}

	@Override
	public void set(Integer v)
	{
		synchronized (this) 
		{
			if(!done)
				done = true;
			this.value = v;
			this.notifyAll();
			listener.success(session,value);
		}
	}

	

}
