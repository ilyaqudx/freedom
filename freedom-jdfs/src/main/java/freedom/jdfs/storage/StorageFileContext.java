package freedom.jdfs.storage;

import java.io.RandomAccessFile;

import freedom.jdfs.protocol.ProtoCommon;
import freedom.jdfs.storage.trunk.FDFSTrunkFullInfo;

public class StorageFileContext {

	public byte[] filename = new byte[ProtoCommon.MAX_PATH_SIZE + 128];  	//full filename

	/* FDFS logic filename to log not including group name */
	//public byte[] fname2log = new byte[128+"-m".length()];
	public String fname2log;

	public byte op;            //w for writing, r for reading, d for deleting etc.
	public byte sync_flag;     //sync flag log to binlog
	public boolean calc_crc32;    //if calculate file content hash code
	public boolean calc_file_hash;      //if calculate file content hash code
	public int open_flags;           //open file flags
	public int[] file_hash_codes = new int[4];   //file hash code
	public int crc32;   //file content crc32 signature
	public MD5_CTX md5_context;

	public ExtraInfoUnion extra_info = new ExtraInfoUnion();

	public int dio_thread_index;		//dio thread index
	public long timestamp2log;		//timestamp to log
	public int delete_flag;     //delete file flag
	public int create_flag;    //create file flag
	public int buff_offset;    //buffer offset after recv to write to file
	public int fd;         //file description no
	public RandomAccessFile file;  //java中不能用文件描述符,直接保存为file
	public long start;  //the start offset of file
	public long end;    //the end offset of file
	public long offset; //the current offset of file
	public FileDealDoneCallback done_callback;
	public DeleteFileLogCallback log_callback;

	public long tv_deal_start; //task deal start tv for access log
	
	public class ExtraInfoUnion{
		public StorageUploadInfo upload = new StorageUploadInfo();//c 中的union  中的元素  extra_info
		public StorageSetMetaInfo setmeta = new StorageSetMetaInfo();//c 中的union  中的元素  extra_info
	}
	
	public class StorageUploadInfo{
		public boolean if_gen_filename;	  //if upload generate filename
		public byte file_type;           //regular or link file
		public boolean if_sub_path_alloced; //if sub path alloced since V3.0
		public byte[] master_filename = new byte[128];
		public byte[] file_ext_name = new byte[ProtoCommon.FDFS_FILE_EXT_NAME_MAX_LEN + 1];
		public byte[] formatted_ext_name = new byte[ProtoCommon.FDFS_FILE_EXT_NAME_MAX_LEN + 2];
		public byte[] prefix_name = new byte[ProtoCommon.FDFS_FILE_PREFIX_MAX_LEN + 1];
		public byte[] group_name = new byte[ProtoCommon.FDFS_GROUP_NAME_MAX_LEN + 1];  	//the upload group name
		public long start_time;		//upload start timestamp
		public FDFSTrunkFullInfo trunk_info = new FDFSTrunkFullInfo();
		public FileBeforeOpenCallback before_open_callback;
		public FileBeforeCloseCallback before_close_callback;
	}
}
