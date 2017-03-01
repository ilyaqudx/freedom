package freedom.nio2;

import java.net.InetSocketAddress;

public interface Service {

	public void start();
	
	public boolean isActive();
	
	public long	   getActivationTime();
	
	public InetSocketAddress getBindAddress();
	
	public NioHandler getHandler();
	
	public ProtocolCodec getProtocolCodec();
	
	public NioChannelConfig getChannelConfig();
	
	public ServiceListener getServiceListener();
}
