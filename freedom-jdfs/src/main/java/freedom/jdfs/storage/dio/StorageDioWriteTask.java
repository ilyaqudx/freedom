package freedom.jdfs.storage.dio;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.concurrent.BlockingQueue;

import freedom.jdfs.storage.Globle;
import freedom.jdfs.storage.StorageFileContext;
import freedom.jdfs.storage.StorageTask;

public class StorageDioWriteTask extends Thread{

	private Object lock = new Object();
	
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

		int result = 0;

		if (fileContext.file != null)
		{
			return 0;
		}
		//如何确定文件的路径,或者说在哪儿确定文件保存的位置.在生成文件名时就已经确定了存储的路径 storage_path
		
		//参考方法 : storage_get_store_path
		File file = new File(new String(fileContext.filename));

		//Notice 读和写都锁嘛?
		synchronized (lock) {
			
			Globle.g_storage_stat.total_file_open_count++;
			if (result == 0)
			{
				Globle.g_storage_stat.success_file_open_count++;
			}
		}

		if (result != 0)
		{
			return result;
		}

		RandomAccessFile randomFile = new RandomAccessFile(file, "rw");
		if(fileContext.offset > 0){
			randomFile.seek(fileContext.offset);
		}
		/*if (fileContext.offset > 0 && lseek(fileContext.fd,
			fileContext.offset, SEEK_SET) < 0)
		{
			result = errno != 0 ? errno : EIO;
			logError("file: "__FILE__", line: %d, " \
				"lseek file: %s fail, " \
				"errno: %d, error info: %s", \
				__LINE__, fileContext.filename, \
				result, STRERROR(result));
			return result;
		}*/

		return 0;
	
	}
	
}
