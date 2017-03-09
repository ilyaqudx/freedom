package freedom.cache.client;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ServerNodeManager {

	public static final ServerNodeManager I = new ServerNodeManager();
	
	/**
	 * 服务器hash值 ->服务器列表
	 * */
	private SortedMap<Integer,ServerNode> mapping = new TreeMap<Integer, ServerNode>();
	
	public static final int VIRTUAL_SERVER_NODE_COUNT = 100;
	
	private ServerNodeManager(){
		
	}
	
	public void addNode(List<ServerNode> nodes)
	{
		nodes.forEach(this::addNode);
	}
	
	public void addNode(ServerNode node)
	{
		if(null == node)
			throw new NullPointerException("node is null");
		synchronized (mapping) {
			for (int i = 0; i < VIRTUAL_SERVER_NODE_COUNT; i++) 
			{
				int hashCode = hash(node.getAddress() +"&VN" + i);
				mapping.put(hashCode, node);
			}
		}
	}
	
	public void delNode(ServerNode node)
	{
		if(null == node)
			throw new NullPointerException("node is null");
		synchronized (mapping) {
			Map<Integer,ServerNode> newMapping = mapping.entrySet().stream().filter((e) ->{
				return !e.getValue().getAddress().equals(node.getAddress());
			}).collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
			mapping = new TreeMap<Integer,ServerNode>(newMapping);
		}
	}
	
	public ServerNode getServer(String str)
	{
		int hashCode = hash(str);
		synchronized (mapping) {
			SortedMap<Integer, ServerNode> tailNodes = mapping.tailMap(hashCode);
			if(tailNodes.isEmpty())
				return mapping.get(mapping.firstKey());
			return mapping.get(tailNodes.firstKey());
		}
	}
	
	public static final int hash(String str)
	{
		final int p = 16777619;
		int hash = (int) 2166136261L;
		for (int i = 0; i < str.length(); i++)
			hash = (hash ^ str.charAt(i)) * p;
		hash += hash;
		hash ^= hash >> 7;
		hash += hash;
		hash ^= hash >> 17;
		hash += hash;

		// 如果算出来的值为负数则取其绝对值
		if (hash < 0)
			hash = Math.abs(hash);
		return hash;
	}
}
