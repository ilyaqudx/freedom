
package freedom.socket.server.codec;

import freedom.socket.server.message.Request;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class NettyServerDecoder extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,List<Object> out) throws Exception 
	{
		in.markReaderIndex();
		int remaining = in.readableBytes();
		if(remaining >= 4)
		{
			int packageLen = in.readInt();
			if(in.readableBytes() >= packageLen)
			{
				byte[] body = new byte[packageLen];
				in.readBytes(body);
				Request request = JSON.parseObject(new String(body), Request.class);
				out.add(request);
			}
			else
				in.resetReaderIndex();
		}
		else
			in.resetReaderIndex();
	}

}
