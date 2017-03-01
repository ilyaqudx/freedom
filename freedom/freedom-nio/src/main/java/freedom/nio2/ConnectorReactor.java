package freedom.nio2;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ConnectorReactor extends NioReactor {

	public ConnectorReactor()
	{
		super("connector-reactor");
	}

	@Override
	public void open() throws IOException 
	{
		SocketChannel socket = SocketChannel.open();
		socket.configureBlocking(false);
		socket.connect(service.getBindAddress());
		socket.register(sel, SelectionKey.OP_CONNECT);
	}

	@Override
	protected void interest(SelectionKey key)
	{
		if(key.isConnectable())
		{
			SocketChannel socket = (SocketChannel) key.channel();
			try {
				boolean connected = socket.finishConnect();
				if(!connected)
					throw new IOException("socket connect fail!");
				NioSession session = buildNewSession(socket);
				NioProcessor processor = service.getProcessorPool().getProcessor(session);
				processor.regiestSession(session);
				session.setProcessor(processor);
				service.getHandler().onCreated(session);
			} 
			catch (Exception e)
			{
				e.printStackTrace();
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		else if(key.isReadable())
		{
			
		}

	}

}
