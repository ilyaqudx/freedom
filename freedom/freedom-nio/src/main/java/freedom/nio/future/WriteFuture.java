package freedom.nio.future;

import freedom.nio.IoSession;

public class WriteFuture implements IoFuture<Boolean> {

	private IoFutureListener<Boolean> listener;
	private IoSession session;
	private Boolean value;
	private volatile boolean done;
	public WriteFuture(IoSession session)
	{
		this.session = session;
	}

	@Override
	public Boolean get() 
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
	public Boolean get(long timeout) 
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
	public void addListener(IoFutureListener<Boolean> listener) 
	{
		this.listener = listener;
	}

	@Override
	public void set(Boolean v)
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
