package freedom.jdfs.storage;

public class StorageClientInfo {

	public int nio_thread_index;  //nio thread index
	public boolean canceled;
	public byte stage;  //nio stage, send or recv
	public byte[] storage_server_id;// 16= new byte[ProtoCommon.FDFS_STORAGE_ID_MAX_SIZE];

	public StorageFileContext file_context;

	public long total_length;   //request total length
	public long total_offset;   //request total offset

	public long request_length;   //request pkg length for access log

	//FDFSStorageServer pSrcStorage;
	TaskDealFunc deal_func;  //function pointer to deal this task
	byte[] extra_arg;   //store extra arg, such as (BinLogReader *)
	DisconnectCleanFunc clean_func;  //clean function pointer when finished
	
}
