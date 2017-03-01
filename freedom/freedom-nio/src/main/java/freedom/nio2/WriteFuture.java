package freedom.nio2;


public class WriteFuture {

	private FutureListener listener;
	private int value;
	
	public WriteFuture()
	{
		
	}
	public WriteFuture(FutureListener listener)
	{
		addFutureListener(listener);
	}
	private void addFutureListener(FutureListener listener)
	{
		if(null == listener)
			throw new NullPointerException("listener is null");
		this.listener = listener;
	}
	
	public FutureListener getFutureListener()
	{
		return this.listener;
	}
	
	public boolean isWritten()
	{
		return value > 0;
	}
	
	public void setValue(int value)
	{
		if(isWritten())return;
		synchronized (this) {
			this.value = value;
			this.notifyAll();
		}
	}
	
	public int get()
	{
		return get(5000);
	}
	public int get(long waittime)
	{
		if(isWritten())
			return value;
		synchronized (this) {
			try {
				this.wait(waittime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return value;
	}
}
