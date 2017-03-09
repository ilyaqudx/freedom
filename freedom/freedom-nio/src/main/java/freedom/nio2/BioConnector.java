package freedom.nio2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

public class BioConnector extends AbstractNioService {

	public BioConnector(InetSocketAddress bindAddress,NioHandler handler,ProtocolCodec codec)
	{
		this(bindAddress, handler, codec, null);
	}
	
	public BioConnector(InetSocketAddress bindAddress,NioHandler handler,ProtocolCodec codec,NioChannelConfig channelConfig)
	{
		super(bindAddress, handler, codec, channelConfig,1,ConnectorReactor.class,new ConnectorListener());
	}
	
	public static void main(String[] args) throws IOException {
		
		SocketChannel channel = SocketChannel.open();
		
		channel.configureBlocking(true);
		
		boolean connected = channel.connect(new InetSocketAddress(6666));
		
		Socket socket = channel.socket();
		
		OutputStream out = socket.getOutputStream();
		
		out.write("get name\r\n".getBytes());
		out.flush();
		
		
		InputStream in = socket.getInputStream();
		byte[] data = new byte[1024];
		int len = in.read(data);
		
		System.out.println("received server data : " + new String(data,0,len));
	}
}
