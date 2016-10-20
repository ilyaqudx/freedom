package freedom.reactor;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractNioChannel {

	private NioReactor reactor;
	private SelectableChannel channel;
	private ChannelHandler handler;
	private final Map<SelectableChannel, Queue<Object>> channelToPendingWrites = new ConcurrentHashMap<SelectableChannel, Queue<Object>>();

	public AbstractNioChannel(ChannelHandler handler, SelectableChannel channel) {
		this.channel = channel;
		this.handler = handler;
	}

	public void setReactor(NioReactor reactor) {
		this.reactor = reactor;
	}

	public SelectableChannel getJavaChannel() {
		return channel;
	}

	public abstract void bind() throws UnknownHostException, IOException;

	public abstract Object read(SelectionKey key) throws IOException;

	public abstract int getInterestedOps();

	public ChannelHandler getHandler() {
		return handler;
	}

	public void flush(SelectionKey key) throws IOException{
		Queue<Object> pendingWrites = channelToPendingWrites.get(key.channel());
		while (true) {
			Object pendingWrite = pendingWrites.poll();
			if (pendingWrite == null) {
				// We don't have anything more to write so channel is interested
				// in reading more data
				reactor.changeOps(key, SelectionKey.OP_READ);
				break;
			}

			// ask the concrete channel to make sense of data and write it to
			// java channel
			doWrite(pendingWrite, key);
		}
	}

	public abstract void doWrite(Object pendingWrite , SelectionKey key)throws IOException;

	public void write(Object data , SelectionKey key) {
		Queue<Object> pendingWrites = this.channelToPendingWrites.get(key
				.channel());
		if (pendingWrites == null) {
			synchronized (this.channelToPendingWrites) {
				pendingWrites = this.channelToPendingWrites.get(key.channel());
				if (pendingWrites == null) {
					pendingWrites = new ConcurrentLinkedQueue<Object>();
					this.channelToPendingWrites.put(key.channel(),
							pendingWrites);
				}
			}
		}
		pendingWrites.add(data);
		reactor.changeOps(key, SelectionKey.OP_WRITE);
	}

}
