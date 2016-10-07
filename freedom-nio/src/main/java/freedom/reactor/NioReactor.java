package freedom.reactor;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NioReactor {

	private Selector selector;
	private Dispatcher dispatcher;

	private Queue<Runnable> pendingCommands = new ConcurrentLinkedQueue<Runnable>();
	private ExecutorService reactorMain = Executors.newSingleThreadExecutor();

	public NioReactor(Dispatcher dispatcher) throws IOException {
		this.dispatcher = dispatcher;
		this.selector = Selector.open();
	}

	public void start() {
		reactorMain.execute(new Runnable() {
			@Override
			public void run() {
				try 
				{
					eventLoop();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void stop() throws InterruptedException, IOException {
		reactorMain.shutdownNow();
		selector.wakeup();
		reactorMain.awaitTermination(4, TimeUnit.SECONDS);
		selector.close();
	}

	public NioReactor registerChannel(AbstractNioChannel channel)
			throws ClosedChannelException {
		SelectionKey key = channel.getJavaChannel().register(selector,
				channel.getInterestedOps());
		key.attach(channel);
		channel.setReactor(this);
		return this;
	}

	public void changeOps(SelectionKey key,int interestedOps) {
		pendingCommands.add(new ChangeKeyOpsCommand(key, interestedOps));
		selector.wakeup();
	}

	private void eventLoop() throws IOException {
		while (true) {

			// honor interrupt request
			if (Thread.interrupted()) {
				break;
			}

			// honor any pending commands first
			processPendingCommands();

			/*
			 * Synchronous event de-multiplexing happens here, this is blocking
			 * call which returns when it is possible to initiate non-blocking
			 * operation on any of the registered channels.
			 */
			selector.select();

			/*
			 * Represents the events that have occurred on registered handles.
			 */
			Set<SelectionKey> keys = selector.selectedKeys();

			Iterator<SelectionKey> iterator = keys.iterator();

			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				if (!key.isValid()) {
					iterator.remove();
					continue;
				}
				processKey(key);
			}
			keys.clear();
		}
	}

	private void processPendingCommands() {
		Iterator<Runnable> iterator = pendingCommands.iterator();
		while (iterator.hasNext()) {
			Runnable command = iterator.next();
			command.run();
			iterator.remove();
		}
	}

	/*
	 * Initiation dispatcher logic, it checks the type of event and notifier
	 * application specific event handler to handle the event.
	 */
	private void processKey(SelectionKey key) throws IOException {
		if (key.isAcceptable()) {
			onChannelAcceptable(key);
		} else if (key.isReadable()) {
			onChannelReadable(key);
		} else if (key.isWritable()) {
			onChannelWritable(key);
		}
	}

	private static void onChannelWritable(SelectionKey key) throws IOException {
		AbstractNioChannel channel = (AbstractNioChannel) key.attachment();
		channel.flush(key);
	}

	private void onChannelReadable(SelectionKey key) 
	{
		try {
			// reads the incoming data in context of reactor main loop. Can this
			// be improved?
			Object readObject = ((AbstractNioChannel) key.attachment()).read(key);

			dispatchReadEvent(key, readObject);
		} catch (IOException e) {
			try {
				key.channel().close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void dispatchReadEvent(SelectionKey key, Object readObject) 
	{
		dispatcher.onChannelReadEvent((AbstractNioChannel) key.attachment(),readObject, key);
	}

	private void onChannelAcceptable(SelectionKey key) throws IOException {
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		SelectionKey readKey = socketChannel.register(selector,SelectionKey.OP_READ);
		readKey.attach(key.attachment());
	}

	/**
	 * A command that changes the interested operations of the key provided.
	 */
	class ChangeKeyOpsCommand implements Runnable {
		private SelectionKey key;
		private int interestedOps;

		public ChangeKeyOpsCommand(SelectionKey key, int interestedOps) {
			this.key = key;
			this.interestedOps = interestedOps;
		}

		public void run() {
			key.interestOps(interestedOps);
		}

		@Override
		public String toString() 
		{
			return "Change of ops to: " + interestedOps;
		}
	}
}
