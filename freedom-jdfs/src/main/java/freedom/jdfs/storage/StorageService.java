package freedom.jdfs.storage;

import static freedom.jdfs.protocol.ProtoCommon.FDFS_FILE_DIST_PATH_ROUND_ROBIN;
import static freedom.jdfs.protocol.ProtoCommon.FDFS_FILE_EXT_NAME_MAX_LEN;
import static freedom.jdfs.protocol.ProtoCommon.FDFS_STORAGE_DATA_DIR_FORMAT;
import static freedom.jdfs.storage.StorageSync.STORAGE_OP_TYPE_SOURCE_CREATE_FILE;
import freedom.jdfs.protocol.ProtoCommon;
import freedom.jdfs.protocol.TrackerHeader;
import freedom.jdfs.storage.trunk.FDFSTrunkFullInfo;
public class StorageService {

	private static final Object stat_count_thread_lock = new Object();
	
	public static final int ALLOC_CONNECTIONS_ONCE  = 256;
	
	public static final int STORAGE_CREATE_FLAG_NONE = 0;
	public static final int STORAGE_CREATE_FLAG_FILE = 1;
	public static final int STORAGE_CREATE_FLAG_LINK = 2;

	public static final int STORAGE_DELETE_FLAG_NONE = 0;
	public static final int STORAGE_DELETE_FLAG_FILE = 1;
	public static final int STORAGE_DELETE_FLAG_LINK = 2;
	public static final void storage_service_init()
	{
		
	}
	
	public static final void storage_upload_file_done_callback(StorageTask storageTask,int errno)
	{

		StorageClientInfo clientInfo = storageTask.clientInfo;
		StorageFileContext fileContext = clientInfo.file_context;
		TrackerHeader pHeader;
		int result = errno;

		/*
		 * TODO Continue
		 * if (fileContext.extra_info.upload.file_type & _FILE_TYPE_TRUNK)
		{
			result = trunk_client_trunk_alloc_confirm( 
				&(fileContext.extra_info.upload.trunk_info), err_no);
			if (err_no != 0)
			{
				result = err_no;
			}
		}
		else
		{
			result = err_no;
		}*/

		if (result == 0)
		{
			result = storage_service_upload_file_done(storageTask);
			if (result == 0)
			{
				if ((fileContext.create_flag & STORAGE_CREATE_FLAG_FILE) > 0)
				{
					//记录binlog
					result = StorageSync.storage_binlog_write(
						fileContext.timestamp2log, 
						STORAGE_OP_TYPE_SOURCE_CREATE_FILE, 
						fileContext.fname2log);
				}
			}
		}

		if (result == 0)
		{
			int filename_len;
			//char *p;
			String p;

			if ((fileContext.create_flag & STORAGE_CREATE_FLAG_FILE) > 0)
			{
				/*
				 * TODO Continue
				 * CHECK_AND_WRITE_TO_STAT_FILE3_WITH_BYTES( 
					g_storage_stat.total_upload_count, 
					g_storage_stat.success_upload_count, 
					g_storage_stat.last_source_update, 
					g_storage_stat.total_upload_bytes, 
					g_storage_stat.success_upload_bytes, 
					fileContext.end - fileContext.start)*/
			}

			filename_len = fileContext.fname2log.length();
			clientInfo.total_length = ProtoCommon.HEADER_LENGTH + ProtoCommon.FDFS_GROUP_NAME_MAX_LEN + filename_len;
			storageTask.buffer.position(ProtoCommon.HEADER_LENGTH);
			storageTask.buffer.put(fileContext.extra_info.upload.group_name);//FDFS_GROUP_NAME_MAX_LEN
		}
		else
		{
			synchronized (stat_count_thread_lock) {
				if ((fileContext.create_flag & STORAGE_CREATE_FLAG_FILE) > 0)
				{
					Globle.g_storage_stat.total_upload_count++;
					Globle.g_storage_stat.total_upload_bytes += clientInfo.total_offset;
				}
			}

			clientInfo.total_length = ProtoCommon.HEADER_LENGTH;
		}

		//TODO Continue
		//STORAGE_ACCESS_LOG(pTask, ACCESS_LOG_ACTION_UPLOAD_FILE, result);

		clientInfo.total_offset = 0;
		storageTask.length = (int) clientInfo.total_length;

		storageTask.buffer.position(0);
		storageTask.buffer.put((byte)result);
		storageTask.buffer.put(ProtoCommon.STORAGE_PROTO_CMD_RESP);
		storageTask.buffer.putLong(clientInfo.total_length - ProtoCommon.HEADER_LENGTH);
		storage_nio_notify(storageTask);
	}

