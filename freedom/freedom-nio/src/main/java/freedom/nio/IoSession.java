package freedom.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;

import freedom.nio.future.WriteFuture;
import freedom.nio.processor.IoProcessor;

public interface IoSession {

	public long getId();
	
	public WriteFuture write(Object message)throws IOException;
	
	public void setProcessor(IoProcessor processor);
	
	public IoProcessor getProcessor();
	
	public IoHandler getHandler();
	
	public FilterChain getFilterChain();
	
	public void regiest(Selector sel) throws ClosedChannelException;

	public Object getAttr(String attr);

	public void setAttr(String attr, Object value);
	
	public SocketChannel getChannel();
	
	public SelectionKey getSelectionKey();
	
	public boolean hasFragment();

	public int read(ByteBuffer buffer) throws IOException;

	public void close() throws IOException;

	public void storeFragment(ByteBuffer buffer);

	public void clearFragment();
	
	public Queue<WriteRequest> getWriteRequestQueue();
}
