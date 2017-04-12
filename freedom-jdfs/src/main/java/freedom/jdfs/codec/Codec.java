package freedom.jdfs.codec;

import java.io.IOException;
import java.nio.ByteBuffer;

import freedom.jdfs.nio.NioSession;
import freedom.jdfs.protocol.TrackerHeader;

public class Codec {

	public static boolean decode(NioSession session,ByteBuffer buffer)throws IOException
	{
		if(buffer.remaining() >= 10)
		{
			buffer.mark();
			long pkgLen = buffer.getLong();
			byte cmd    = buffer.get();
			byte errno  = buffer.get();
			if(buffer.remaining() >= pkgLen)
			{
				if(pkgLen <= Integer.MAX_VALUE)
				{
					byte[] body = new byte[(int) pkgLen];
					buffer.get(body);
					
					TrackerHeader packet = new TrackerHeader(errno, body);
					return true;
				}
			}else
				buffer.reset();
		}
		return false;
	}
	
	public static void encode(Object obj)throws IOException
	{
		
	}
}