	private static void storage_nio_notify(StorageTask storageTask) 
	{
		
	}

	private static int storage_service_upload_file_done(StorageTask storageTask)
	{
		int result;
		int filename_len;
		StorageClientInfo pClientInfo = storageTask.clientInfo;
		StorageFileContext pFileContext = pClientInfo.file_context;
		long file_size = 0;
		long file_size_in_name=0;
		long end_time = 0;
		String new_fname2log;//char new_fname2log[128];
		String new_full_filename ;//new_full_filename[MAX_PATH_SIZE+64];
		String new_filename = null;//char new_filename[128];
		int new_filename_len;

		file_size = pFileContext.end - pFileContext.start;

		new_filename_len = 0;
		if ((pFileContext.extra_info.upload.file_type & ProtoCommon._FILE_TYPE_TRUNK) > 0)
		{
			//TODO Continue
			/*end_time = pFileContext.extra_info.upload.start_time;
			 * 
			Globle.COMBINE_RAND_FILE_SIZE(file_size, file_size_in_name);
			file_size_in_name |= ProtoCommon.FDFS_TRUNK_FILE_MARK_SIZE;*/
		}
		else
		{
			//TODO Continue
			/*struct stat stat_buf;
			//获取文件的属性
			if (stat(pFileContext.filename, &stat_buf) == 0)
			{
				end_time = stat_buf.st_mtime;
			}
			else
			{
				result = errno != 0 ? errno : ENOENT;
				STORAGE_STAT_FILE_FAIL_LOG(result, pTask.client_ip,
					"regular", pFileContext.filename)
				end_time = g_current_time;
			}

			if (pFileContext.extra_info.upload.file_type & _FILE_TYPE_APPENDER)
			{
				COMBINE_RAND_FILE_SIZE(0, file_size_in_name);
				file_size_in_name |= FDFS_APPENDER_FILE_SIZE;
			}
			else
			{
				file_size_in_name = file_size;
			}*/
		}

		//生成文件名
		try 
		{
			new_full_filename = storage_get_filename(pClientInfo, end_time,file_size_in_name, pFileContext.crc32,
					pFileContext.extra_info.upload.formatted_ext_name,new_filename, new_filename_len);
			
		} catch (Exception e) {
			storage_delete_file_auto(pFileContext);
			return ProtoCommon.FAIL;
		}
		
		pFileContext.extra_info.upload.group_name = Globle.g_group_name;
		new_fname2log = String.format("%c", "%c" + ProtoCommon.FDFS_STORAGE_DATA_DIR_FORMAT + "/%s", 
				ProtoCommon.FDFS_STORAGE_STORE_PATH_PREFIX_CHAR, 
				pFileContext.extra_info.upload.trunk_info.path. 
				store_path_index, new_filename);

		if ((pFileContext.extra_info.upload.file_type & ProtoCommon._FILE_TYPE_TRUNK) > 0)
		{
			/*
			 * TODO Continue
			 * char trunk_buff[FDFS_TRUNK_FILE_INFO_LEN + 1];
			trunk_file_info_encode(&(pFileContext.extra_info.upload. 
						trunk_info.file), trunk_buff);

			sprintf(new_fname2log + FDFS_LOGIC_FILE_PATH_LEN 
				+ FDFS_FILENAME_BASE64_LENGTH, "%s%s", trunk_buff, 
				new_filename + FDFS_TRUE_FILE_PATH_LEN + 
				FDFS_FILENAME_BASE64_LENGTH);*/
		}
		else if (rename(pFileContext.filename, new_full_filename) != 0)
		{
			/*logError("file: "__FILE__", line: %d, " 
				"rename %s to %s fail, " 
				"errno: %d, error info: %s", __LINE__, 
				pFileContext.filename, new_full_filename, 
				result, STRERROR(result));

			unlink(pFileContext.filename);*/
			return ProtoCommon.FAIL;
		}

		pFileContext.timestamp2log = end_time;
		if ((pFileContext.extra_info.upload.file_type & ProtoCommon._FILE_TYPE_APPENDER) > 0)
		{
			pFileContext.fname2log =  new_fname2log;
			pFileContext.create_flag = STORAGE_CREATE_FLAG_FILE;
			return 0;
		}

		if ((pFileContext.extra_info.upload.file_type & ProtoCommon._FILE_TYPE_SLAVE) > 0)
		{/*
			char true_filename[128];
			char filename[128];
			int master_store_path_index;
			int master_filename_len = strlen(pFileContext.extra_info. \
							upload.master_filename);
			if ((result=storage_split_filename_ex(pFileContext.extra_info.\
				upload.master_filename, &master_filename_len, \
				true_filename, &master_store_path_index)) != 0)
			{
				unlink(new_full_filename);
				return result;
			}
			if ((result=fdfs_gen_slave_filename(true_filename, \
				pFileContext.extra_info.upload.prefix_name, \
				pFileContext.extra_info.upload.file_ext_name, \
				filename, &filename_len)) != 0)
			{
				unlink(new_full_filename);
				return result;
			}

			snprintf(pFileContext.filename, sizeof(pFileContext.filename), \
				"%s/data/%s", g_fdfs_store_paths.paths[master_store_path_index], \
				filename);
			sprintf(pFileContext.fname2log, \
				"%c"FDFS_STORAGE_DATA_DIR_FORMAT"/%s", \
				FDFS_STORAGE_STORE_PATH_PREFIX_CHAR, \
				master_store_path_index, filename);

			if (g_store_slave_file_use_link)
			{
				if (symlink(new_full_filename, pFileContext.filename) != 0)
				{
					result = errno != 0 ? errno : ENOENT;
					logError("file: "__FILE__", line: %d, " \
						"link file %s to %s fail, " \
						"errno: %d, error info: %s", \
						__LINE__, new_full_filename, \
						pFileContext.filename, \
						result, STRERROR(result));

					unlink(new_full_filename);
					return result;
				}

				result = storage_binlog_write( \
						pFileContext.timestamp2log, \
						STORAGE_OP_TYPE_SOURCE_CREATE_FILE, \
						new_fname2log);
				if (result == 0)
				{
					char binlog_buff[256];
					snprintf(binlog_buff, sizeof(binlog_buff), \
						"%s %s", pFileContext.fname2log, \
						new_fname2log);
					result = storage_binlog_write( \
						pFileContext.timestamp2log, \
						STORAGE_OP_TYPE_SOURCE_CREATE_LINK, \
						binlog_buff);
				}
				if (result != 0)
				{
					unlink(new_full_filename);
					unlink(pFileContext.filename);
					return result;
				}

				pFileContext.create_flag = STORAGE_CREATE_FLAG_LINK;
			}
			else
			{
				if (rename(new_full_filename, pFileContext.filename) != 0)
				{
					result = errno != 0 ? errno : ENOENT;
					logError("file: "__FILE__", line: %d, " \
						"rename file %s to %s fail, " \
						"errno: %d, error info: %s", \
						__LINE__, new_full_filename, \
						pFileContext.filename, \
						result, STRERROR(result));

					unlink(new_full_filename);
					return result;
				}

				pFileContext.create_flag = STORAGE_CREATE_FLAG_FILE;
			}

			return 0;
		*/}

		pFileContext.fname2log = new_fname2log;
		if ((pFileContext.extra_info.upload.file_type & ProtoCommon._FILE_TYPE_TRUNK) <= 0)
		{
			pFileContext.filename = new_full_filename.getBytes();
		}

		if (Globle.g_check_file_duplicate && ((pFileContext.extra_info.upload.file_type & ProtoCommon._FILE_TYPE_LINK) <= 0))
		{/*
			GroupArray[] pGroupArray;
			byte[] value = new byte[128];
			FDHTKeyInfo key_info;
			char *pValue;
			int value_len;
			int nSigLen;
			byte[] szFileSig = new byte[ProtoCommon.FILE_SIGNATURE_SIZE];
			//char buff[64];

			key_info.namespace_len = Globle.g_namespace_len;
			key_info.szNameSpace = Globle.g_key_namespace;

			pGroupArray=&((g_nio_thread_data+pClientInfo.nio_thread_index).group_array);

			STORAGE_GEN_FILE_SIGNATURE(file_size,pFileContext.file_hash_codes, szFileSig);
			
			bin2hex(szFileSig, FILE_SIGNATURE_SIZE, buff);
			logInfo("file: "__FILE__", line: %d, " \
				"file sig: %s", __LINE__, buff);
			

			nSigLen = FILE_SIGNATURE_SIZE(pClientInfo.file_context.fi);
			key_info.obj_id_len = nSigLen;
			memcpy(key_info.szObjectId, szFileSig, nSigLen);
			key_info.key_len = sizeof(FDHT_KEY_NAME_FILE_ID) - 1;
			memcpy(key_info.szKey, FDHT_KEY_NAME_FILE_ID, \
					sizeof(FDHT_KEY_NAME_FILE_ID) - 1);

			pValue = value;
			value_len = sizeof(value) - 1;
			result = fdht_get_ex1(pGroupArray, g_keep_alive, \
					&key_info, FDHT_EXPIRES_NONE, \
					&pValue, &value_len, malloc);
			if (result == 0)
			{   //exists
				char *pGroupName;
				char *pSrcFilename;
				char *pSeperator;

				*(value + value_len) = '\0';
				pSeperator = strchr(value, '/');
				if (pSeperator == NULL)
				{
					logError("file: "__FILE__", line: %d, "\
						"value %s is invalid", \
						__LINE__, value);

					return EINVAL;
				}

				*pSeperator = '\0';
				pGroupName = value;
				pSrcFilename = pSeperator + 1;

				if ((result=storage_delete_file_auto(pFileContext)) != 0)
				{
					logError("file: "__FILE__", line: %d, "\
						"unlink %s fail, errno: %d, " \
						"error info: %s", __LINE__, \
						((pFileContext.extra_info.upload. \
						file_type & _FILE_TYPE_TRUNK) ? \
						pFileContext.fname2log \
						: pFileContext.filename), \
						result, STRERROR(result));

					return result;
				}

				memset(pFileContext.extra_info.upload.group_name, \
					0, FDFS_GROUP_NAME_MAX_LEN + 1);
				snprintf(pFileContext.extra_info.upload.group_name, \
					FDFS_GROUP_NAME_MAX_LEN + 1, "%s", pGroupName);
				result = storage_client_create_link_wrapper(pTask, \
					pFileContext.extra_info.upload.master_filename, \
					pSrcFilename, value_len-(pSrcFilename-value),\
					key_info.szObjectId, key_info.obj_id_len, \
					pGroupName, \
					pFileContext.extra_info.upload.prefix_name, \
					pFileContext.extra_info.upload.file_ext_name,\
					pFileContext.fname2log, &filename_len);

				pFileContext.create_flag = STORAGE_CREATE_FLAG_LINK;
				return result;
			}
			else if (result == ENOENT)
			{
				char src_filename[128];
				FDHTKeyInfo ref_count_key;

				filename_len = sprintf(src_filename, "%s", new_fname2log);
				value_len = sprintf(value, "%s/%s", \
						g_group_name, new_fname2log);
				if ((result=fdht_set_ex(pGroupArray, g_keep_alive, \
							&key_info, FDHT_EXPIRES_NEVER, \
							value, value_len)) != 0)
				{
					logError("file: "__FILE__", line: %d, "\
						"client ip: %s, fdht_set fail,"\
						"errno: %d, error info: %s", \
						__LINE__, pTask.client_ip, \
						result, STRERROR(result));

					storage_delete_file_auto(pFileContext);
					return result;
				}

				memcpy(&ref_count_key, &key_info, sizeof(FDHTKeyInfo));
				ref_count_key.obj_id_len = value_len;
				memcpy(ref_count_key.szObjectId, value, value_len);
				ref_count_key.key_len = sizeof(FDHT_KEY_NAME_REF_COUNT) - 1;
				memcpy(ref_count_key.szKey, FDHT_KEY_NAME_REF_COUNT, \
						ref_count_key.key_len);
				if ((result=fdht_set_ex(pGroupArray, g_keep_alive, \
					&ref_count_key, FDHT_EXPIRES_NEVER, "0", 1)) != 0)
				{
					logError("file: "__FILE__", line: %d, "\
						"client ip: %s, fdht_set fail,"\
						"errno: %d, error info: %s", \
						__LINE__, pTask.client_ip, \
						result, STRERROR(result));

					storage_delete_file_auto(pFileContext);
					return result;
				}


				result = storage_binlog_write(pFileContext.timestamp2log, \
						STORAGE_OP_TYPE_SOURCE_CREATE_FILE, \
						src_filename);
				if (result != 0)
				{
					storage_delete_file_auto(pFileContext);
					return result;
				}

				result = storage_client_create_link_wrapper(pTask, \
					pFileContext.extra_info.upload.master_filename, \
					src_filename, filename_len, szFileSig, nSigLen,\
					g_group_name, pFileContext.extra_info.upload.prefix_name, \
					pFileContext.extra_info.upload.file_ext_name, \
					pFileContext.fname2log, &filename_len);

				if (result != 0)
				{
					fdht_delete_ex(pGroupArray, g_keep_alive, &key_info);
					fdht_delete_ex(pGroupArray, g_keep_alive, &ref_count_key);

					storage_delete_file_auto(pFileContext);
				}

				pFileContext.create_flag = STORAGE_CREATE_FLAG_LINK;
				return result;
			}
			else //error
			{
				logError("file: "__FILE__", line: %d, " \
					"fdht_get fail, " \
					"errno: %d, error info: %s", \
					__LINE__, result, STRERROR(errno));

				storage_delete_file_auto(pFileContext);
				return result;
			}
		*/}

		if ((pFileContext.extra_info.upload.file_type & ProtoCommon._FILE_TYPE_LINK) > 0)
		{
			pFileContext.create_flag = STORAGE_CREATE_FLAG_LINK;
		}
		else
		{
			pFileContext.create_flag = STORAGE_CREATE_FLAG_FILE;
		}

		return 0;
}
	
