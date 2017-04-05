package freedom.jdfs.storage;

public class StorageClientInfo {

	int nio_thread_index;  //nio thread index
	boolean canceled;
	byte stage;  //nio stage, send or recv
	byte[] storage_server_id;// 16= new byte[ProtoCommon.FDFS_STORAGE_ID_MAX_SIZE];

	StorageFileContext file_context;

	long total_length;   //pkg total length for req and request
	long total_offset;   //pkg current offset for req and request

	long request_length;   //request pkg length for access log

	//FDFSStorageServer pSrcStorage;
	TaskDealFunc deal_func;  //function pointer to deal this task
	byte[] extra_arg;   //store extra arg, such as (BinLogReader *)
	DisconnectCleanFunc clean_func;  //clean function pointer when finished
	
}
