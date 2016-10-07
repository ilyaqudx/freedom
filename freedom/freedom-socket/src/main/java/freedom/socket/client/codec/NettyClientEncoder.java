
package freedom.socket.client.codec;

import freedom.socket.server.message.Request;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.alibaba.fastjson.JSON;

public class NettyClientEncoder extends MessageToByteEncoder<Object> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
			throws Exception 
	{
		if(msg instanceof Request)
		{
			byte[] buffer = JSON.toJSONString(msg).getBytes();
			out.writeInt(buffer.length);
			out.writeBytes(buffer);
		}
	}

}
