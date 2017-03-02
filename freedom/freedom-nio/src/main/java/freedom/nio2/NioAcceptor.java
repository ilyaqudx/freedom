package freedom.nio2;

import java.net.InetSocketAddress;

public class NioAcceptor extends AbstractNioService{

	public NioAcceptor(InetSocketAddress bindAddress, NioHandler handler,ProtocolCodec codec)
	{
		this(bindAddress, handler, codec, null);
	}
	public NioAcceptor(InetSocketAddress bindAddress, NioHandler handler,ProtocolCodec codec,NioChannelConfig channelConfig) 
	{
		super(bindAddress, handler, codec, channelConfig,NioProcessorPool.DEFAULT_PROCESSORT_COUNT,AcceptorReactor.class,new AcceptorListener());
	}
	
}
