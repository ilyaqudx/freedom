package freedom.jdfs.storage;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import freedom.jdfs.LogKit;
import freedom.jdfs.protocol.ProtoCommon;

/**
 * char id[FDFS_STORAGE_ID_MAX_SIZE];
	char group_name[FDFS_GROUP_NAME_MAX_LEN + 1];
	char sync_src_id[FDFS_STORAGE_ID_MAX_SIZE];
 * */
public class StorageSync {

	public static final char STORAGE_OP_TYPE_SOURCE_CREATE_FILE	='C';  //upload file
	public static final char STORAGE_OP_TYPE_SOURCE_APPEND_FILE	='A';  //append file
	public static final char STORAGE_OP_TYPE_SOURCE_DELETE_FILE	='D';  //delete file
	public static final char STORAGE_OP_TYPE_SOURCE_UPDATE_FILE	='U';  //for whole file update such as metadata file
	public static final char STORAGE_OP_TYPE_SOURCE_MODIFY_FILE	='M';  //for part modify
	public static final char STORAGE_OP_TYPE_SOURCE_TRUNCATE_FILE	='T';  //truncate file
	public static final char STORAGE_OP_TYPE_SOURCE_CREATE_LINK	='L';  //create symbol link
	public static final char STORAGE_OP_TYPE_REPLICA_CREATE_FILE	='c';
	public static final char STORAGE_OP_TYPE_REPLICA_APPEND_FILE	='a';
	public static final char STORAGE_OP_TYPE_REPLICA_DELETE_FILE	='d';
	public static final char STORAGE_OP_TYPE_REPLICA_UPDATE_FILE	='u';
	public static final char STORAGE_OP_TYPE_REPLICA_MODIFY_FILE	='m';
	public static final char STORAGE_OP_TYPE_REPLICA_TRUNCATE_FILE	='t';
	public static final char STORAGE_OP_TYPE_REPLICA_CREATE_LINK	='l';

	public static final int STORAGE_BINLOG_BUFFER_SIZE	=	64 * 1024;
	public static final int STORAGE_BINLOG_LINE_SIZE	=	256;
	
	public static final String SYNC_BINLOG_FILE_PREFIX		 = "binlog";
	public static final String SYNC_BINLOG_INDEX_FILENAME	 = SYNC_BINLOG_FILE_PREFIX + ".index";
	public static final String SYNC_MARK_FILE_EXT		 	 = ".mark";
	public static final String SYNC_BINLOG_FILE_EXT_FMT	 	 = ".%03d";
	public static final String SYNC_DIR_NAME			 	 = "sync";
	public static final String MARK_ITEM_BINLOG_FILE_INDEX	 = "binlog_index";
	public static final String MARK_ITEM_BINLOG_FILE_OFFSET	 = "binlog_offset";
	public static final String MARK_ITEM_NEED_SYNC_OLD		 = "need_sync_old";
	public static final String MARK_ITEM_SYNC_OLD_DONE		 = "sync_old_done";
	public static final String MARK_ITEM_UNTIL_TIMESTAMP	 = "until_timestamp";
	public static final String MARK_ITEM_SCAN_ROW_COUNT	     = "scan_row_count";
	public static final String MARK_ITEM_SYNC_ROW_COUNT	     = "sync_row_count";
	
	String id;
	String group_name;
	String sync_src_id;
	
	private static final Lock sync_thread_lock = new ReentrantLock();
	
	private static int binlog_write_cache_len = 0;
	
	public static final int SYNC_BINLOG_WRITE_BUFF_SIZE = 16 * 1024;
	
	private static byte[] binlog_write_cache_buff = new byte[SYNC_BINLOG_WRITE_BUFF_SIZE];
	
