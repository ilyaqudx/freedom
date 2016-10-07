package freedom.nio;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

	public static void main(String[] args)
	{
		try 
		{
			Acceptor acceptor = new NioAcceptor(new InetSocketAddress(3333),new NioHandler());
			acceptor.start();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
