package freedom.jdfs.storage;

import java.io.File;
import java.util.Random;

import freedom.jdfs.protocol.ProtoCommon;
import static freedom.jdfs.protocol.ProtoCommon.*;
public class Globle {

	public static FDFSStorageStat g_storage_stat = new FDFSStorageStat();
	
	public static int g_file_distribute_path_mode = FDFS_FILE_DIST_PATH_ROUND_ROBIN;
	public static int g_file_distribute_rotate_count = FDFS_FILE_DIST_DEFAULT_ROTATE_COUNT;
	public static Object path_index_thread_lock = new Object();
	public static short g_dist_path_index_high = 0;
	public static short g_dist_path_index_low = 0;
	public static int g_dist_write_file_count = 0;
	
	
	public static final int FDFS_ONE_MB = 1024 *1024;
	//unsigned int  
	public static final int g_server_id_in_filename = 0;
	
	public static FDFSStorePaths g_fdfs_store_paths = new FDFSStorePaths();
	public static boolean g_check_file_duplicate = false;
	public static FDFSStorePathInfo[] g_path_space_list = null;

	public static int g_avg_storage_reserved_mb = 1024;//TODO FDFS_DEF_STORAGE_RESERVED_MB

	public static FDFSStorageReservedSpace g_storage_reserved_space;

	public static int g_extra_open_file_flags = 0;
	
	public static final long now()
	{
		return System.currentTimeMillis();
	}
	
	public static final Random random = new Random();

	public static final byte SUCCESS = 0;
	public static short g_subdir_count_per_path = 256;
	public static int g_stat_change_count = 0;

	public static String g_group_name ;//= new byte[ProtoCommon.FDFS_GROUP_NAME_MAX_LEN];//char g_group_name[FDFS_GROUP_NAME_MAX_LEN + 1] = {0};

	public static int g_namespace_len;

	public static byte[] g_key_namespace = new byte[ProtoCommon.FDHT_MAX_NAMESPACE_LEN];

	public static byte g_file_signature_method = ProtoCommon.STORAGE_FILE_SIGNATURE_METHOD_HASH;//STORAGE_FILE_SIGNATURE_METHOD_HAS;

	public static volatile boolean g_continue_flag = true;

	public static String g_fdfs_base_path;//这个为CONF配置的BASE_PATH

	public static int g_binlog_index;

	public static File g_binlog_fd;

	public static int rand() 
	{
		return random.nextInt();
	}
	
	public static int rand(int max) 
	{
		return random.nextInt(max);
	}
	
	public static boolean existFile(String basePath, String lastStorageDir) 
	{
		return existFile(new File(basePath,lastStorageDir));
	}

	public static final boolean existFile(String path)
	{
		return existFile(new File(path));
	}
	
	public static final boolean existFile(File file)
	{
		return file.exists();
	}
	
	//Notice
	public static final long COMBINE_RAND_FILE_SIZE(long file_size) 
	{
		long masked_file_size = 0;
		do 
		{ 
			int r = (rand() & 0x007FFFFF) | 0x80000000; 
			masked_file_size = ((((long)r) << 32 ) | file_size); 
		} while (false);
		return masked_file_size;
	}
	public static final int  buff2int(byte[] buff,int offset)
	{
		int value = 0;
		for (int i = 0; i < 4; i++) 
		{
			value |= (buff[offset++] & 0xff) << (24 - i * 8);
		}
		return value;
	}
	public static final long  buff2long(byte[] buff,int offset)
	{
		long value = 0;
		for (int i = 0; i < 8; i++) 
		{
			value |= (buff[offset++] & 0xff) << (56 - i * 8);
		}
		return value;
	}
	public static final void int2buff(int value,byte[] buff)
	{
		for (int i = 0; i < 4; i++) 
		{
			buff[i] = (byte)((value >>> (24 - i * 8)) & 0xff); 
		}
	}
	public static final void int2buff(int value,byte[] buff,int offset)
	{
		for (int i = 0; i < 4; i++) 
		{
			buff[offset++] = (byte)((value >>> (24 - i * 8)) & 0xff); 
		}
	}
	public static final byte[] long2buff(long value)
	{
		byte[] buff = new byte[8];
		long2buff(value, buff);
		return buff;
	}
	public static final void long2buff(long value,byte[] buff)
	{
		for (int i = 0; i < 8; i++) 
		{
			buff[i] = (byte)((value >>> (56 - i * 8)) & 0xff); 
		}
	}
	public static final void long2buff(long value,byte[] buff,int offset)
	{
		for (int i = 0; i < 8; i++) 
		{
			buff[offset++] = (byte)((value >>> (56 - i * 8)) & 0xff); 
		}
	}
	
	public static final int CRC32_FINAL(int crc) {
		return crc ^ 0xFFFFFFFF;
	}
	
	public static void main(String[] args) {
		
		System.out.println(random.nextInt());
		
		int a = -654964654;
		byte[] buff = new byte[4];
		int2buff(a, buff, 0);
		
		System.out.println(buff2int(buff,0));
	}
}