	private static void STORAGE_GEN_FILE_SIGNATURE(long file_size,int[] hash_codes, byte[] sig_buff) {
		Globle.long2buff(file_size, sig_buff); 
		if (Globle.g_file_signature_method == ProtoCommon.STORAGE_FILE_SIGNATURE_METHOD_HASH) 
		{
			Globle.int2buff(hash_codes[0], sig_buff,8);  
			Globle.int2buff(hash_codes[1], sig_buff,12); 
			Globle.int2buff(hash_codes[2], sig_buff,16); 
			Globle.int2buff(hash_codes[3], sig_buff,20); 
		}
		else 
		{ 
			System.arraycopy(hash_codes, 0, sig_buff, 8, 16);
		}
	}

	private static int rename(byte[] filename, String new_full_filename) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static void storage_delete_file_auto(StorageFileContext pFileContext) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Notice
	 * */
	public static final String storage_get_filename(StorageClientInfo clientInfo,
			long start_time, long file_size, int crc32,
			byte[] szFormattedExt, String filename, int filename_len) 
	{
		int i;
		int result;
		int store_path_index; 
		String fullFileName = null;
		store_path_index = clientInfo.file_context.extra_info.upload.trunk_info.path.store_path_index;
		for (i=0; i< 10; i++)
		{
			filename =storage_gen_filename(clientInfo, file_size, 
				crc32, szFormattedExt, FDFS_FILE_EXT_NAME_MAX_LEN+1, 
				start_time, filename, filename_len);
			
			fullFileName = String.format("%s/data/%s", Globle.g_fdfs_store_paths.paths[store_path_index], filename);
			if (!Globle.existFile(fullFileName))
			{
				break;
			}
		}
		return fullFileName;

	}

