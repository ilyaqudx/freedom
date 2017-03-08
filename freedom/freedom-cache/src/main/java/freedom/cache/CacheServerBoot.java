package freedom.cache;

import java.net.InetSocketAddress;

import freedom.cache.net.NioHandlerImpl;
import freedom.cache.net.TextLineCodec;
import freedom.nio2.NioAcceptor;

public class CacheServerBoot {

	public static void main(String[] args) 
	{
		NioAcceptor netAcceptor = new NioAcceptor(new InetSocketAddress(6666), new NioHandlerImpl(),new TextLineCodec());
		//绑定网络
		netAcceptor.start();
	}
}
