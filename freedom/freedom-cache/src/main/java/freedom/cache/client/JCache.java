package freedom.cache.client;

import java.util.Map;

import freedom.nio2.NioService;


//session如何放入

//文本行协议(支持TELNET)客户端不能使用非阻塞通信(无法附加信息来标识哪个请求对应哪个应答)
public class JCache {
	
	private NioService service;

	private Map<Long, V>
	
	public JCache(NioService service)
	{
		this.service = service;
	}
	
	public String set(String key,String value)
	{
		this.service.getSessions().get(0).write(String.format("set %s %s", key,value));
	}
	
	public String get(String key)
	{
		return this.service.getSessions().get(0).write(String.format("get %s", key));
	}
	
	public String del(String key)
	{
		return this.service.getSessions().get(0).write(String.format("del %s", key));
	}
}
