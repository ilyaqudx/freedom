package freedom.nio.future;

public interface IoFuture<T> {

	
	T get();
	
	T get(long timeout);
	
	void set(T v);
	
	void await();
	
	void await(long timeout);
	
	boolean isDone();
	
	void addListener(IoFutureListener<T> listener);
}
