
package freedom.socket.client;

import freedom.socket.client.codec.NettyClientDecoder;
import freedom.socket.client.codec.NettyClientEncoder;
import freedom.socket.client.handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class NettySocketClient {

	private InetSocketAddress remoteAddress;
	
	public NettySocketClient(InetSocketAddress remoteAddress)
	{
		this.remoteAddress = remoteAddress;
	}
	
	public InetSocketAddress getRemoteAddress()
	{
		return this.remoteAddress;
	}
	
	public Channel connect()
	{
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap client = new Bootstrap();
		client.group(group)
		.channel(NioSocketChannel.class)
		.option(ChannelOption.TCP_NODELAY, true)
		.handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception
			{
				ch.pipeline()
				.addLast("decoder", new NettyClientDecoder())
				.addLast("encoder",new NettyClientEncoder())
				.addLast("handler", new NettyClientHandler());
			}
		});
		
		try {
			ChannelFuture future = client.connect(remoteAddress).sync();
			future.await(15000);
			if(future.isSuccess())
			{
				System.out.println("connected server : " + remoteAddress.getHostName());
				return future.channel();
			}
			else
				System.out.println("connect failed!");
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
}
