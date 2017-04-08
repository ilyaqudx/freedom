package freedom.jdfs.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.DirectoryNotEmptyException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import com.google.common.io.Files;

import freedom.jdfs.LogKit;
import static freedom.jdfs.protocol.ProtoCommon.*;


public class StorageServer {

	public static final char[] DIR_0X_CHAR = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	
	public static void main(String[] args) throws Exception
	{
		//load config
		StorageConfig storageConfig = loadConfig();
		//storage根目录 
		String basePath = storageConfig.getBase_path();
		//检查data目录是否存在,如果不存在则创建storage path
		storage_check_and_make_data_dirs(storageConfig,basePath);
		
		
		
		StorageTaskPool storageTaskPool = new StorageTaskPool();
	}

	private static void storage_check_and_make_data_dirs(StorageConfig storageConfig,String basePath)
			throws IOException {
		File basePathDir = new File(basePath);
		if(!existFile(basePathDir))
		{
			LogKit.error(String.format("base_path is not exist : %s", basePath), StorageServer.class);
			throw new FileNotFoundException(String.format("base_path is not exist : %s", basePath));
		}
		
		//判断.data_init_flag文件是否存在,如果存在,则表示data已经创建了
		String data_path = String.format("%s/%s", basePath,"data");
		File data_init_flag = new File(String.format("%s/%s", data_path,".data_init_flag"));
		if(data_init_flag.exists()){
			
		}else{
			//data_init_flag not exist
			File data = new File(data_path);
			if(!existFile(data))
			{
				if(!data.mkdir())
					throw new FileNotFoundException(String.format("data is not exist : %s", data.getAbsolutePath()));
				//创建data_init_flag文件
				long storage_join_time = Globle.now();
				int  server_port       = storageConfig.getPort();
				int  http_port         = storageConfig.getHttp_server_port();
				try {
					Files.write(String.format(
							"%s=%s\r\n%s=%s\r\n%s=%s\r\n", 
							"storage_join_time",storage_join_time,
							"last_server_port",server_port,
							"last_http_port",http_port).getBytes(), data_init_flag);
				}
				catch (IOException e)
				{
					throw e;
				}
			}
		}
		
		boolean pathCreate = false;
		int store_path_count = storageConfig.getStore_path_count();
		for (int i = 0; i < store_path_count; i++) 
		{
			storage_make_data_dirs(storageConfig.getSubdir_count_per_path(),data_path);
			/**TODO 
			 * 	if (g_sync_old_done && pathCreated)  //repair damaged disk
				{
					if ((result=storage_disk_recovery_start(i)) != 0)
					{
						return result;
					}
				}
		
				result = storage_disk_recovery_restore(g_fdfs_store_paths.paths[i]);
				if (result == EAGAIN) //need to re-fetch binlog
				{
					if ((result=storage_disk_recovery_start(i)) != 0)
					{
						return result;
					}
		
					result=storage_disk_recovery_restore(g_fdfs_store_paths.paths[i]);
				}
		
				if (result != 0)
				{
					return result;
				}
			 * */
		}
	}

	private static boolean storage_make_data_dirs(int g_subdir_count_per_path,String data_path) throws DirectoryNotEmptyException {
		boolean pathCreate = false;
		//检查存储目录00 - FF
		String firstStorageDir = String.format(FDFS_STORAGE_DATA_DIR_FORMAT + "/" + FDFS_STORAGE_DATA_DIR_FORMAT, 0,0);
		String lastStorageDir  = String.format(FDFS_STORAGE_DATA_DIR_FORMAT + "/" + FDFS_STORAGE_DATA_DIR_FORMAT, g_subdir_count_per_path - 1,g_subdir_count_per_path - 1);
		if(!existFile(data_path,firstStorageDir) && !existFile(data_path,lastStorageDir))
		{
			for (int i = 0; i < g_subdir_count_per_path; i++) 
			{
				File mainDir = new File(data_path,String.format(FDFS_STORAGE_DATA_DIR_FORMAT, i));
				if(!mainDir.mkdirs()){
					throw new DirectoryNotEmptyException(mainDir.toString());
				}
				for (int m = 0; m < g_subdir_count_per_path; m++) {
					File subDir = new File(mainDir,String.format(FDFS_STORAGE_DATA_DIR_FORMAT, m));
					if(!subDir.mkdirs())
						throw new DirectoryNotEmptyException(subDir.toString());
				}
			}
			//create success
			pathCreate = true;
		}
		return pathCreate;
	}
	
	private static boolean existFile(String basePath, String lastStorageDir) 
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

	private static StorageConfig loadConfig() throws FileNotFoundException, IOException, NoSuchFieldException, SecurityException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException
	{
		Class<StorageConfig> clazz = StorageConfig.class;
		StorageConfig storageConfig = clazz.newInstance();
		FileInputStream fis = new FileInputStream("conf/storage.conf");
		Properties config   = new Properties();
		config.load(fis);
		Iterator<Entry<Object,Object>> it = config.entrySet().iterator();
		while(it.hasNext()){
			Entry<Object,Object> e = it.next(); 
			Field  field  = clazz.getDeclaredField((String)e.getKey());
			Object value  = e.getValue();
			if(value == null || value.toString().trim().equals(""))
				continue;
			field.setAccessible(true);
			Method method = clazz.getMethod("set" + firstUpper(field.getName()), field.getType());
			method.invoke(storageConfig, getJavaType(field.getType(),e.getValue()));
		}
		
		System.out.println(storageConfig);
		return storageConfig;
	}

	private static Object getJavaType(Class<?> type,Object value)
	{
		
		if(type == int.class)
			return Integer.parseInt(value.toString());
		else if(type == short.class)
			return Short.parseShort(value.toString());
		else if(type == long.class)
			return Long.parseLong(value.toString());
		else if(type == byte.class)
			return Byte.parseByte(value.toString());
		else if(type == boolean.class)
			return Boolean.parseBoolean(value.toString());
		return value;
	}

	private static String firstUpper(String name)
	{
		if(null == name || "".equals(name))return name;
		char[] cs = name.toCharArray();
		char first = cs[0];
		if(cs[0] >= 'a' && cs[0] <= 'z')
			cs[0] = (char) (first - 32);
		return new String(cs);
	}
}
