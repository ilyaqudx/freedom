package freedom.bio.codec;

import freedom.bio.core.ByteArrayUtils;
import freedom.bio.core.Dispatcher;
import freedom.bio.core.IOBuffer;
import freedom.bio.core.IoSession;
import freedom.bio.core.PacketHead;
import freedom.bio.core.PacketReq;
import freedom.bio.core.PacketRes;
import freedom.bio.core.Utils;

public class Codec {

	private IoSession session;
	
	public Codec(IoSession session)
	{
		this.session = session;
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
					Dispatcher.dipatch(session,new PacketReq(head, data));
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
	
	public byte[] encode(PacketRes res)
	{
		byte[] data = ByteArrayUtils.getBytes(res.getData());
		//res.getHead().setPacketSize((short)(data.length + 8));
		
		byte[] head = ByteArrayUtils.getBytes(Utils.buildPackageHead(data.length));
		byte[] buffer = new byte[head.length + data.length];
		System.arraycopy(head, 0, buffer, 0, head.length);
		System.arraycopy(data, 0, buffer, head.length, data.length);
		return buffer;
	}
}
