package freedom.cache.net;

import static freedom.cache.Const.CR_LF;
import static freedom.cache.Const.SPACE;

import java.nio.ByteBuffer;
import java.util.Arrays;

import freedom.nio2.AbstractProtocolCodec;
import freedom.nio2.NioSession;
/**
 * 文本行编解码器
 * */
public class TextLineCodec extends AbstractProtocolCodec {

	
	
	@Override
	public Object decodePacket(NioSession session, ByteBuffer buffer)
	{
		if(buffer.hasRemaining())
		{
			int remaining= buffer.remaining();
			byte[] array = buffer.array();
			int start = 0,offset = 0;
			while(offset < remaining && array[offset] == SPACE)
			{
				offset ++;
			}
			boolean findCR = false;
			boolean findLF = false;
			while(offset < remaining){
				byte v = array[offset++];
				if(v == CR_LF[0])
					findCR = true;
				else if(findCR && v == CR_LF[1])
				{
					findLF = true;
					break;
				}
				else
					findCR = false;
			}
			
			if(findCR && findLF){
				//完整的命令
				buffer.position(offset);
				return Arrays.copyOfRange(array, start, offset - 2);
			}
		}
		return null;
	}

	@Override
	public ByteBuffer encode(NioSession session, Object msg)
	{
		if(msg != null && msg instanceof String)
		{
			byte[] arr = ((String)msg).getBytes();
			return ByteBuffer.wrap(arr);
		}
		return null;
	}
}
