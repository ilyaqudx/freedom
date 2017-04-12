package freedom.jdfs.storage;

import freedom.jdfs.nio.NioSession;

public class StorageClientInfo {

	public NioSession session;
	public int nioThreadIndex;  //nio thread index
	public boolean canceled;
	public byte stage;  //nio stage, send or recv
	public byte[] storage_server_id;// 16= new byte[ProtoCommon.FDFS_STORAGE_ID_MAX_SIZE];

	public long totalLength;   //request total length
	public long totalOffset;   //request total offset

	public long requestLength;   //request pkg length for access log

	//FDFSStorageServer pSrcStorage;
	public TaskDealFunc dealFunc;  //function pointer to deal this task
	public byte[] extraArg;   //store extra arg, such as (BinLogReader *)
	public DisconnectCleanFunc cleanFunc;  //clean function pointer when finished
	public StorageFileContext fileContext = new StorageFileContext();
}
