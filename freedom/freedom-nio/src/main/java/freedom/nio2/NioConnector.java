package freedom.nio2;

import java.net.InetSocketAddress;

public class NioConnector extends AbstractNioService {

	public NioConnector(InetSocketAddress bindAddress,NioHandler handler,ProtocolCodec codec)
	{
		this(bindAddress, handler, codec, null);
	}
	
	public NioConnector(InetSocketAddress bindAddress,NioHandler handler,ProtocolCodec codec,NioChannelConfig channelConfig)
	{
		super(bindAddress, handler, codec, channelConfig,1,ConnectorReactor.class,new ConnectorListener());
	}
	
	
}
