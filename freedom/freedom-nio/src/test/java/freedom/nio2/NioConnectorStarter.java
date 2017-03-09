package freedom.nio2;

import java.net.InetSocketAddress;

import freedom.nio1.IdleState;
import freedom.nio1.NioConnector;
import freedom.nio1.NioHandler;
import freedom.nio1.NioSession;
import freedom.nio1.TextLineProcotolCodec;

public class NioConnectorStarter {

	public static void main(String[] args) {
		
		
		
		new NioConnector(new InetSocketAddress(8888), 
				new NioHandler() {
					
					@Override
					public void onWritten(NioSession session) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onReceived(NioSession session, Object msg) {
						System.out.println("received server message : " + msg);
						
						String say = "hello server! i am client";
						session.write(say);
						
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
						System.out.println("connect succcess!");
						String text = "set name zhangsan!";
						session.write(text);
					}
					
					@Override
					public void onClosed(NioSession abstractNioSession) {
						// TODO Auto-generated method stub
						
					}
				}, new TextLineProcotolCodec()).start();
	}
}
