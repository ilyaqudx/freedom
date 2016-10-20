package freedom.reactor;

import java.nio.channels.SelectionKey;

public class LoggingHandler implements ChannelHandler {

	@Override
	public void handleChannelRead(AbstractNioChannel channel, Object msg,
			SelectionKey key) 
	{
		System.out.println("loggin handler  : " + msg);
	}

}
