package freedom.cache.cluster;

import java.net.InetSocketAddress;

import freedom.nio2.IdleState;
import freedom.nio2.NioHandler;
import freedom.nio2.NioSession;
import freedom.nio2.TextLineProcotolCodec;


/**
 * 集群服务器(主要管理集群中的节点)
 * */
public class ClusterServer {

	
	public static void main(String[] args) {
		
		new freedom.nio2.NioAcceptor(new InetSocketAddress(5555),
				new NioHandler() {
					
					@Override
					public void onWritten(NioSession session) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onWriteSuspend(NioSession session, long qps) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onReceived(NioSession session, Object msg) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onIdle(NioSession session, IdleState state) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onException(NioSession session, Throwable throwable) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onCreated(NioSession session) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onClosed(NioSession abstractNioSession)
					{
						
					}
				}, new TextLineProcotolCodec()).start();
		
	}
}
