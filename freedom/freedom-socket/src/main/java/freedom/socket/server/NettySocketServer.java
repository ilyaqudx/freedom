
package freedom.socket.server;

import freedom.socket.server.codec.NettyServerDecoder;
import freedom.socket.server.codec.NettyServerEncoder;
import freedom.socket.server.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public abstract class NettySocketServer extends AbstractSocketServer {

	public NettySocketServer(InetSocketAddress bindAddress) 
	{
		super(bindAddress);
	}

	@SuppressWarnings("rawtypes")
	public void start() throws Exception 
	{
		EventLoopGroup parentGroup = new NioEventLoopGroup(16);
		EventLoopGroup childGroup = new NioEventLoopGroup(16);
		ServerBootstrap server = new ServerBootstrap();
		server.group(parentGroup, childGroup)
		.channel(NioServerSocketChannel.class)
		.option(ChannelOption.TCP_NODELAY, true)
		.option(ChannelOption.SO_SNDBUF, 8096)
		.option(ChannelOption.SO_RCVBUF, 8096)
		.childHandler(new ChannelInitializer() {

			protected void initChannel(Channel ch) throws Exception 
			{
				ch.pipeline()
				.addLast("decoder", new NettyServerDecoder())
				.addLast("encoder", new NettyServerEncoder())
				.addLast("handler", new NettyServerHandler(NettySocketServer.this));
			}
		});
		
		ChannelFuture future = server.bind(bindAddress).sync();
		future.await(15000);
		if(!future.isSuccess())
		{
			System.out.println("server start failed,exit");
			System.exit(-1);
		}else
			System.out.println("server is started! listening port : " + bindAddress.getPort());
	}	

}
