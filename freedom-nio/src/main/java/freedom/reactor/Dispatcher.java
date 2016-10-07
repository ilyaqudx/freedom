package freedom.reactor;

import java.nio.channels.SelectionKey;

public interface Dispatcher {

	void onChannelReadEvent(AbstractNioChannel channel, Object readObject, SelectionKey key);
	
	void stop()throws InterruptedException;
}
