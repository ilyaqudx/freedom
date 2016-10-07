package freedom.reactor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NioServerSocketChannel extends AbstractNioChannel {

	private int port;
	
	public NioServerSocketChannel(int port,ChannelHandler handler) throws IOException 
	{
		super(handler, ServerSocketChannel.open());
		this.port = port;
	}

	@Override
	public void bind() throws UnknownHostException, IOException 
	{
		((ServerSocketChannel) getJavaChannel()).socket().bind(
		        new InetSocketAddress(InetAddress.getLocalHost(), port));
		    ((ServerSocketChannel) getJavaChannel()).configureBlocking(false);
		    System.out.println("Bound TCP socket at port: " + port);
	}

	@Override
	public Object read(SelectionKey key) throws IOException 
	{
		SocketChannel socketChannel = (SocketChannel) key.channel();
	    ByteBuffer buffer = ByteBuffer.allocate(1024);
	    int read = socketChannel.read(buffer);
	    buffer.flip();
	    if (read == -1) {
	      throw new IOException("Socket closed");
	    }
	    return buffer;
	}

	@Override
	public int getInterestedOps()
	{
		return SelectionKey.OP_ACCEPT;
	}

	@Override
	public SelectableChannel getJavaChannel()
	{
		return (ServerSocketChannel)super.getJavaChannel();
	}

	@Override
	public void doWrite(Object pendingWrite, SelectionKey key)
			throws IOException 
	{
		ByteBuffer pendingBuffer = (ByteBuffer) pendingWrite;
	    ((SocketChannel) key.channel()).write(pendingBuffer);
		
	}


}
