package freedom.jdfs.storage;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.BlockingQueue;

public abstract class StorageDioTask extends Thread
{
	protected Object lock = new Object();
	
	protected BlockingQueue<StorageTask> queue;
	
	public StorageDioTask(String name,BlockingQueue<StorageTask> queue)
	{
		super(name);
		this.queue = queue;
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				StorageTask storageTask = queue.take();
				process(storageTask);
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}

	public abstract void process(StorageTask storageTask);
	
	/**
	 * 打开文件
	 * */
	@SuppressWarnings("resource")
	protected void dio_open_file(StorageFileContext fileContext) throws IOException
	{
		if (fileContext.file != null)
		{
			//写入偏移量
			fileContext.file.seek(fileContext.offset);
		}
		else
		{
			RandomAccessFile randomFile = new RandomAccessFile(fileContext.fileName, "rw");
			//设置文件的大小
			randomFile.setLength(fileContext.end);
			//Notice 读和写都锁嘛?
			synchronized (lock) {
				Globle.g_storage_stat.total_file_open_count++;
				Globle.g_storage_stat.success_file_open_count++;
			}
			
			fileContext.file = randomFile;
			if(fileContext.offset > 0)
			{
				randomFile.seek(fileContext.offset);
			}
		}
	}
	
	/**
	 * 通知NIO线程已完成本次读/写盘任务
	 * 
	 * 
	 * 将该任务添加到NIO线程的完成任务队列中
	 * */
	protected void storage_nio_notify(StorageTask storageTask)
	{
		storageTask.session.getProcessor().complete(storageTask);
	}
}
