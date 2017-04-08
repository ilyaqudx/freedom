package freedom.jdfs.command;

import static freedom.jdfs.protocol.ProtoCommon.FDFS_FILE_DIST_PATH_ROUND_ROBIN;
import static freedom.jdfs.protocol.ProtoCommon.FDFS_FILE_EXT_NAME_MAX_LEN;
import static freedom.jdfs.protocol.ProtoCommon.FDFS_STORAGE_DATA_DIR_FORMAT;
import freedom.jdfs.LogKit;
import freedom.jdfs.nio.NioSession;
import freedom.jdfs.protocol.ProtoCommon;
import freedom.jdfs.storage.DisconnectCleanFunc;
import freedom.jdfs.storage.FileDealDoneCallback;
import freedom.jdfs.storage.Globle;
import freedom.jdfs.storage.StorageClientInfo;
import freedom.jdfs.storage.StorageFileContext;
import freedom.jdfs.storage.StorageTask;
import freedom.jdfs.storage.TaskDealFunc;
import freedom.jdfs.storage.trunk.FDFSTrunkFullInfo;

public class UploadFileCommand implements Command {

	@Override
	public int execute(NioSession session, StorageTask storageTask) 
	{
		StorageClientInfo clientInfo = storageTask.clientInfo;
		StorageFileContext fileContext = storageTask.fileContext;
		DisconnectCleanFunc clean_func;
		//int offset = (int) clientInfo.total_offset;
		byte[] filename = new byte[128];
		byte[] file_ext_name = new byte[7];
		long packetLen;//包体长度
		long file_offset;
		long file_bytes;
		int crc32;
		int store_path_index;
		int result;
		int filename_len;
		//请求的数据信息
		clientInfo = storageTask.clientInfo;
		fileContext =  storageTask.fileContext;
		//包体长度
		packetLen = clientInfo.total_length - ProtoCommon.HEADER_LENGTH;

		if (packetLen < 1 + ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE + 
				ProtoCommon.FDFS_FILE_EXT_NAME_MAX_LEN)
		{
			LogKit.error(String.format("client ip: %s, package size %ld is not correct,expect length >= %d", 
					storageTask.client_ip,packetLen,
					1 + ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE + 
					ProtoCommon.FDFS_FILE_EXT_NAME_MAX_LEN),this.getClass());
			return ProtoCommon.EINVAL;
		}

		//头部后第一个字节:存储路径索引
		store_path_index = storageTask.buffer.get();

		if (store_path_index == -1)
		{
			if ((result = storage_get_storage_path_index(store_path_index)) != 0)
			{
				LogKit.error(String.format("get_storage_path_index fail,errno: %d",result),this.getClass());
				return result;
			}
		}
		else if (store_path_index < 0 || store_path_index >= Globle.g_fdfs_store_paths.count)
		{
			LogKit.error(String.format("store_path_index: %d is invalid",store_path_index),this.getClass());
			return ProtoCommon.EINVAL;
		}

		//真实文件的长度
		file_bytes = storageTask.buffer.getLong();
		if (file_bytes < 0 || file_bytes != packetLen - (1 + ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE + 
						ProtoCommon.FDFS_FILE_EXT_NAME_MAX_LEN))
		{
			LogKit.error(String.format("pkg length is not correct,invalid file bytes: %ld, total body length: %ld",file_bytes, packetLen),this.getClass());
			return ProtoCommon.EINVAL;
		}
		storageTask.buffer.get(file_ext_name,storageTask.buffer.position(),6);
		//将第7字节设置为'\0' end
		file_ext_name[ProtoCommon.FDFS_FILE_EXT_NAME_MAX_LEN] = '\0';
		//validate file name
		if ((result = fdfs_validate_filename(file_ext_name)) != 0)
		{
			LogKit.error(String.format("file_ext_name: %s is invalid!",file_ext_name),this.getClass());
			return result;
		}

		fileContext.calc_crc32 = true;//need crc32 validate
		fileContext.calc_file_hash = Globle.g_check_file_duplicate;//validate repeate file default false
		fileContext.extra_info.upload.start_time = Globle.now();//record current time
		//set file ext name
		fileContext.extra_info.upload.file_ext_name = file_ext_name;
		storage_format_ext_name(file_ext_name,fileContext.extra_info.upload.formatted_ext_name);//fommat file ext name
		fileContext.extra_info.upload.trunk_info.path.store_path_index = (short) store_path_index;//set store_path_index
		fileContext.extra_info.upload.file_type = ProtoCommon._FILE_TYPE_REGULAR;//set file type
		fileContext.sync_flag = ProtoCommon.STORAGE_OP_TYPE_SOURCE_CREATE_FILE;//set storage opt
		fileContext.timestamp2log = fileContext.extra_info.upload.start_time;
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
			if (!storage_check_reserved_space_path(Globle.g_path_space_list 
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
			}

			crc32 = Globle.rand();
			filename[0] = '\0';
			filename_len = 0;
			fileContext.extra_info.upload.if_sub_path_alloced = false;
			//generate store file name
			if ((result=storage_get_filename(clientInfo, 
				fileContext.extra_info.upload.start_time, 
				file_bytes, crc32, fileContext.extra_info.upload.
				formatted_ext_name, new String(filename), filename_len, //TODO storage file name length
				fileContext.filename)) != 0)
			{
				return result;
			}

			//set clean callback
			clean_func = dio_write_finish_clean_up(storageTask);
			file_offset = 0;
			fileContext.extra_info.upload.if_gen_filename = true;
			fileContext.extra_info.upload.before_open_callback = null;
			fileContext.extra_info.upload.before_close_callback = null;
			fileContext.open_flags = ProtoCommon.O_WRONLY | ProtoCommon.O_CREAT | ProtoCommon.O_TRUNC | Globle.g_extra_open_file_flags;
		}

		//write to file
	  return storage_write_to_file(storageTask, file_offset, file_bytes, 
				storageTask.buffer.position(), dio_write_file(), 
				storage_upload_file_done_callback(), 
				clean_func, store_path_index);

	}

