package freedom.study.concurrent.future;

public interface Future<T> {

	public boolean isDone();
	
	public void set(T v);
	
	public T get();
	
	public T get(int timeout);
}
