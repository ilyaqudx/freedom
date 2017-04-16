package freedom.jdfs.storage;

import java.nio.ByteBuffer;

import freedom.jdfs.nio.NioSession;

/**
 * storage任务
 * */
public class StorageTask {
	
	public static final int DEFAULT_BUFFER_CAPACITY = 256 * 1024;
	
	public static final int 
			FDFS_STORAGE_STAGE_NIO_INIT 	= 0, 
			FDFS_STORAGE_STAGE_NIO_RECV 	= 1,
			FDFS_STORAGE_STAGE_NIO_SEND 	= 2,
			FDFS_STORAGE_STAGE_NIO_CLOSE 	= 4,
			FDFS_STORAGE_STAGE_DIO_THREAD 	= 8;
	
	public ByteBuffer data = ByteBuffer.allocate(DEFAULT_BUFFER_CAPACITY);
	public String clientIp;//16
	public int    size;//分配的大小,default 256K
	public int   length;//data length
	public int   offset;//current offset
	public long  reqCount;//请求数量,暂时不知道干什么
	public StorageClientInfo clientInfo = new StorageClientInfo();//client request info
	public StorageTask next;
	public NioSession session;
	public StorageTaskCallback callback;
	
	/**
	 * 是否已完成数据的读取或写出
	 * */
	public boolean isComplete()
	{
		return data.position() > 0 && data.position() >= length;
	}
	
	public int remaining()
	{
		return length - data.position();
	}
}
