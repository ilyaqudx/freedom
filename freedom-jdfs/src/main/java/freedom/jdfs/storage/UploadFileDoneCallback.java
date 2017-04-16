package freedom.jdfs.storage;

import java.util.zip.CRC32;

import freedom.jdfs.LogKit;
import freedom.jdfs.storage.dio.StorageDioWriteTask;

/**
 * 文件写入成功回调
 * */
public class UploadFileDoneCallback implements StorageTaskCallback {

	@Override
	public void complete(StorageTask storageTask)
	{
		final StorageClientInfo clientInfo   = storageTask.clientInfo;
		final StorageFileContext fileContext = clientInfo.fileContext;
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
		if (fileContext.offset < fileContext.end)
		{
			storageTask.offset = 0;
			storageTask.length = 0;
			storageTask.data.clear();
			storageTask.clientInfo.fileContext.buffOffset = 0;
			storageTask.clientInfo.stage = StorageTask.FDFS_STORAGE_STAGE_NIO_RECV;
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
	}

	@Override
	public void exception(StorageTask storageTask,Exception ex)
	{
		
	}

}
