package freedom.jdfs.storage.dio;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import freedom.jdfs.storage.StorageClientInfo;
import freedom.jdfs.storage.StorageDioTask;
import freedom.jdfs.storage.StorageFileContext;
import freedom.jdfs.storage.StorageTask;


public class StorageDioWriteTask extends StorageDioTask{

	public StorageDioWriteTask(String name, BlockingQueue<StorageTask> queue)
	{
		super(name, queue);
	}

	@Override
	public void process(StorageTask storageTask) {
		try 
		{
			final StorageClientInfo clientInfo = storageTask.clientInfo;
			//获得文件上下文信息
			final StorageFileContext fileContext = clientInfo.fileContext;
			//打开文件
			dio_open_file(fileContext);
			//写入数据到磁盘
			storageTask.data.position(fileContext.buffOffset);
			int expect = storageTask.data.remaining();
			//这儿如果一次没有将数据写完是会有问题的。
			int writeBytes = fileContext.file.getChannel().write(storageTask.data.buf());
			if(writeBytes != expect){
				//error
				storageTask.callback.exception(storageTask,
						new StorageDioException(String.format("write bytes error!expect %d,actual %d", expect,writeBytes)));
			}else{
				storageTask.callback.complete(storageTask);
			}
			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

}
