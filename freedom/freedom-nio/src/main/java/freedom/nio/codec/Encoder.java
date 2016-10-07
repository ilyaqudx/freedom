package freedom.nio.codec;

import java.nio.channels.SocketChannel;

public interface Encoder {

	public void encode(SocketChannel channel,Object msg);
}
