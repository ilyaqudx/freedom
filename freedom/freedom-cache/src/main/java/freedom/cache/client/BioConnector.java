package freedom.cache.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

import freedom.nio2.ProtocolCodec;

public class BioConnector {

	private InetSocketAddress remoteAddress;
	private BioHandler handler;
	private ProtocolCodec codec;
	public BioConnector(String hostname,int port,BioHandler handler,ProtocolCodec codec)
	{
		this(new InetSocketAddress(hostname, port),handler,codec);
	}
	public BioConnector(InetSocketAddress remoteAddress,BioHandler handler,ProtocolCodec codec)
	{
		this.handler = handler;
		this.codec   = codec;
		this.remoteAddress = remoteAddress;
	}
	
	public Connection connect() throws IOException
	{
		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(true);
		channel.connect(remoteAddress);
		if(channel.isConnected()){
			System.out.println("client successed connected!");
			Connection connection = buildConnection(channel.socket());
			this.handler.onCreated(connection);
			return connection;
		}else
			throw new SocketException("connect fail!");
	}

	private Connection buildConnection(Socket socket) throws IOException 
	{
		return new Connection(socket,handler,codec);
	}
}
