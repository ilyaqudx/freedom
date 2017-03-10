package freedom.cache.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CacheClientBoot {

	
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		List<ServerNode> servers = loadServer();
		ServerNodeManager.I.addNode(servers);
		String hello = ConnectionManager.I().getConnection().write("set name 张三哈!\r\n");
		System.out.println(hello);
		String hello2 = ConnectionManager.I().getConnection().write("get name\r\n");
		System.out.println(hello2);
		
	}

	private static List<ServerNode> loadServer() throws IOException 
	{
		InputStream in = CacheClientBoot.class.getClassLoader().getResourceAsStream("freedom/cache/client/client.properties");
		Properties properties = new Properties();
		properties.load(in);
		String str = properties.getProperty("serverlist");
		if(null == str || str.equals(""))
			throw new NullPointerException("client.properties [serverlist] is empty");
		List<ServerNode> serverNode = new ArrayList<ServerNode>();
		String[] address = str.split(",");
		for (int i = 0; i < address.length; i++) {
			serverNode.add(new ServerNode(address[i]));
		}
		return serverNode;
	}
}
