package freedom.jdfs.storage.dio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.BlockingQueue;
import java.util.zip.CRC32;

import freedom.jdfs.storage.Globle;
import freedom.jdfs.storage.StorageClientInfo;
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
				//从队列中取出任务
				StorageTask storageTask = queue.take();
				try {
					StorageClientInfo clientInfo = storageTask.clientInfo;
					//获得文件上下文信息
					final StorageFileContext fileContext = clientInfo.file_context;
					//打开文件
					dio_open_file(fileContext);
					//写入数据到磁盘
					fileContext.file.write(storageTask.buffer.array(), fileContext.buff_offset, 
							storageTask.length - fileContext.buff_offset);
					//本次数据全部写入后,记录整个OFFSET的位置
					//storageTask.clientInfo.total_offset += storageTask.length;
					//当次offset,length重置
					//计算CRC32值
					if (fileContext.calc_crc32)
					{
						CRC32 crc = new CRC32();
						crc.update(storageTask.buffer.array(), fileContext.buff_offset, 
								storageTask.length - fileContext.buff_offset);
						fileContext.crc32 = (int) crc.getValue();
					}
					synchronized (lock) {
						Globle.g_storage_stat.total_file_write_count++;
						Globle.g_storage_stat.success_file_write_count++;
					}
					
					
					//TODO Continue
					/*if (fileContext.calc_file_hash)
					{
						if (g_file_signature_method == STORAGE_FILE_SIGNATURE_METHOD_HASH)
						{
							CALC_HASH_CODES4(pDataBuff, write_bytes, \
									fileContext.file_hash_codes)
						}
						else
						{
							my_md5_update(&fileContext.md5_context, \
								(unsigned char *)pDataBuff, write_bytes);
						}
					}*/
					//记录整个文件的写入偏移量
					fileContext.offset += storageTask.length - fileContext.buff_offset;
					if (fileContext.offset < fileContext.end)
					{
						storageTask.offset = 0;
						storageTask.length = 0;
						storageTask.buffer.clear();
						fileContext.buff_offset = 0;
						storage_nio_notify(storageTask);  //notify nio to deal
					}
					else
					{
						//整个文件已经全部写入
						if (fileContext.calc_crc32)
						{
							//计算最终的crc32值
							fileContext.crc32 = Globle.CRC32_FINAL(fileContext.crc32);
						}

						/*TODO Continue
						 * if (fileContext.calc_file_hash)
						{
							if (g_file_signature_method == STORAGE_FILE_SIGNATURE_METHOD_HASH)
							{
								FINISH_HASH_CODES4(fileContext.file_hash_codes)
							}
							else
							{
								my_md5_final((unsigned char *)(fileContext. \
								file_hash_codes), &fileContext.md5_context);
							}
						}*/

						/*if (fileContext.extra_info.upload.before_close_callback != NULL)
						{
							result = fileContext.extra_info.upload. \
									before_close_callback(pTask);
						}*/

						/* file write done, close it */
						fileContext.file.close();
						fileContext.file = null;

						/*
						 * TODO Continue
						 * if (fileContext.done_callback != NULL)
						{
							fileContext.done_callback(pTask, result);
						}*/
						clientInfo.clean_func.callback(storageTask);
						
						if (fileContext.done_callback != null)
						{
							//fileContext.done_callback(storageTask, null);
							fileContext.done_callback.callback(storageTask);
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			catch (InterruptedException e) 
			{
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
		String fileName = new String(fileContext.filename);
		System.out.println("write file name : " + fileName);
		RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
		//设置文件的大小
		randomFile.setLength(fileContext.end);
		//File file = new File(new String(fileContext.filename));

		//Notice 读和写都锁嘛?
		synchronized (lock) {
			
			Globle.g_storage_stat.total_file_open_count++;
			Globle.g_storage_stat.success_file_open_count++;
		}


		fileContext.file = randomFile;
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
	
	public static void main(String[] args) throws IOException {
		
		RandomAccessFile file = new RandomAccessFile("h:/fastdfs/storage/data/00/00/AAAAAE54q+2AbubrAAIRhB+J9jY=3ez.sql", "rw");
		
		file.write("helle file!".getBytes());
		
		
	}
}
