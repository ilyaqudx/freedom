package freedom.cache.client;

import java.io.IOException;



//文本行协议(支持TELNET)客户端不能使用非阻塞通信(无法附加信息来标识哪个请求对应哪个应答)
public class JCache {
	
	public static final String set(String key,String value)
	{
		try {
			return ConnectionManager.I().getConnection().write(String.format("set %s %s", key, value));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static final String get(String key)
	{
		try {
			return ConnectionManager.I().getConnection().write(String.format("get %s", key));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static final String del(String key)
	{
		try {
			return ConnectionManager.I().getConnection().write(String.format("get %s", key));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
