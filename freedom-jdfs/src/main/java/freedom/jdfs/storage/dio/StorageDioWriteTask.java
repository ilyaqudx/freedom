package freedom.jdfs.storage.dio;

import java.io.File;
import java.util.concurrent.BlockingQueue;

import freedom.jdfs.storage.StorageFileContext;
import freedom.jdfs.storage.StorageTask;

public class StorageDioWriteTask extends Thread{

	private BlockingQueue<StorageTask> queue;
	
	public  StorageDioWriteTask(String name,BlockingQueue<StorageTask> queue) 
	{
		super(name);
		this.queue = queue;
	}
	
	@Override
	public void run() 
	{
		while(true){
			try
			{
				StorageTask storageTask = queue.take();
				
				final StorageFileContext fileContext = storageTask.fileContext;
				//获取文件
				dio_open_file(fileContext);
			}
			catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private int dio_open_file(StorageFileContext fileContext){

		int result;

		if (fileContext.file != null)
		{
			return 0;
		}
		//如何确定文件的路径,或者说在哪儿确定文件保存的位置
		File file = new File(pathname)
		fileContext->fd = open(fileContext->filename, 
					fileContext->open_flags, 0644);
		if (fileContext->fd < 0)
		{
			result = errno != 0 ? errno : EACCES;
			logError("file: "__FILE__", line: %d, " \
				"open file: %s fail, " \
				"errno: %d, error info: %s", \
				__LINE__, fileContext->filename, \
				result, STRERROR(result));
		}
		else
		{
			result = 0;
		}

		pthread_mutex_lock(&g_dio_thread_lock);
		g_storage_stat.total_file_open_count++;
		if (result == 0)
		{
			g_storage_stat.success_file_open_count++;
		}
		pthread_mutex_unlock(&g_dio_thread_lock);

		if (result != 0)
		{
			return result;
		}

		if (fileContext->offset > 0 && lseek(fileContext->fd, \
			fileContext->offset, SEEK_SET) < 0)
		{
			result = errno != 0 ? errno : EIO;
			logError("file: "__FILE__", line: %d, " \
				"lseek file: %s fail, " \
				"errno: %d, error info: %s", \
				__LINE__, fileContext->filename, \
				result, STRERROR(result));
			return result;
		}

		return 0;
	
	}
	
}
