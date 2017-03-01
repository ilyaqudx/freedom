package freedom.nio2;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class TextLineProcotolCodec extends AbstractProtocolCodec {

	public static final byte[] CTRL = new byte[]{13,10};
	
	@Override
	public ByteBuffer encode(NioSession session, Object msg) 
	{
		if(msg instanceof String){
			if("".equals(msg))
				return ByteBuffer.wrap(CTRL);
			byte[] array = ((String)msg).getBytes();
			return ByteBuffer.wrap(array);
		}
		return null;
	}

	@Override
	public Object decodePacket(NioSession session, ByteBuffer buffer)
	{
		byte[]   data    = Arrays.copyOfRange(buffer.array(),  buffer.position(), buffer.limit());
		int      offset  = 0;
		int 	 end     = data.length;
		boolean  preEnd  = false;
		boolean  success = false;
		while(offset < end)
		{
			byte v = data[offset++];
			if(v == CTRL[0])
				preEnd = true;
			else if(v == CTRL[1] && preEnd){
				success = true;
				break;
			}else
				preEnd = false;
		}
		
		if(success){
			buffer.position(offset);
			return new String(data,0,offset - 2);
		}
		
		return null;
	}
}
