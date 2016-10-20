package freedom.nio.processor;

import freedom.nio.Channel;
import freedom.nio.IoSession;

public interface IoProcessor {

	public IoSession getSession(Channel channel);
	
	public void registSession(IoSession session);
}
