package freedom.nio;

import java.nio.channels.SelectionKey;

public enum SocketEvent {

	ACCEPT(SelectionKey.OP_ACCEPT),
	CONNECT(SelectionKey.OP_CONNECT),
	READ(SelectionKey.OP_READ),
	WRITE(SelectionKey.OP_WRITE);
	
	public int code;
	SocketEvent(int code)
	{
		this.code = code;
	}
}
