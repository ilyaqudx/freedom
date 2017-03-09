package freedom.cache.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CacheClientBoot {

	
	public static void main(String[] args) throws IOException 
	{
		BioConnector connector = new BioConnector("127.0.0.1", 6666, new BioHandler(), null);
		//连接服务器
		connector.start();
		
		List<ServerNode> servers = loadServer();
		ServerNodeManager.I.addNode(servers);
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
