package freedom.nio2.test;

import java.net.InetSocketAddress;
import java.util.Arrays;

import freedom.nio2.IdleState;
import freedom.nio2.NioAcceptor;
import freedom.nio2.NioHandler;
import freedom.nio2.NioSession;
import freedom.nio2.TextLineProcotolCodec;

public class NioAcceptorStarter {

	public static void main(String[] args) {
		
		new NioAcceptor(new InetSocketAddress(8888), new AcceptorNioHandler(), 
				new TextLineProcotolCodec()).start();
		
	}
	
	static class AcceptorNioHandler implements NioHandler{

		@Override
		public void onCreated(NioSession session) {
			// TODO Auto-generated method stub
			System.out.println(session + "连接成功!");
			
			new Thread(()->{
				byte[] data = new byte[1024 * 1024];
				//Arrays.fill(data,(byte) 97);
				//String msg = new String(data);
				while(true){
					try {
						Thread.sleep(5);
						session.write(data);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch (Exception e2) {
						e2.printStackTrace();
						break;
					}
				}
			}).start();
		}

		@Override
		public void onReceived(NioSession session, Object msg) {
			// TODO Auto-generated method stub
			//System.out.println("接收到客户端数据【" +msg+ "】");
		}

		@Override
		public void onWritten(NioSession session) {
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
		public void onClosed(NioSession abstractNioSession) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onWriteSuspend(NioSession session, long qps)
		{
			session.suspend = true;
			session.close();
			System.out.println(session + "【qps : "+qps+" byte|" +(qps / 1024)+ "k|" +(qps / 1024 / 1024)+ "m】");
		
		}
		
	}
}
