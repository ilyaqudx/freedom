package freedom.reactor;

import java.nio.channels.SelectionKey;

public interface ChannelHandler {

	void handleChannelRead(AbstractNioChannel channel,Object msg,SelectionKey key);
}
