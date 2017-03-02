package freedom.nio2;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public abstract class AbstractNioService implements NioService {

	protected NioHandler handler;
	protected ProtocolCodec codec;
	protected NioChannelConfig channelConfig;
	protected InetSocketAddress bindAddress;
	protected NioProcessorPool  processorPool;
	protected NioReactor		reactor;
	protected ServiceListener   listener;
	protected volatile boolean active;
	protected volatile long activationTime;
	
	public AbstractNioService(InetSocketAddress bindAddress,NioHandler handler, ProtocolCodec codec,NioChannelConfig channelConfig,
			int processorCount,Class<? extends NioReactor> reactorCls,ServiceListener listener)
	{
		if(null == bindAddress)		throw new IllegalArgumentException("bindAddress is null");
		if(null == handler)			throw new NullPointerException("handler is null");
		if(null == codec)			throw new NullPointerException("protocolCodec is null");
		this.bindAddress = bindAddress;
		this.handler = handler;
		this.codec = codec;
		this.channelConfig = channelConfig == null ? new NioChannelConfig() : channelConfig;
		this.processorPool = new NioProcessorPool(processorCount,this);
		this.reactor       = Utils.invokeCtor(reactorCls, AbstractNioService.class, this);
		this.listener      = listener;
		this.listener.addService(this);
	}

	@Override
	public boolean isActive() {
		return this.active;
	}

	@Override
	public long getActivationTime() {
		return this.activationTime;
	}

	@Override
	public NioHandler getHandler() {
		// TODO Auto-generated method stub
		return this.handler;
	}

	@Override
	public ProtocolCodec getProtocolCodec() {
		// TODO Auto-generated method stub
		return this.codec;
	}
	
	@Override
	public InetSocketAddress getBindAddress() {
		// TODO Auto-generated method stub
		return this.bindAddress;
	}

	@Override
	public NioChannelConfig getChannelConfig()
	{
		return this.channelConfig;
	}
	
	@Override
	public ServiceListener getServiceListener() 
	{
		return this.listener;
	}

	public NioProcessorPool getProcessorPool()
	{
		return this.processorPool;
	}
	protected NioSession buildNewSession(SocketChannel channel)
	{
		return new NioSession(this, channel);
	}
	public void start()
	{
		synchronized (this) 
		{
			if(isActive())return;
			active = true;
		}
		activationTime = System.currentTimeMillis();
		this.reactor.start();
	}
}
