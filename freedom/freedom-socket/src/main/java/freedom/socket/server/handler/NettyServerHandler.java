
package freedom.socket.server.handler;

import freedom.common.constants.Consts;
import freedom.socket.command.Command;
import freedom.socket.command.CommandMessage;
import freedom.socket.server.SocketServer;
import freedom.socket.server.message.Request;
import freedom.socket.server.message.Response;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
//复用
@Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

	private SocketServer server;
	
	public NettyServerHandler(SocketServer server)
	{
		this.server = server;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception 
	{
		super.channelActive(ctx);
		InetSocketAddress remoteAddress = (InetSocketAddress)ctx.channel().remoteAddress();
		System.out.println(remoteAddress.getHostName() + " active : " + remoteAddress.getPort());
	}
	
	

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelRegistered(ctx);
		InetSocketAddress remoteAddress = (InetSocketAddress)ctx.channel().remoteAddress();
		System.out.println(remoteAddress.getHostName() + " registered :" + remoteAddress.getPort());
	}



	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception 
	{
		if(msg instanceof Request)
		{
			Request req = (Request) msg;
			Command<?> command = server.findCommand(String.valueOf(req.getCmd()));
			CommandMessage<?,?> message = command.executeCommand(req,ctx);
			Response response = new Response();
			if(message.hasError())
			{
				response.setMid(req.getMid());
				response.setCode((short) message.getEx().getCode());
				response.setMsg(message.getEx().getMsg());
			}
			else
			{
				response.setMid(req.getMid());
				response.setOneWay((byte) 0);
				response.setBody(message.getOut());
				response.setCode(Consts.SUCCESS_SHORT);
				response.setCmd(message.getResCmd());
			}
			ctx.writeAndFlush(response);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception 
	{
		super.exceptionCaught(ctx, cause);
	}

	
}