	private static MappedByteBuffer byteBuffer;
	private static long binlog_file_size;
	private static int g_binlog_index;
	private static int binlog_write_version;
	public static final long SYNC_BINLOG_FILE_MAX_SIZE = 1024 * 1024 * 1024;
	
	
	public static final void storage_sync_init() throws IOException
	{
		RandomAccessFile file = new RandomAccessFile("","rw");
		byteBuffer = file.getChannel().map(MapMode.READ_WRITE, 0, SYNC_BINLOG_FILE_MAX_SIZE);
	}
	
	
	/**
	 * 记录binlog
	 * */
	private static final int storage_binlog_write_ex(long timestamp, char op_type, 
			String filename,String extra)
	{
		int write_ret = 0;
		try {
			sync_thread_lock.lock();
			
			if (extra != null)
			{
				byte[] logBuffer = String.format("%d %c %s %s\n", timestamp, op_type, filename, extra).getBytes();
				System.arraycopy(logBuffer, 0, binlog_write_cache_buff, binlog_write_cache_len, logBuffer.length);
			}
			else
			{
				byte[] logBuffer = String.format("%d %c %s\n", timestamp, op_type, filename).getBytes();
				System.arraycopy(logBuffer, 0, binlog_write_cache_buff, binlog_write_cache_len, logBuffer.length);
			}

			//check if buff full
			if (SYNC_BINLOG_WRITE_BUFF_SIZE - binlog_write_cache_len < 256)
			{
				//binlog在内存中达到了１６Ｋ则写入磁盘中
				write_ret = storage_binlog_fsync(false);  //sync to disk
			}
			else
			{
				write_ret = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sync_thread_lock.unlock();
		}
		return write_ret;
	}


	private static int storage_binlog_fsync(boolean bNeedLock)
	{
		int write_ret = 0;

		try {
			if(bNeedLock){
				sync_thread_lock.lock();
			}

			if (binlog_write_cache_len == 0) //ignore
			{
				write_ret = 0;  //skip
			}
			else{
				byteBuffer.put(binlog_write_cache_buff, 0, binlog_write_cache_len);

				binlog_file_size += binlog_write_cache_len;
				if (binlog_file_size >= SYNC_BINLOG_FILE_MAX_SIZE)
				{
					if ((write_ret=write_to_binlog_index(g_binlog_index + 1)) == 0)
					{
						write_ret = open_next_writable_binlog();
					}

					binlog_file_size = 0;
					if (write_ret != 0)
					{
						Globle.g_continue_flag = false;
						LogKit.error(String.format("open binlog file \"%s\" fail, program exit!", get_writable_binlog_filename(null)), StorageSync.class);
					}
				}
				else
				{
					write_ret = 0;
				}
			}

			binlog_write_version++;
			binlog_write_cache_len = 0;  //reset cache buff
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(bNeedLock)
				sync_thread_lock.unlock();
		}
		return write_ret;
}




	private static String get_writable_binlog_filename(String full_filename) 
	{
		String base = "%s/data/" + SYNC_DIR_NAME + "/" + SYNC_BINLOG_FILE_PREFIX + SYNC_BINLOG_FILE_EXT_FMT;
		return String.format(base, StorageServer.storageConfig.base_path,Globle.g_binlog_index);
	}


	private static int open_next_writable_binlog() {

		if (Globle.g_binlog_fd != null)
		{
			//close(g_binlog_fd);
			Globle.g_binlog_fd = null;
		}

		String full_filename = get_writable_binlog_filename1(Globle.g_binlog_index + 1);
		
		File file = new File(full_filename);
		if(file.exists()){
			if(!file.delete()){
				LogKit.error(String.format("delete binlog file %s fail", file.getAbsolutePath()), StorageSync.class);
				return ProtoCommon.FAIL;
			}
			LogKit.error(String.format("binlog file %s already exist,truncate", file.getAbsolutePath()), StorageSync.class);
		}

		try
		{
			file.createNewFile();
		}
		catch (IOException e) 
		{
			LogKit.error(String.format("create binlog file %s fail", file.getAbsolutePath()), StorageSync.class);
			return ProtoCommon.FAIL;
		}

		Globle.g_binlog_index++;
		return ProtoCommon.SUCCESS;
}


	private static String get_writable_binlog_filename1(int binlog_index) 
	{
		return String.format("%s/data/%s/%s", StorageServer.storageConfig.getBase_path(),
				SYNC_DIR_NAME,SYNC_BINLOG_FILE_PREFIX + SYNC_BINLOG_FILE_EXT_FMT,binlog_index);
	}


	private static int write_to_binlog_index(int binlog_index) 
	{
		String full_filename = String.format("%s/data/%s/%s", StorageServer.storageConfig.getBase_path(),SYNC_DIR_NAME,SYNC_BINLOG_INDEX_FILENAME);
		try 
		{
			Files.write(Paths.get(full_filename),String.valueOf(binlog_index).getBytes());
		}
		catch (IOException e)
		{
			LogKit.error(String.format("write to binlog index %s fail", full_filename), StorageSync.class);
			return ProtoCommon.FAIL;
		}

		return 0;

	}


	public static int storage_binlog_write(long timestamp2log,char storageOpTypeSourceCreateFile, String fname2log) 
	{
		return storage_binlog_write_ex(timestamp2log, storageOpTypeSourceCreateFile, fname2log, null);
	}
}
