package freedom.bio.codec;

import freedom.bio.core.ByteArrayUtils;
import freedom.bio.core.Dispatcher;
import freedom.bio.core.IOBuffer;
import freedom.bio.core.PacketHead;
import freedom.bio.core.PacketReq;
import freedom.bio.core.PacketRes;
import freedom.bio.core.Utils;

public class Codec {

	public Codec()
	{
	}
	
	public boolean decode(IOBuffer buffer)
	{
		try 
		{
			if(buffer.remaining() > 8)
			{
				PacketHead head    = ByteArrayUtils.parse(buffer, PacketHead.class);
				if(buffer.remaining() >= head.getPacketSize())
				{
					byte[]     data    = buffer.readCppBytes(head.getPacketSize());
					Dispatcher.dipatch(new PacketReq(head, data));
					return true;
				}else
					buffer.reset();
			}else
				buffer.reset();
		} 
		catch (InstantiationException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public void encode(PacketRes res)
	{
		byte[] data = res.getData();
		res.getHead().setPacketSize((short)(data.length + 8));
		byte[] head = Utils.getBytes(res.getHead());
	}
}