	@SuppressWarnings("restriction")
	public static final String storage_gen_filename(StorageClientInfo clientInfo,
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
		//检查文件名是否有\
		encodeString = encodeString.replace("\\", "Z");
		encodeString = encodeString.replace("/", "Z");
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
				.append(new String(szFormattedExt,0,szFormattedExt.length-1)).toString();
		
		return filename;

	}
	
	public static final short[] storage_get_store_path(byte[] encoded, int filename_len)
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

	public static final void storage_format_ext_name(byte[] file_ext_name,
			byte[] szFormattedExt) 
	{
		int i;
		int ext_name_len;
		int pad_len;
		byte[] p = new byte[FDFS_FILE_EXT_NAME_MAX_LEN + 1];

		int firstZero = -1;
		for (int j = 0; j < file_ext_name.length; j++) {
			if(file_ext_name[j] == 0)
			{
				firstZero = j;
				break;
			}
		}
		
		ext_name_len = firstZero; 
		if (ext_name_len == 0)
		{
			pad_len = FDFS_FILE_EXT_NAME_MAX_LEN + 1;
		}
		else
		{
			pad_len = FDFS_FILE_EXT_NAME_MAX_LEN - ext_name_len;
		}

		p = szFormattedExt;
		for (i=0; i< pad_len; i++)
		{
			/*
			 * TODO Continue
			 * byte a = (byte) ('0' + (int)(10.0 * (double)Globle.rand() / ProtoCommon.RAND_MAX));
			System.out.println("random padding ext name : " + a);
			p[i] = (byte) Math.abs(a);*/
			p[i] = 65;
		}

		if (ext_name_len > 0){
			p[i++] = '.';
			//memcpy(p, file_ext_name, ext_name_len);
			System.arraycopy(file_ext_name, 0, p, i, ext_name_len);
			//p += ext_name_len;
			i += ext_name_len;
		}
		p[i++] = '\0';
	}

	public static final int fdfs_validate_filename(byte[] file_ext_name) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static final int storage_get_storage_path_index(int store_path_index)
	{
		return 0;
	}
	
}
