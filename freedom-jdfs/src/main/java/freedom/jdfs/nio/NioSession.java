package freedom.jdfs.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import freedom.jdfs.storage.StorageTask;

public class NioSession {

	private SocketChannel channel;
	public final long id;
	public final String name;
	private NioProcessor processor;
	protected static final AtomicLong unionIdCreater = new AtomicLong(0);
	private Map<String,Object> attrs = new HashMap<String,Object>();
	static final String FRAGMENT = "fragment";
	private SelectionKey key;
	public ByteBuffer buffer = ByteBuffer.allocate(256 * 1024);
	public StorageTask task;
	private ByteBuffer headerFragment = ByteBuffer.allocate(9);
	public NioSession(SocketChannel channel)
	{
		this.channel = channel;
		this.id      = unionIdCreater.incrementAndGet();
		this.name    = "NioSession-" + id;
		this.task    = new StorageTask();
	}

	public void setProcessor(NioProcessor processor) {
		this.processor = processor;
	}
	public NioProcessor getProcessor()
	{
		return processor;
	}
	
	public void registerRead(Selector sel)
	{
		if(channel.isConnected())
		{
			try
			{
				key = channel.register(sel, SelectionKey.OP_READ,this);
				//set task stage is recv data
				this.task.clientInfo.stage = StorageTask.FDFS_STORAGE_STAGE_NIO_RECV;
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public SocketChannel getChannel()
	{
		return this.channel;
	}
	
	public void storeFragment(ByteBuffer fragment)
	{
		if(fragment.hasRemaining())
		{
			ByteBuffer buffer = ByteBuffer.allocate(fragment.remaining());
			byte[] remaining  = new byte[fragment.remaining()];
			System.arraycopy(fragment.array(), fragment.position(), remaining, 0, fragment.remaining());
			buffer.put(remaining);
			attrs.put(FRAGMENT, ByteBuffer.wrap(remaining));
		}
	}
	
	public boolean hasFragment()
	{
		return null != attrs.get(FRAGMENT);
	}
	
	public ByteBuffer pollFragment()
	{
		return (ByteBuffer) attrs.get(FRAGMENT);
	}
	
	/**
	 * 获取头部片断长度
	 * */
	public int headerFragmentLength()
	{
		//直接用Buffer的position作为头部版本的长度,是因为写入过后,position会更新位置,下次再写入的时候就直接写入而不会覆盖掉之前的数据
		return headerFragment.position();
	}

	/**
	 * 暂存头部片断,
	 * */
	public void storeHeaderFragment(int len) 
	{
		headerFragment.put(buffer);
	}
	

	/**
	 * 合并头部片断
	 * */
	public void mergeHeader2Buffer()
	{
		if(buffer.capacity() - buffer.position() >= headerFragment.position())
		{
			//现有BUFFER可以装入所有的头数据,直接合并
			byte[] array = buffer.array();
			System.arraycopy(array, 0, array, headerFragmentLength(), buffer.position());
			System.arraycopy(headerFragment.array(), 0, array, 0, headerFragmentLength());
		}else{
			//TODO
			
		}
	}
	
	//注册/取消读事件
	public void setIntestedRead(boolean intested)
	{
        if ((key == null) || !key.isValid()) {
            return;
        }

        int newInterestOps = key.interestOps();
        if(intested)
        	newInterestOps |= SelectionKey.OP_READ;
        else
        	newInterestOps &= ~SelectionKey.OP_READ;
        key.interestOps(newInterestOps);
	}
}
