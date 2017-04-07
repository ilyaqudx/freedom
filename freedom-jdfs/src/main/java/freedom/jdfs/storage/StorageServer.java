package freedom.jdfs.storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;


public class StorageServer {

	
	
	public static void main(String[] args) throws Exception {
		
		//load config
		loadConfig();
		
		StorageTaskPool storageTaskPool = new StorageTaskPool();
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
			
			field.setAccessible(true);
			Method method = clazz.getMethod("set" + firstUpper(field.getName()), field.getType());
			method.invoke(storageConfig, e.getValue());
		}
		
		System.out.println(storageConfig);
		return storageConfig;
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
