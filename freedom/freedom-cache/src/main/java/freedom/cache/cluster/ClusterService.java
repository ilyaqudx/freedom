package freedom.cache.cluster;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import freedom.nio2.NioSession;

public class ClusterService {

	public static final ClusterService 
		I = new ClusterService();
	private ClusterService(){}
	/**
	 * 集群中所有节点
	 * */
	private Map<NioSession,ClusterNode>
		nodes = new ConcurrentHashMap<NioSession,ClusterNode>();
	
	public void addNode(ClusterNode node)
	{
		if(node == null)return;
		
		nodes.put(node.getSession(), node);
	}
	
	public void remoteNode(ClusterNode node)
	{
		
	}
}
