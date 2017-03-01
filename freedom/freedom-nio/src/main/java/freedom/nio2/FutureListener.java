package freedom.nio2;

public interface FutureListener {

	public void complete(long writeBytes);
	
	public void exception(Throwable e);
}
