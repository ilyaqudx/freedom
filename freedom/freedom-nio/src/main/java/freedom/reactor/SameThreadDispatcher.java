package freedom.reactor;

import java.nio.channels.SelectionKey;

public class SameThreadDispatcher implements Dispatcher {

	@Override
	public void stop() 
	{
		
	}

	@Override
	public void onChannelReadEvent(AbstractNioChannel channel,
			Object readObject, SelectionKey key) 
	{
		/*
	     * Calls the associated handler to notify the read event where application specific code
	     * resides.
	     */
	    channel.getHandler().handleChannelRead(channel, readObject, key);
	}

}
