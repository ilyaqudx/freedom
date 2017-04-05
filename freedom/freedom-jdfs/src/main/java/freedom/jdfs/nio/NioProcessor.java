package freedom.jdfs.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.apache.log4j.Logger;

import freedom.jdfs.storage.StorageTask;

public class NioProcessor {

	Logger LOGGER = Logger.getLogger(NioProcessor.class); 
	public final int id;
	public final String name;
	private Selector sel;
	private LinkedList<NioSession> newSessions = new LinkedList<NioSession>();

	public NioProcessor(int id)
	{
		this.id = id;
		this.name = "NioProcessor-" + id;
		try 
		{
			this.sel = Selector.open();
			startup();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	private void startup()
	{
		new Thread(new Processor()).start();
	}

	public void addNewSession(NioSession session) 
	{
		session.registerRead(sel);
		newSessions.add(session);
	}

	final class Processor implements Runnable
	{
		@Override
		public void run()
		{
			while (true)
			{
				try 
				{
					sel.select(1000);
					process(sel.selectedKeys());
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}

		private void process(Set<SelectionKey> selectedKeys)
		{
			for (Iterator<SelectionKey> it = selectedKeys.iterator();it.hasNext();) 
			{
				SelectionKey key = it.next();
				if (key.isReadable()){
					read(key);
					//此处读事件,不能按照常规直接就删除
				}
				else if(key.isWritable())
					write(key);
				it.remove();
			}
		}

		private void write(SelectionKey key) 
		{
			
		}

		private void read(SelectionKey key)
		{
			NioSession session = ((NioSession) key.attachment());
			if ((session.task.stage & StorageTask.FDFS_STORAGE_STAGE_DIO_THREAD) > 0)
			{
				session.task.stage &= ~StorageTask.FDFS_STORAGE_STAGE_DIO_THREAD;
			}
			switch(session.task.stage)
			{
				case StorageTask.FDFS_STORAGE_STAGE_NIO_INIT:
					init(session.task);
					break;
				case StorageTask.FDFS_STORAGE_STAGE_NIO_RECV:
					break;
			}
		}
		
		private void init(StorageTask task)
		{
			//注册读事件
		}

		private ByteBuffer mergeBuffer(ByteBuffer fragment, ByteBuffer newBuffer)
		{
			newBuffer.flip();
			int capacity = fragment.limit() + newBuffer.limit();
			ByteBuffer mergeBuffer = ByteBuffer.allocate(capacity);
			mergeBuffer.put(fragment);
			mergeBuffer.put(newBuffer);
			return mergeBuffer;
		}

	}
}
