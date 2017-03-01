package freedom.nio2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class SessionContext {

	
	public static final void closeSession(NioSession session)
	{
		if(null != session){
			session.close();
		}
	}

	public static NioSession attachment(SelectionKey key) 
	{
		if(key == null)
			return null;
		return (NioSession) key.attachment();
	}
	
	public static final String remoteHost(SocketChannel channel)
	{
		InetSocketAddress remoteAddress;
		try {
			remoteAddress = (InetSocketAddress) channel.getRemoteAddress();
			return remoteAddress.getHostString() + " " + remoteAddress.getPort();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "unknown address!";
	}
}
