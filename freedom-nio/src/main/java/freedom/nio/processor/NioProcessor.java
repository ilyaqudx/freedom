package freedom.nio.processor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

import freedom.nio.Channel;
import freedom.nio.IoSession;

public class NioProcessor implements IoProcessor,Runnable {

	private static final long TIME_OUT = 1000L;

	private volatile boolean running = false;
	
	/**
	 * 新加入的连接
	 * */
	private Queue<IoSession> newSessions = new ConcurrentLinkedQueue<IoSession>();
	
	
	private ExecutorService executors;
	private Selector selector;
	public NioProcessor(ExecutorService executors)
	{
		this.executors = executors;
		try 
		{
			this.selector  = Selector.open();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public IoSession getSession(Channel channel) 
	{
		return null;
	}

	@Override
	public void registSession(IoSession session)
	{
		newSessions.offer(session);
		if(!running)
		{
			this.running = true;
			startupProcessor();
		}
	}
	
	private void startupProcessor()
	{
		executors.execute(this);
	}
	
	@Override
	public void run()
	{
		while (running)
		{
			try 
			{
				int sel = selector.select(TIME_OUT);
				
				if(sel > 0)
				{
					process();
				}
				
				sel += handleNewSession();
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void process()
	{
		 for (Iterator<IoSession> i = selectedSessions(); i.hasNext();) 
		 {
			 	IoSession session = i.next();
			 	process(session);
	            i.remove();
	     }
	}

	private Iterator<IoSession> selectedSessions()
	{
		return new IoSessionIterator(selector.selectedKeys());
	}

	private void process(IoSession session)
	{
		if(isReadable(session))
			read(session);
		else if(isWriteable(session))
			write(session);
	}
	
	private void write(IoSession session)
	{
		
		
	}

	/**
	 * 正式读取数据
	 * */
	private void read(IoSession session)
	{
		
		ByteBuffer buffer = ByteBuffer.allocate(16384);
		int ret = 0;
		int readBytes = 0;
		try {
			//尽可能先把channel缓冲区中的数据取出来,让tcp可以接收更多的数据
			while ((ret = session.read(buffer)) > 0) 
			{
				readBytes += ret;
				if (!buffer.hasRemaining())
					break;
			}
			
			if(readBytes > 0)
			{
				buffer.flip();//设置为读模式
				//过滤
				session.getFilterChain().fireRead(buffer);
			}
			
			if(ret < 0){
				session.close();
			}
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			//此处需要处理closed session
			try {
				session.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}

	private boolean isWriteable(IoSession session) {
		// TODO Auto-generated method stub
		return session.getSelectionKey().isWritable();
	}

	private boolean isReadable(IoSession session)
	{
		return session.getSelectionKey().isReadable();
	}

	/**
	 * 处理新接受的链接(注册读写事件)
	 * */
	private int handleNewSession()
	{
		int count = 0;
		while(!newSessions.isEmpty())
		{
			try 
			{
				IoSession session = newSessions.poll();
				session.regiest(selector);
				count++;
			} 
			catch(ClosedChannelException e) 
			{
				e.printStackTrace();
			}
		}
		return count;
	}

	public class IoSessionIterator implements Iterator<IoSession>
	{
		private Iterator<SelectionKey> keys;
		
		public IoSessionIterator(Set<SelectionKey> keys)
		{
			this.keys = keys.iterator();
		}

		@Override
		public boolean hasNext() 
		{
			return keys.hasNext();
		}

		@Override
		public IoSession next()
		{
			return (IoSession) keys.next().attachment();
		}

		@Override
		public void remove()
		{
			keys.remove();
		}
		
	}
	
}
