package freedom.nio2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class NioSession {

	private static final AtomicLong sessionId = new AtomicLong(1);
	
	private long          		id;
	private Selector 			sel;
	private NioProcessor 		processor;
	private SocketChannel 		channel;
	private String   			remoteAddress;
	private AbstractNioService	service;
	private ByteBuffer 			recv ;
	private ByteBuffer 			send ;
	private ByteBuffer     		fragment;
	private Queue<Object>   	recvPacketQueue = new ConcurrentLinkedQueue<Object>();
	private Queue<WriteRequest> writeRequestQueue = new ConcurrentLinkedQueue<WriteRequest>();
	private WriteRequest		currentWriteRequest;

	public long					createdTime   = System.currentTimeMillis();
	public long					lastWriteTime = createdTime;
	public long					lastReadTime  = createdTime;
	public long					lastIdleTimeForWrite = createdTime;
	public long					lastIdleTimeForRead  = createdTime;
	public long					lastIdleTimeForBoth  = createdTime;
	public long					idleCountForWrite    = 0;
	public long					idleCountForRead     = 0;
	public long					idleCountForBoth     = 0;
	public long					lastWriteBytes;
	public long					lastReadBytes;
	public long					writenAllBytes;
	public long					readAllBytes;
	public long					writeAllPackets;
	public long					writenAllTime = 1L;
	public volatile boolean suspend;
	
	public NioSession(AbstractNioService service,SocketChannel channel)
	{
		this.id       		= sessionId.getAndIncrement();
		this.service  		= service;
		this.channel  		= channel;
		this.remoteAddress	= SessionContext.remoteHost(channel);
		this.recv     		= ByteBuffer.allocate(8192);
		this.send     		= ByteBuffer.allocate(8192);
	}
	
	public void regiestRead(Selector sel) throws ClosedChannelException
	{
		this.sel 	    = sel;
		this.channel.register(sel, SelectionKey.OP_READ, this);
	}
	
	public void read()
	{
		try 
		{
			int n = channel.read(recv);
			if(n == -1)
				throw new IOException("socket is closed!");
			if(n > 0){
				service.getProtocolCodec().decode(this, recv);
			}
			recv.clear();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			close();
			service.getHandler().onClosed(this);
		}
	}
	
	public void flush()
	{
		if(suspend)return;
		
		
		
		WriteRequest writeRequest = currentWriteRequest == null ? writeRequestQueue.poll() : currentWriteRequest;
		
		if(writeRequest == null)return;
		
		this.currentWriteRequest = writeRequest;
		//TODO encode不由IO线程处理
		ByteBuffer buffer = service.getProtocolCodec().encode(this, writeRequest.getMsg());
		int writtenBytes = 0;
		long costTime = 0;
		if(buffer != null && buffer.hasRemaining() && isConnected())
		{
			do {
				try 
				{
					long start = System.currentTimeMillis();
					writtenBytes += channel.write(buffer);
					long end   = System.currentTimeMillis();
					costTime += (end - start);
					if(writtenBytes == 0)
						break;
					else if(!buffer.hasRemaining()){
						this.lastWriteTime  = System.currentTimeMillis();
						this.lastWriteBytes = writtenBytes;
						this.writenAllBytes += writtenBytes;
						this.writenAllTime  += costTime;
						this.writeAllPackets ++;
						this.currentWriteRequest.getFuture().setValue(writtenBytes);
						this.service.getHandler().onWritten(this);
						this.currentWriteRequest = null;
						writeRequest.getFuture().getFutureListener().complete(writtenBytes);
						System.out.println("发送成功【"+processor.getId()+"】【writtenBytes : " +writtenBytes+ "】【writenAllBytes : "+writenAllBytes+"】【writeAllPackets : " +writeAllPackets+"】【qps : " +(writenAllBytes * 1000 / writenAllTime)+ " bytes】");
						if((writenAllBytes * 1000 / writenAllTime) > 1024*1024*2){
							this.service.getHandler().onWriteSuspend(this, writenAllBytes * 1000 / writenAllTime);
						}
					}
				} 
				catch(IOException e){
					this.close();
				}
				catch (Exception e) {
					e.printStackTrace();
					writeRequest.getFuture().getFutureListener().exception(e);
					this.close();
				}
			} while (buffer.hasRemaining());
		}
	}
	
	public boolean isConnected()
	{
		return channel.isConnected();
	}
	
	public WriteFuture write(Object msg)
	{
		return write(msg, new DefaultFutureListener());
	}
	public WriteFuture write(Object msg,FutureListener listener)
	{
		if(msg == null)
			throw new NullPointerException("msg is null");
		if(suspend){
			System.err.println("session 流量过大.现不能发送消息");
			throw new RuntimeException("session 流量过大.现不能发送消息");
		}
		WriteFuture writeFuture   = new WriteFuture(listener);
		WriteRequest writeRequest = new WriteRequest(msg, writeFuture);
		writeRequestQueue.add(writeRequest);
		processor.addFlushSession(this);
		if(!processor.wakeup)
			sel.wakeup();
		return writeFuture;
	}
	
	public Queue<WriteRequest> getSendPacketQueue() {
		return writeRequestQueue;
	}

	public void addRecvPacket(Object packet)
	{
		//recvPacketQueue.add(packet);
		service.getHandler().onReceived(this, packet);
	}
	
	public void storeFragment(ByteBuffer buffer)
	{
		System.out.println(buffer);
		ByteBuffer fragment = ByteBuffer.allocate(buffer.remaining());
		fragment.put(buffer.array(),buffer.position(),buffer.remaining());
		this.fragment = fragment;
	}
	
	public ByteBuffer mergeFragment(ByteBuffer buffer)
	{
		ByteBuffer newBuffer = ByteBuffer.allocate(fragment.capacity() + buffer.limit());
		newBuffer.put(fragment.array(), 0, fragment.capacity());
		newBuffer.put(buffer.array(), 0, buffer.limit());
		newBuffer.flip();
		this.fragment = null;
		return newBuffer;
	}

	public void close() 
	{
		if(null != processor){
			processor.removeSession(this);
		}
		
		//如果需要关闭SESSION,就要关闭SESSION所有的资源.这就是为什么需要DISPOSE的原因
		if(null != writeRequestQueue)
			writeRequestQueue.clear();
		if(null != currentWriteRequest)
			this.currentWriteRequest = null;
		
		if(channel != null){
			try {
				channel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public NioService getService(){
		return service;
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() 
	{ 
		return String.format("NioSession-" + id +" [RemoteAddress:%s]", remoteAddress);
	}

	public boolean hasFragment()
	{
		return fragment != null;
	}

	public void setProcessor(NioProcessor processor) {
		this.sel        = processor.getSelector();
		this.processor  = processor;
	}
	
	public void notifyIdle(long now)
	{
		if(isConnected())
		{
			notifyIdle0(now,IdleState.BOTH_IDLE ,Math.max(lastIdleTimeForBoth,Math.max(lastReadTime, lastWriteTime)),10);
			notifyIdle0(now,IdleState.READ_IDLE ,Math.max(lastReadTime, lastIdleTimeForRead) ,10);
			notifyIdle0(now,IdleState.WRITE_IDLE,Math.max(lastWriteTime,lastIdleTimeForWrite), 10);
			
			//TODO notifyWriteTimeout();
		}
	}
	
	private void notifyIdle0(long now, IdleState state, long lastIOTime,int timeout)
	{
		if(((now - lastIOTime) / 1000) >= timeout){
			increaseIdleCount(state,now);
			service.getHandler().onIdle(this, state);
		}
	}

	public void increaseIdleCount(IdleState state, long now)
	{
		if(state == IdleState.BOTH_IDLE){
			idleCountForBoth++;
			lastIdleTimeForBoth = now;
		}
		else if(state == IdleState.READ_IDLE){
			idleCountForRead++;
			lastIdleTimeForRead = now;
		}
		else if(state == IdleState.WRITE_IDLE)
		{
			idleCountForWrite++;
			lastIdleTimeForWrite= now;
		}
	}
	
}
