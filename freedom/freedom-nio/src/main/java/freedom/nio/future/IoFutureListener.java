package freedom.nio.future;

import freedom.nio.IoSession;

public interface IoFutureListener<T> {

	public void success(IoSession session,T v);
	
	public void fail(IoSession session,String msg);
}
