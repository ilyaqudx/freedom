package freedom.nio2;

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
}
