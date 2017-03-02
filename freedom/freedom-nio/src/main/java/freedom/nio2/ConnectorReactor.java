package freedom.nio2;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ConnectorReactor extends NioReactor {

	public ConnectorReactor(AbstractNioService service)
	{
		super("connector-reactor",service);
	}

	@Override
	public void openSocket() throws IOException 
	{
		SocketChannel socket = SocketChannel.open();
		socket.configureBlocking(false);
		socket.connect(service.getBindAddress());
		socket.register(sel, SelectionKey.OP_CONNECT);
	}

	@Override
	protected void interest(SelectionKey key) throws IOException
	{
		SocketChannel socket = (SocketChannel) key.channel();
		boolean connected = socket.finishConnect();
		if(!connected)throw new IOException("socket connect fail!");
		
		NioSession session 	   = service.buildNewSession(socket);
		NioProcessor processor = service.getProcessorPool().getProcessor(session);
		processor.regiestSession(session);
		session.setProcessor(processor);
		service.getHandler().onCreated(session);
		super.exitThread();
	}
}
