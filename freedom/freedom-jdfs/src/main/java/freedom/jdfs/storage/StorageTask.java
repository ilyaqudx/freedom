package freedom.jdfs.storage;

/**
 * storage任务
 * */
public class StorageTask {
	
	public static final int 
			FDFS_STORAGE_STAGE_NIO_INIT 	= 0, 
			FDFS_STORAGE_STAGE_NIO_RECV 	= 1,
			FDFS_STORAGE_STAGE_NIO_SEND 	= 2,
			FDFS_STORAGE_STAGE_NIO_CLOSE 	= 4,
			FDFS_STORAGE_STAGE_DIO_THREAD 	= 8;
	
	public byte[] clientIp;//16
	public byte[] arg;//扩展参数
	public byte[] data;//数据实体
	public int    size;//分配的大小
	public int   length;//data length
	public int   offset;//current offset
	public long  reqCount;//请求数量,暂时不知道干什么
	public int   stage;
	StorageTask next;
	
	
	
	
	
	
	
	
	
	
	
	
	
}
