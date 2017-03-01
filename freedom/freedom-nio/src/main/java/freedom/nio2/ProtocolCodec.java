package freedom.nio2;

import java.nio.ByteBuffer;

public interface ProtocolCodec {

	public Object 		decode(NioSession session,ByteBuffer buffer);
	
	public ByteBuffer   encode(NioSession session,Object msg);
}
