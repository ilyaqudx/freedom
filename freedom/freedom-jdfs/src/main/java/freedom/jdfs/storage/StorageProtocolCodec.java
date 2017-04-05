package freedom.jdfs.storage;

import java.nio.ByteBuffer;

import freedom.nio2.NioSession;
import freedom.nio2.ProtocolCodec;

public class StorageProtocolCodec implements ProtocolCodec {

	@Override
	public Object decode(NioSession session, ByteBuffer buffer) 
	{
		return null;
	}

	@Override
	public ByteBuffer encode(NioSession session, Object msg)
	{
		return null;
	}

}
