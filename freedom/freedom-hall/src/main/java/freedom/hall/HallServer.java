package freedom.hall;

import java.net.InetSocketAddress;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import freedom.socket.command.Command;
import freedom.socket.command.CommandMessage;
import freedom.socket.server.NettySocketServer;

public class HallServer extends NettySocketServer {
	public static ApplicationContext application = null;

	public static void main(String[] args) throws Exception {
		application = new ClassPathXmlApplicationContext("application.xml");
		new HallServer(new InetSocketAddress("127.0.0.1", 9900)).start();
	}

	public HallServer(InetSocketAddress bindAddress) {
		super(bindAddress);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Command<CommandMessage<?, ?>> findCommand(String command) {
		return (Command<CommandMessage<?, ?>>) application.getBean(command);
	}

}
