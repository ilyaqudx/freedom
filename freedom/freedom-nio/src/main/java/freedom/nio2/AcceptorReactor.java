package freedom.nio2;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class AcceptorReactor extends NioReactor {

	public AcceptorReactor(AbstractNioService service)
	{
		super("acceptor-reactor",service);
	}

	@Override
	public void openSocket() throws IOException 
	{
		ServerSocketChannel serverSocket = ServerSocketChannel.open();
		serverSocket.configureBlocking(false);
		serverSocket.bind(service.getBindAddress());
		serverSocket.register(sel, SelectionKey.OP_ACCEPT);
		service.getServiceListener().fireStartup();
	}

	@Override
	protected void interest(SelectionKey key) throws IOException 
	{
		ServerSocketChannel serverSocket  = (ServerSocketChannel)key.channel();
		SocketChannel       socketChannel = serverSocket.accept();
		NioSession 			session       = service.buildNewSession(socketChannel);
		socketChannel.configureBlocking(false);
		NioProcessor processor = service.getProcessorPool().getProcessor(session);
		processor.regiestSession(session);
		session.setProcessor(processor);
		//在调用onCreated之前一定要将processor设置进行,否则SESSION当中的PROCESSOR为null
		service.getHandler().onCreated(session);
	}

}
