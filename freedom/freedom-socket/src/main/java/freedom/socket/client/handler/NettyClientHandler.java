
package freedom.socket.client.handler;

import freedom.socket.server.message.Request;
import freedom.socket.server.message.Response;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
@Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception 
	{
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception 
	{
		//System.out.println("client received msg : " + msg);
		if(msg instanceof Response)
		{
			Thread.sleep(5);
			Response response = (Response) msg;
			Request request = new Request();
			request.setCmd((short) 3001);
			request.setMid(response.getMid()+1);
			request.setOneWay((byte) 0);
			request.setBody("i want to enter to server world!");
			request.setUid(10001);
			
			ctx.writeAndFlush(request);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception 
	{
		super.exceptionCaught(ctx, cause);
	}

	
}
