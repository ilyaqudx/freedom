package freedom.cache.cluster;

import java.net.InetSocketAddress;

import freedom.nio2.NioService;
import freedom.nio2.NioSession;
/**
 * 集群中的节点信息
 * */
public class ClusterNode {

	private int id;
	private String name;
	private InetSocketAddress remoteAddress;
	
	private int coreCount = 1;//集群节点服务器CPU内核数量
	private int clientCount;//集群节点服务器上客户端连接数量
	private int clustersCount;//集群节点服务器上集群节点服务器连接数量
	private int localCount;//集群节点服务器上本地连接数量
	private NioSession session;// 集群节点服务器连接
	private int useCpu;//使用的cpu
	private int allCpu;//总CPU
	private int useMem;//使用内存
	private int allMem;//所有内存
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public InetSocketAddress getRemoteAddress() {
		return remoteAddress;
	}
	public void setRemoteAddress(InetSocketAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
	public int getCoreCount() {
		return coreCount;
	}
	public void setCoreCount(int coreCount) {
		this.coreCount = coreCount;
	}
	public int getClientCount() {
		return clientCount;
	}
	public void setClientCount(int clientCount) {
		this.clientCount = clientCount;
	}
	public int getClustersCount() {
		return clustersCount;
	}
	public void setClustersCount(int clustersCount) {
		this.clustersCount = clustersCount;
	}
	public int getLocalCount() {
		return localCount;
	}
	public void setLocalCount(int localCount) {
		this.localCount = localCount;
	}
	public NioSession getSession() {
		return session;
	}
	public void setSession(NioSession session) {
		this.session = session;
	}
	public int getUseCpu() {
		return useCpu;
	}
	public void setUseCpu(int useCpu) {
		this.useCpu = useCpu;
	}
	public int getAllCpu() {
		return allCpu;
	}
	public void setAllCpu(int allCpu) {
		this.allCpu = allCpu;
	}
	public int getUseMem() {
		return useMem;
	}
	public void setUseMem(int useMem) {
		this.useMem = useMem;
	}
	public int getAllMem() {
		return allMem;
	}
	public void setAllMem(int allMem) {
		this.allMem = allMem;
	}
	@Override
	public String toString() {
		return "ClusterNode [id=" + id + ", name=" + name + ", remoteAddress="
				+ remoteAddress + ", coreCount=" + coreCount + ", clientCount="
				+ clientCount + ", clustersCount=" + clustersCount
				+ ", localCount=" + localCount + ", session=" + session
				+ ", useCpu=" + useCpu + ", allCpu=" + allCpu + ", useMem="
				+ useMem + ", allMem=" + allMem + "]";
	}
}
