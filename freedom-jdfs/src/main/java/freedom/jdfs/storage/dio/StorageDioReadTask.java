package freedom.jdfs.storage.dio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.concurrent.BlockingQueue;

import freedom.jdfs.LogKit;
import freedom.jdfs.protocol.ProtoCommon;
import freedom.jdfs.storage.Globle;
import freedom.jdfs.storage.StorageClientInfo;
import freedom.jdfs.storage.StorageFileContext;
import freedom.jdfs.storage.StorageTask;

public class StorageDioReadTask extends Thread {

	private BlockingQueue<StorageTask> queue;

	public StorageDioReadTask(String name, BlockingQueue<StorageTask> queue) {
		super(name);
		this.queue = queue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				StorageTask storageTask = queue.take();
				
				StorageClientInfo  clientInfo  = storageTask.clientInfo;
				StorageFileContext fileContext = clientInfo.fileContext;
				dio_open_file(fileContext);
				//start read data
				FileChannel channel = fileContext.file.getChannel();
				storageTask.data.position(fileContext.buffOffset);
				storageTask.data.limit(storageTask.length);
				int expect = storageTask.length - fileContext.buffOffset;
				int len = channel.read(storageTask.data);
				if(len != expect){
					LogKit.error(String.format("read bytes error,expect %d,actual %d",expect,len), StorageDioReadTask.class);
				}else{
					storageTask.data.position(storageTask.offset);
					fileContext.offset += len;
					clientInfo.stage = StorageTask.FDFS_STORAGE_STAGE_NIO_SEND;
					if(fileContext.offset >= fileContext.end){
						//此时应该释放到文件描述符
						channel.close();
						fileContext.file.close();
						fileContext.file = null;
						//read success
						storage_nio_notify(storageTask);
					}else{
						storage_nio_notify(storageTask);
					}
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 通知NIO线程已完成本次写盘任务
	 * 
	 * 
	 * 将该任务添加到NIO线程的完成任务队列中
	 * */
	private void storage_nio_notify(StorageTask storageTask)
	{
		storageTask.session.getProcessor().complete(storageTask);
	}

	private Object lock = new Object();
	
	/**
	 * 打开文件
	 * */
	private int dio_open_file(StorageFileContext fileContext) throws IOException{

		if (fileContext.file != null)
		{
			//写入偏移量
			fileContext.file.seek(fileContext.offset);
			return 0;
		}
		//如何确定文件的路径,或者说在哪儿确定文件保存的位置.在生成文件名时就已经确定了存储的路径 storage_path
		
		//参考方法 : storage_get_store_path
		RandomAccessFile randomFile = new RandomAccessFile(fileContext.fileName, "rw");
		//设置文件的大小
		randomFile.setLength(fileContext.end);
		//File file = new File(new String(fileContext.filename));

		//Notice 读和写都锁嘛?
		synchronized (lock) {
			Globle.g_storage_stat.total_file_open_count++;
			Globle.g_storage_stat.success_file_open_count++;
		}


		fileContext.file = randomFile;
		if(fileContext.offset > 0)
		{
			try
			{
				randomFile.seek(fileContext.offset);
			}
			catch (Exception e)
			{
				LogKit.error(String.format("seek file %s fail", fileContext.fileName), StorageDioWriteTask.class);
				return ProtoCommon.FAIL;
			}
		}
		return ProtoCommon.SUCCESS;
	}
}
