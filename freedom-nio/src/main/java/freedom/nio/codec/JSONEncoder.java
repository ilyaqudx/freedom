package freedom.nio.codec;

import java.nio.ByteBuffer;

import com.alibaba.fastjson.JSON;

import freedom.common.kit.Utils;
import freedom.nio.IoSession;
import freedom.nio.codec.ProtocolCodecFilter.ProtocolEncoderOutput;

public class JSONEncoder implements Encoder {

	@Override
	public void encode(IoSession session, Object msg,ProtocolEncoderOutput output)
	{
		Utils.assertNull(msg, "msg is null");
		
		byte[] byteArray  = JSON.toJSONString(msg).getBytes();
		ByteBuffer buffer = ByteBuffer.allocate(4 + byteArray.length);
		buffer.putInt(byteArray.length);
		buffer.put(byteArray);
		buffer.flip();
		output.write(buffer);
	}

}
