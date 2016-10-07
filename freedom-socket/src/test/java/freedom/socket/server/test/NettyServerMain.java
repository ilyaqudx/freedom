
package freedom.socket.server.test;

import java.net.InetSocketAddress;

import freedom.socket.command.Command;
import freedom.socket.command.CommandMessage;
import freedom.socket.server.NettySocketServer;

public class NettyServerMain {

	public static void main(String[] args) {
		try {
			new NettySocketServer((new InetSocketAddress(9955))) {
				
				@Override
				public Command<CommandMessage<?, ?>> findCommand(String command) {
					// TODO Auto-generated method stub
					return null;
				}
			};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
