package freedom.socket.client.codec;

import java.util.List;

import com.alibaba.fastjson.JSON;

import freedom.socket.server.message.Response;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class NettyClientDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception 
	{
		in.markReaderIndex();
		if(in.readableBytes() >= 4)
		{
			int packageLen = in.readInt();
			if(in.readableBytes() >= packageLen)
			{
				byte[] buffer = new byte[packageLen];
				in.readBytes(buffer);
				Response respone = JSON.parseObject(new String(buffer), Response.class);
				out.add(respone);
				return;
			}
		}
		in.resetReaderIndex();
	}

}
