package freedom.jdfs.storage.dio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.BlockingQueue;
import java.util.zip.CRC32;

import freedom.jdfs.LogKit;
import freedom.jdfs.protocol.ProtoCommon;
import freedom.jdfs.storage.Globle;
import freedom.jdfs.storage.StorageClientInfo;
import freedom.jdfs.storage.StorageFileContext;
import freedom.jdfs.storage.StorageService;
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
					final StorageFileContext fileContext = clientInfo.fileContext;
					//打开文件
					dio_open_file(fileContext);
					//写入数据到磁盘
					storageTask.data.position(fileContext.buffOffset);
					int writeBytes = fileContext.file.getChannel().write(storageTask.data);
					//当次offset,length重置
					//计算CRC32值
					if (fileContext.calcCrc32)
					{
						CRC32 crc = new CRC32();
						crc.update(storageTask.data.array(), fileContext.buffOffset,storageTask.length - fileContext.buffOffset);
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
					fileContext.offset += (storageTask.length - fileContext.buffOffset);
					//LogKit.info(String.format("[Channel %d - filename : %s]成功写入数据  :%d,累计 offset : %d,总长度 : %d", storageTask.session.id
						//	,new String(fileContext.filename),(storageTask.length - fileContext.buff_offset),fileContext.offset,fileContext.end), StorageDioWriteTask.class);
					if (fileContext.offset < fileContext.end)
					{
						/*storageTask.offset = 0;
						storageTask.length = 0;
						storageTask.buffer.clear();移动动nio去处理
						fileContext.buff_offset = 0;*/
						storage_nio_notify(storageTask);  //notify nio to deal
					}
					else
					{
						//整个文件已经全部写入
						if (fileContext.calcCrc32)
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
						clientInfo.cleanFunc.callback(storageTask);
						
						LogKit.info(String.format("[Channel %d success write file : %s]",storageTask.session.id,new String(clientInfo.fileContext.fileName)), StorageDioWriteTask.class);
						if (fileContext.doneCallback != null)
						{
							//fileContext.done_callback.callback(storageTask);
							StorageService.storage_upload_file_done_callback(storageTask, 0);
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
	
	public static void main(String[] args) throws IOException {
		
		RandomAccessFile file = new RandomAccessFile("h:/fastdfs/storage/data/00/00/AAAAAE54q+2AbubrAAIRhB+J9jY=3ez.sql", "rw");
		
		file.write("helle file!".getBytes());
		
		
	}
}
