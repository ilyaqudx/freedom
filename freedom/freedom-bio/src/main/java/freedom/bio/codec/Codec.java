package freedom.bio.codec;

import freedom.bio.core.IOBuffer;

public interface Codec {

	public boolean decode(IOBuffer buffer);
	
	public byte[] encode(Object msg);
}
