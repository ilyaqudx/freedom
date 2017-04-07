package freedom.jdfs.storage;

import java.util.Random;

public class Globle {

	public static final int FDFS_ONE_MB = 1024 *1024;
	
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

	public static int rand() 
	{
		return random.nextInt();
	}
	
	public static void main(String[] args) {
		
		System.out.println(random.nextInt());
	}
}
