package freedom.reactor.app;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.List;

import freedom.reactor.AbstractNioChannel;
import freedom.reactor.ChannelHandler;
import freedom.reactor.Dispatcher;
import freedom.reactor.LoggingHandler;
import freedom.reactor.NioDatagramChannel;
import freedom.reactor.NioReactor;
import freedom.reactor.NioServerSocketChannel;
import freedom.reactor.ThreadPoolDispatcher;

public class App {

	private NioReactor reactor;
	private Dispatcher dispatcher;
	 private List<AbstractNioChannel> channels = new ArrayList<AbstractNioChannel>();
	public App(Dispatcher dispatcher) 
	{
		this.dispatcher = dispatcher;
	}
	
	public void start() throws ClosedChannelException, IOException
	{
		/*
	     * The application can customize its event dispatching mechanism.
	     */
		reactor = new NioReactor(dispatcher);

	    /*
	     * This represents application specific business logic that dispatcher will call on appropriate
	     * events. These events are read events in our example.
	     */
	    LoggingHandler loggingHandler = new LoggingHandler();

	    /*
	     * Our application binds to multiple channels and uses same logging handler to handle incoming
	     * log requests.
	     */
	    reactor.registerChannel(tcpChannel(6666, loggingHandler))
	        .registerChannel(tcpChannel(6667, loggingHandler))
	        .registerChannel(udpChannel(6668, loggingHandler)).start();
	}
	
	public void stop()
	{
		
	}
	
	private AbstractNioChannel tcpChannel(int port, ChannelHandler handler) throws IOException {
	    NioServerSocketChannel channel = new NioServerSocketChannel(port, handler);
	    channel.bind();
	    channels.add(channel);
	    return channel;
	  }

	  private AbstractNioChannel udpChannel(int port, ChannelHandler handler) throws IOException {
	    NioDatagramChannel channel = new NioDatagramChannel(port, handler);
	    channel.bind();
	    channels.add(channel);
	    return channel;
	  }
	
	public static void main(String[] args) throws IOException 
	{
		Dispatcher dispatcher = new ThreadPoolDispatcher(16);
		App app = new App(dispatcher);
		app.start();
	}
	
	
	
}
