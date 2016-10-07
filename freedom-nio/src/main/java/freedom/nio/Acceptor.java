package freedom.nio;

import java.io.IOException;

public interface Acceptor {

	public void start() throws IOException;
	
	public void shutdown();
	
	public boolean isRunning();
	
	public IoHandler getHandler();
	
}
