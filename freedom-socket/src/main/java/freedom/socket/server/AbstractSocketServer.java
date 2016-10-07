package freedom.socket.server;

import java.net.InetSocketAddress;

public abstract class AbstractSocketServer implements SocketServer {

	protected InetSocketAddress bindAddress;
	public AbstractSocketServer(InetSocketAddress bindAddress)
	{
		this.bindAddress = bindAddress;
	}
	
	public void close() throws Exception 
	{
		
	}

	public InetSocketAddress getBindAddress() 
	{
		return this.bindAddress;
	}

}
