
package freedom.socket.server;

import java.net.InetSocketAddress;

import freedom.socket.command.Command;
import freedom.socket.command.CommandMessage;

public interface SocketServer {

	public void start()throws Exception;
	
	public void close()throws Exception;
	
	public Command<CommandMessage<?,?>> findCommand(String command);
	
	public InetSocketAddress getBindAddress();
}
