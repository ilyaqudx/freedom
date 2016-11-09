package freedom.nio.codec;

import java.nio.ByteBuffer;

import freedom.nio.IoSession;
import freedom.nio.filter.ProtocolCodecFilter.ProtocolDecoderOutput;

public interface Decoder {

	public boolean decode(IoSession session,ByteBuffer buffer,ProtocolDecoderOutput output);
}