	private int storage_write_to_file(StorageTask storageTask,
			long file_offset, long file_bytes, int bufferOffset,
			TaskDealFunc dio_write_file,
			FileDealDoneCallback storage_upload_file_done_callback,
			DisconnectCleanFunc clean_func, int store_path_index)
	{
		return 0;
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

	/**
	 * Notice
	 * */
	private int storage_get_filename(StorageClientInfo clientInfo,
			long start_time, long file_size, int crc32,
			byte[] szFormattedExt, String filename, int filename_len,
			byte[] full_filename) 
	{
		int i;
		int result;
		int store_path_index; 
		String fullFileName;
		store_path_index = clientInfo.file_context.extra_info.upload.trunk_info.path.store_path_index;
		for (i=0; i<10; i++)
		{
			if ((result=storage_gen_filename(clientInfo, file_size, 
				crc32, szFormattedExt, FDFS_FILE_EXT_NAME_MAX_LEN+1, 
				start_time, filename, filename_len)) != 0)
			{
				return result;
			}
			fullFileName = String.format("%s/data/%s", Globle.g_fdfs_store_paths.paths[store_path_index], filename);
			if (!Globle.existFile(fullFileName))
			{
				break;
			}
		}
		return 0;

	}

	@SuppressWarnings("restriction")
	private int storage_gen_filename(StorageClientInfo clientInfo,
			long file_size, int crc32, byte[] szFormattedExt, int ext_name_len,
			long timestamp, String filename, int filename_len)
	{

		byte[] buff = new byte[20];
		byte[] encoded = new byte[33];
		int len;
		long masked_file_size;
		FDFSTrunkFullInfo pTrunkInfo = clientInfo.file_context.extra_info.upload.trunk_info;
		Globle.int2buff(Globle.g_server_id_in_filename, buff);
		Globle.int2buff((int)timestamp, buff,4);//Notice
		if ((file_size >> 32) != 0)
		{
			masked_file_size = file_size;
		}
		else
		{
			masked_file_size = Globle.COMBINE_RAND_FILE_SIZE(file_size);
		}
		Globle.long2buff(masked_file_size, buff,4*2);
		Globle.int2buff(crc32, buff,4*4);

		//base64_encode_ex(g_fdfs_base64_context, buff, 4 * 5, encoded,filename_len, false);
		String encodeString = new sun.misc.BASE64Encoder().encode(buff);   
		encoded = encodeString.getBytes();
		

		if (!clientInfo.file_context.extra_info.upload.if_sub_path_alloced)
		{
			short[] sub_paths = storage_get_store_path(encoded,filename_len);
			short sub_path_high = sub_paths[0];
			short sub_path_low = sub_paths[1];

			pTrunkInfo.path.sub_path_high = sub_path_high;
			pTrunkInfo.path.sub_path_low  = sub_path_low;

			clientInfo.file_context.extra_info.upload. 
					if_sub_path_alloced = true;
		}

		filename = String.format(FDFS_STORAGE_DATA_DIR_FORMAT + "/" +  
				FDFS_STORAGE_DATA_DIR_FORMAT + "/", 
				pTrunkInfo.path.sub_path_high, 
				pTrunkInfo.path.sub_path_low);
		/*memcpy(filename+len, encoded, filename_len);
		memcpy(filename+len+(filename_len), szFormattedExt, ext_name_len);
		*filename_len += len + ext_name_len;
		*(filename + (*filename_len)) = '\0';*/

		filename = new StringBuffer().append(filename)
				.append(new String(encoded))
				.append(new String(szFormattedExt)).toString();
		
		return 0;

	}

	private short[] storage_get_store_path(byte[] encoded, int filename_len)
	{
		short sub_path_high = 0, sub_path_low = 0;
		int n;
		int result;

		if (Globle.g_file_distribute_path_mode == FDFS_FILE_DIST_PATH_ROUND_ROBIN)
		{
			sub_path_high = Globle.g_dist_path_index_high;
			sub_path_low = Globle.g_dist_path_index_low;

			if (++Globle.g_dist_write_file_count >= Globle.g_file_distribute_rotate_count)
			{
				Globle.g_dist_write_file_count = 0;
		
				synchronized (Globle.path_index_thread_lock) {
					++Globle.g_dist_path_index_low;
					if (Globle.g_dist_path_index_low >= Globle.g_subdir_count_per_path)
					{  //rotate
						Globle.g_dist_path_index_high++;
						if (Globle.g_dist_path_index_high >= 
								Globle.g_subdir_count_per_path)  //rotate
						{
							Globle.g_dist_path_index_high = 0;
						}
						Globle.g_dist_path_index_low = 0;
					}

					++Globle.g_stat_change_count;
				}
			}
			
			return new short[]{sub_path_high,sub_path_low};
		}  
		else
		{//random
			//TODO 
			/*n = PJWHash(filename, filename_len) % (1 << 16);
			*sub_path_high = ((n >> 8) & 0xFF) % g_subdir_count_per_path;
			*sub_path_low = (n & 0xFF) % g_subdir_count_per_path;*/
		}
		return null;

	}

	private Object fdfs_storage_reserved_space_to_string_ex(byte flag,
			int g_avg_storage_reserved_mb, int total_mb, double ratio,
			byte[] reserved_space_str) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean storage_check_reserved_space_path(int total_mb, long l,
			int g_avg_storage_reserved_mb) {
		// TODO Auto-generated method stub
		return false;
	}

	private void storage_format_ext_name(byte[] file_ext_name,
			byte[] formatted_ext_name) {
		// TODO Auto-generated method stub
		
	}

	private int fdfs_validate_filename(byte[] file_ext_name) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int storage_get_storage_path_index(int store_path_index)
	{
		return 0;
	}
	
	
	public static void main(String[] args) {
		System.out.println(Globle.now());
	}
}
