package freedom.nio;

import java.nio.channels.SocketChannel;

public interface SocketListener {

	public void onConnected(SocketChannel channel);
	
	public void onDisconnected(SocketChannel channel);
	
	public void onReceived(SocketChannel channel);
	
	public void onSent(SocketChannel channel);
}
