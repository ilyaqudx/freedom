package freedom.jdfs.storage.dio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import freedom.jdfs.LogKit;
import freedom.jdfs.storage.StorageConfig;
import freedom.jdfs.storage.StorageTask;

/**
 * 磁盘服务
 * */
public class StorageDioService {

	
	/**
	 * 线程模型
	 * 1-读
	 * 2-写
	 * 
	 * 通过队列方法获取读写的任务,队列为并发阻塞队列
	 * 	读写应该都有一个任务队列分别进行处理
	 * 		writeQueue
	 * 		readQueue
	 * */
	private StorageConfig storageConfig;
	public StorageDioService(StorageConfig storageConfig)
	{
		this.storageConfig = storageConfig;
	}
	/**
	 * 写任务队列
	 * */
	private BlockingQueue<StorageTask> writeQueue;
	private BlockingQueue<StorageTask> readQueue;
	
	private StorageDioWriteTask writeTask;
	private StorageDioReadTask readTask;
	
	private void init()
	{
		//初始化任务队列
		writeQueue = new ArrayBlockingQueue<StorageTask>(storageConfig.getMax_connections());
		readQueue  = new ArrayBlockingQueue<>(storageConfig.getMax_connections());
		
		//创建线程去处理读写操作
		readTask = new StorageDioReadTask("storage-dio-read-thread",readQueue);
		writeTask = new StorageDioWriteTask("storage-dio-write-thread",writeQueue);
		readTask.start();
		writeTask.start();
	}
	
	public void addWriteTask(StorageTask task)
	{
		if(task == null){
			LogKit.error("add write task is null", this.getClass());
		}else{
			try {
				writeQueue.put(task);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addReadTask(StorageTask task)
	{
		if(task == null){
			LogKit.error("add read task is null", this.getClass());
		}else{
			try {
				readQueue.put(task);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
