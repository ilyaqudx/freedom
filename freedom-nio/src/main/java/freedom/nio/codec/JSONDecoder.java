package freedom.nio.codec;

import java.nio.ByteBuffer;

import freedom.nio.IoSession;
import freedom.nio.codec.ProtocolCodecFilter.ProtocolDecoderOutput;

public class JSONDecoder implements Decoder {

	@Override
	public boolean decode(IoSession session, ByteBuffer buffer,ProtocolDecoderOutput output) 
	{
		if(buffer.remaining() < 5)
			return false;
		buffer.mark();
		int len = buffer.getInt();
		if(len > buffer.remaining())
		{
			buffer.reset();
			return false;
		}
		
		byte[] data = new byte[len];
		buffer.get(data);
		String message = new String(data);
		output.write(message);
		
		return true;
	}
}
