package freedom.nio.processor;

import freedom.nio.IoSession;

public interface IoProcessorPool<P> {

	public P getProcessor(IoSession session)throws Exception;
}
