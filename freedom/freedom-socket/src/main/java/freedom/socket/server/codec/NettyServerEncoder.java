
package freedom.socket.server.codec;

import com.alibaba.fastjson.JSON;

import freedom.socket.server.message.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyServerEncoder extends MessageToByteEncoder<Object> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
			throws Exception 
	{
		if(msg instanceof Response)
		{
			byte[] buffer = JSON.toJSONString(msg).getBytes();
			out.writeInt(buffer.length);
			out.writeBytes(buffer);
			System.out.println("返回数据 : " + JSON.toJSONString(msg));
		}
	}

}
