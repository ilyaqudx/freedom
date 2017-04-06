package freedom.jdfs.storage;

import freedom.jdfs.protocol.ProtoCommon;
import freedom.jdfs.storage.trunk.FDFSTrunkFullInfo;

public class StorageFileContext {

	byte[] filename = new byte[ProtoCommon.MAX_PATH_SIZE + 128];  	//full filename

	/* FDFS logic filename to log not including group name */
	byte[] fname2log = new byte[128+"-m".length()];

	char op;            //w for writing, r for reading, d for deleting etc.
	char sync_flag;     //sync flag log to binlog
	boolean calc_crc32;    //if calculate file content hash code
	boolean calc_file_hash;      //if calculate file content hash code
	int open_flags;           //open file flags
	int[] file_hash_codes = new int[4];   //file hash code
	int crc32;   //file content crc32 signature
	MD5_CTX md5_context;

	StorageUploadInfo upload;
	StorageSetMetaInfo setmeta;

	int dio_thread_index;		//dio thread index
	int timestamp2log;		//timestamp to log
	int delete_flag;     //delete file flag
	int create_flag;    //create file flag
	int buff_offset;    //buffer offset after recv to write to file
	int fd;         //file description no
	long start;  //the start offset of file
	long end;    //the end offset of file
	long offset; //the current offset of file
	FileDealDoneCallback done_callback;
	DeleteFileLogCallback log_callback;

	long tv_deal_start; //task deal start tv for access log
	
	
	static final class StorageUploadInfo{
		boolean if_gen_filename;	  //if upload generate filename
		byte file_type;           //regular or link file
		boolean if_sub_path_alloced; //if sub path alloced since V3.0
		byte[] master_filename = new byte[128];
		byte[] file_ext_name = new byte[ProtoCommon.FDFS_FILE_EXT_NAME_MAX_LEN + 1];
		byte[] formatted_ext_name = new byte[ProtoCommon.FDFS_FILE_EXT_NAME_MAX_LEN + 2];
		byte[] prefix_name = new byte[ProtoCommon.FDFS_FILE_PREFIX_MAX_LEN + 1];
		byte[] group_name = new byte[ProtoCommon.FDFS_GROUP_NAME_MAX_LEN + 1];  	//the upload group name
		int start_time;		//upload start timestamp
		FDFSTrunkFullInfo trunk_info;
		FileBeforeOpenCallback before_open_callback;
		FileBeforeCloseCallback before_close_callback;
	}
}
