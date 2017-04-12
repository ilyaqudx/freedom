package freedom.jdfs.command;

import static freedom.jdfs.protocol.ProtoCommon.CRC32_XINIT;

import java.util.Arrays;

import freedom.jdfs.LogKit;
import freedom.jdfs.nio.NioSession;
import freedom.jdfs.protocol.ProtoCommon;
import freedom.jdfs.storage.DisconnectCleanFunc;
import freedom.jdfs.storage.FileDealDoneCallback;
import freedom.jdfs.storage.Globle;
import freedom.jdfs.storage.StorageClientInfo;
import freedom.jdfs.storage.StorageFileContext;
import freedom.jdfs.storage.StorageServer;
import freedom.jdfs.storage.StorageService;
import freedom.jdfs.storage.StorageService.FileEntity;
import freedom.jdfs.storage.StorageTask;
import freedom.jdfs.storage.StorageTaskPool;
import freedom.jdfs.storage.TaskDealFunc;

public class UploadFileCommand implements Command {

	private static final int STORAGE_STATUE_DEAL_FILE = 123456;


	@Override
	public int execute(NioSession session, StorageTask storageTask) 
	{
		StorageClientInfo clientInfo = storageTask.clientInfo;
		StorageFileContext fileContext = clientInfo.fileContext;
		DisconnectCleanFunc clean_func;
		String fileExtName ;//= new byte[7];
		long packetLen;//包体长度
		long fileOffset;
		long fileBytes;
		int crc32;
		int storePathIndex = 0;
		int result;
		//包体长度
		packetLen = clientInfo.totalLength - ProtoCommon.HEADER_LENGTH;

		if (packetLen < 1 + ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE + 
				ProtoCommon.FDFS_FILE_EXT_NAME_MAX_LEN)
		{
			LogKit.error(String.format("client ip: %s, package size %ld is not correct,expect length >= %d", 
					storageTask.clientIp,packetLen,
					1 + ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE + 
					ProtoCommon.FDFS_FILE_EXT_NAME_MAX_LEN),this.getClass());
			return ProtoCommon.EINVAL;
		}

		storageTask.data.position(10);
		//头部后第一个字节:存储路径索引
		try {
			//这儿居然storageTask.length = 0,整个BUFFER只接收了10个字节.
			//查看了size也为0.那么应该是SIZE也为0导致 了.因为在前面赋值时,如果整个请求大于了SIZE的大小则用了SIZE的大小赋值给了LENGTH
			//居然session是空的
			storePathIndex = storageTask.data.get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (storePathIndex == -1)
		{
			if ((result = StorageService.storage_get_storage_path_index(storePathIndex)) != 0)
			{
				LogKit.error(String.format("get_storage_path_index fail,errno: %d",result),this.getClass());
				return result;
			}
		}
		else if (storePathIndex < 0 || storePathIndex >= Globle.g_fdfs_store_paths.count)
		{
			LogKit.error(String.format("store_path_index: %d is invalid",storePathIndex),this.getClass());
			return ProtoCommon.EINVAL;
		}

		//真实文件的长度
		fileBytes = storageTask.data.getLong();
		if (fileBytes < 0 || fileBytes != packetLen - (1 + ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE + 
						ProtoCommon.FDFS_FILE_EXT_NAME_MAX_LEN))
		{
			LogKit.error(String.format("pkg length is not correct,invalid file bytes: %ld, total body length: %ld",fileBytes, packetLen),this.getClass());
			return ProtoCommon.EINVAL;
		}
		byte[] fileExtNameBuffer = new byte[6];
		storageTask.data.get(fileExtNameBuffer,0,6);
		fileExtName = new String(fileExtNameBuffer);
		//将第7字节设置为'\0' end
		//fileExtName[ProtoCommon.FDFS_FILE_EXT_NAME_MAX_LEN] = '\0';
		//validate file name
		if ((result = StorageService.fdfs_validate_filename(fileExtName)) != 0)
		{
			LogKit.error(String.format("file_ext_name: %s is invalid!",fileExtName),this.getClass());
			return result;
		}

		fileContext.calcCrc32 = true;//need crc32 validate
		fileContext.calcFileHash = Globle.g_check_file_duplicate;//validate repeate file default false
		fileContext.extraInfo.upload.startTime = Globle.now();//record current time
		//set file ext name
		fileContext.extraInfo.upload.fileExtName = fileExtName;
		fileContext.extraInfo.upload.formattedExtName = StorageService.storage_format_ext_name(fileExtName);//fommat file ext name
		fileContext.extraInfo.upload.trunkInfo.path.storePathIndex = (short) storePathIndex;//set store_path_index
		fileContext.extraInfo.upload.fileType = ProtoCommon._FILE_TYPE_REGULAR;//set file type
		fileContext.syncFlag = ProtoCommon.STORAGE_OP_TYPE_SOURCE_CREATE_FILE;//set storage opt
		fileContext.timestamp2Log = fileContext.extraInfo.upload.startTime;
		fileContext.op = ProtoCommon.FDFS_STORAGE_FILE_OP_WRITE;//set context op is FDFS_STORAGE_FILE_OP_WRITE
		/*if (bAppenderFile)
		{
			fileContext.extra_info.upload.file_type |= \
						_FILE_TYPE_APPENDER;
		}
		else
		{
			if (g_if_use_trunk_file && trunk_check_size( \
				TRUNK_CALC_SIZE(file_bytes)))
			{
				fileContext.extra_info.upload.file_type |= \
							_FILE_TYPE_TRUNK;
			}
		}

		if (fileContext.extra_info.upload.file_type & _FILE_TYPE_TRUNK)
		{
			FDFSTrunkFullInfo pTrunkInfo;

			fileContext.extra_info.upload.if_sub_path_alloced = true;//alloc by who?
			pTrunkInfo = &(fileContext.extra_info.upload.trunk_info);//?
			if ((result=trunk_client_trunk_alloc_space( \
				TRUNK_CALC_SIZE(file_bytes), pTrunkInfo)) != 0)
			{
				return result;
			}

			clean_func = dio_trunk_write_finish_clean_up;
			file_offset = TRUNK_FILE_START_OFFSET((pTrunkInfo));
	    fileContext.extra_info.upload.if_gen_filename = true;
			trunk_get_full_filename(pTrunkInfo, fileContext.filename, \
					sizeof(fileContext.filename));
			fileContext.extra_info.upload.before_open_callback = \
						dio_check_trunk_file_when_upload;
			fileContext.extra_info.upload.before_close_callback = \
						dio_write_chunk_header;
			fileContext.open_flags = O_RDWR | g_extra_open_file_flags;
		}
		else*/
		{
			byte[] reserved_space_str = new byte[32];
			//TODO disk space alloc logic
			/*
			 * TODO Continue
			 * if (!storage_check_reserved_space_path(Globle.g_path_space_list 
				[store_path_index].total_mb, Globle.g_path_space_list 
				[store_path_index].free_mb - (file_bytes/Globle.FDFS_ONE_MB), 
				Globle.g_avg_storage_reserved_mb))
			{
				LogKit.error(String.format("no space to upload file,free space: %d MB is too small, file bytes: %ld, reserved space: %s", 
						Globle.g_path_space_list[store_path_index].free_mb,file_bytes,fdfs_storage_reserved_space_to_string_ex( 
								  Globle.g_storage_reserved_space.flag, 
								  Globle.g_avg_storage_reserved_mb, 
								  Globle.g_path_space_list[store_path_index]. 
										  total_mb, Globle.g_storage_reserved_space.rs.ratio,
										  reserved_space_str)), this.getClass());
				return ProtoCommon.ENOSPC;
			}*/

			crc32 = Globle.rand();
			fileContext.extraInfo.upload.ifSubPathAlloced = false;
			//generate store file name
			FileEntity fileEntity= StorageService.storage_get_filename(clientInfo, 
					fileContext.extraInfo.upload.startTime, 
					fileBytes, crc32, fileContext.extraInfo.upload.formattedExtName);
			fileContext.fileName = fileEntity.getAbsolutePathName();
			
			
			//set clean callback
			clean_func = dio_write_finish_clean_up(storageTask);
			fileOffset = 0;
			fileContext.extraInfo.upload.ifGenFileName = true;
			fileContext.extraInfo.upload.beforeOpenCallback = null;
			fileContext.extraInfo.upload.beforeCloseCallback = null;
			fileContext.openFlags = ProtoCommon.O_WRONLY | ProtoCommon.O_CREAT | ProtoCommon.O_TRUNC | Globle.g_extra_open_file_flags;
		}

		//write to file
	  return storage_write_to_file(storageTask, fileOffset, fileBytes, 
				storageTask.data.position(), dio_write_file(), 
				storage_upload_file_done_callback(), 
				clean_func, storePathIndex);

	}

	private int storage_write_to_file(StorageTask storageTask,
			long file_offset, long upload_bytes, int buff_offset,
			TaskDealFunc dio_write_file,
			FileDealDoneCallback storage_upload_file_done_callback,
			DisconnectCleanFunc clean_func, int store_path_index)
	{
		StorageClientInfo clientInfo   = storageTask.clientInfo;
		StorageFileContext fileContext =  clientInfo.fileContext;

		clientInfo.dealFunc = dio_write_file;
		clientInfo.cleanFunc = clean_func;

		fileContext.buffOffset = buff_offset;
		fileContext.offset = file_offset;
		fileContext.start = file_offset;
		fileContext.end = file_offset + upload_bytes;
		//get dio thread TODO Continue 现在只写死一个线程
		//fileContext.dio_thread_index = storage_dio_get_thread_index(storageTask, store_path_index, fileContext.op);
		fileContext.doneCallback = storage_upload_file_done_callback;

		if (fileContext.calcCrc32)
		{
			fileContext.crc32 = CRC32_XINIT;
		}

		/*
		 * TODO Continue
		 * if (fileContext.calc_file_hash)
		{
			if (g_file_signature_method == STORAGE_FILE_SIGNATURE_METHOD_HASH)
			{
				INIT_HASH_CODES4(fileContext.file_hash_codes)
			}
			else
			{
				my_md5_init(&fileContext.md5_context);
			}
		}*/

		//put task to dio_queue  提取到NioProcessor storage_dio_queue_push方法
		//让当前CHANNEL取消读
		StorageServer.context.storageDioService.storage_dio_queue_push(storageTask);
		return STORAGE_STATUE_DEAL_FILE;
	}

	private TaskDealFunc dio_write_file() {
		// TODO Auto-generated method stub
		return new TaskDealFunc() {
			
			@Override
			public int callback(StorageTask task) {
				// TODO Auto-generated method stub
				return 0;
			}
		};
	}

	//TODO 
	private FileDealDoneCallback storage_upload_file_done_callback()
	{
		return new FileDealDoneCallback() {
			
			@Override
			public int callback(StorageTask task) 
			{
				//先处理上传后的文件名等信息
				StorageFileContext fileContext = task.clientInfo.fileContext;
				//上传任务完成.重置task的参数信息
				task.data.clear();
				task.clientIp = null;
				task.length = 0;
				task.offset = 0;
				task.session.task = null;
				task.session = null;
				task.clientInfo.totalLength = 0;
				task.clientInfo.totalOffset = 0;
				task.clientInfo.cleanFunc = null;
				task.clientInfo.dealFunc  = null;
				task.clientInfo.stage = 0;
				fileContext.buffOffset = 0;
				fileContext.calcCrc32 = false;
				fileContext.end = 0;
				fileContext.start = 0;
				fileContext.timestamp2Log = 0;
				fileContext.syncFlag = 0;
				fileContext.doneCallback = null;
				fileContext.fileName = null;
				fileContext = null;
				
				//回收task复用
				StorageTaskPool.I.free(task);
				return 0;
			}
		};
	}

	//TODO 
	private DisconnectCleanFunc dio_write_finish_clean_up(
			StorageTask storageTask) 
	{
		return new DisconnectCleanFunc() {
			
			@Override
			public void callback(StorageTask task) {
				// TODO Auto-generated method stub
				
			}
		};
	}

	
	public static void main(String[] args) {
		String a = "\0\0\0";
		System.out.println(Arrays.toString(a.getBytes()));
		System.out.println(Globle.now());
	}
}
