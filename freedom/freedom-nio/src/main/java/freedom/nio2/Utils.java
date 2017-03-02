package freedom.nio2;

import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Utils {

	public static final boolean isBlank(String str)
	{
		return str == null || str.trim().equals("");
	}
	public static final boolean isNotBlank(String str)
	{
		return !isBlank(str);
	}
	
	public static final String getLocalAddress()
	{
		try
		{
			return InetAddress.getLocalHost().getHostAddress();
		} 
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		return "127.0.0.1";
	}
	
	/**反射调用构造方法	
	 * @param cls
	 * @param parameterType
	 * @param parameter
	 * @return
	 */
	public static final <T,P> T invokeCtor(Class<T> cls,Class<P> parameterType,P parameter)
	{
		try
		{
			return cls.getConstructor(parameterType).newInstance(parameter);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
}
