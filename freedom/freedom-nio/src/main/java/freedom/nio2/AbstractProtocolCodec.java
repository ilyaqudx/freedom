package freedom.nio2;

import java.nio.ByteBuffer;

public abstract class AbstractProtocolCodec implements ProtocolCodec {

	@Override
	public Object decode(NioSession session, ByteBuffer buffer)
	{
		buffer.flip();
		if(buffer.hasRemaining())
		{
			if(session.hasFragment())
				buffer = session.mergeFragment(buffer);
			int 	start  = buffer.position();
			Object 	packet = null;
			while(buffer.hasRemaining() && (packet = decodePacket(session,buffer)) != null)
			{
				if(start >= buffer.position())
					break;
				start = buffer.position();
				session.addRecvPacket(packet);
			}
			
			if(buffer.hasRemaining())
				session.storeFragment(buffer);
		}
		return null;
	}
	
	public abstract Object decodePacket(NioSession session,ByteBuffer buffer);
}
