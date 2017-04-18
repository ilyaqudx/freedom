package freedom.jdfs.storage;

import freedom.jdfs.common.buffer.IoBuffer;
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
	
	public static final int
			STATE_INIT 			= 0,
			STATE_HEADER_PARSED = 1,
			STATE_PACKET_PARSED = 2,
			STATE_DISK_WRITE 	= 4,
			STATE_DISK_READ 	= 8,
			STATE_NIO_WRITE 	= 16,
			STATE_NIO_READ 		= 32;
	
	public IoBuffer data = IoBuffer.allocate(DEFAULT_BUFFER_CAPACITY);
	public String clientIp;//16
	public int    size;//分配的大小,default 256K
	public int   length;//data length
	public int   offset;//current offset
	public long  reqCount;//请求数量,暂时不知道干什么
	public StorageClientInfo clientInfo = new StorageClientInfo();//client request info
	public StorageTask next;
	public NioSession session;
	public StorageTaskCallback callback;
	public int state;//0-未解析header,1-未解析request,2-已解析request
	
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
