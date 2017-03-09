package freedom.nio2;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;


public class NioAcceptorStarter {

	public static void main(String[] args) 
	{
		new NioAcceptor(new InetSocketAddress(8888), 
				new NioHandler() {

			@Override
			public void onReceived(NioSession session, Object msg) {
				// TODO Auto-generated method stub
				System.out.println(session + "【收到消息】 : " + msg);
				
				WriteFuture future = session.write(msg,new FutureListener() {
					
					@Override
					public void exception(Throwable e) 
					{
						
					}
					
					@Override
					public void complete(long writeBytes) {
						// TODO Auto-generated method stub
						System.out.println("数据发送成功回调函数");
					}
				});
			}

			@Override
			public void onWritten(NioSession session) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onIdle(NioSession session, IdleState state) {
				// TODO Auto-generated method stub
				System.out.println(session + " 空闲状态" + state);
				session.write(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " - socket is idle【" +state+ "】");
			}

			@Override
			public void onException(NioSession session,
					Throwable throwable) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onCreated(NioSession session) {
				// TODO Auto-generated method stub
				System.out.println("socket is created : " + session);
			}

			@Override
			public void onClosed(NioSession session) {
				// TODO Auto-generated method stub
				System.out.println("socket is closed!" + session);
			}

			@Override
			public void onWriteSuspend(NioSession session, long qps) {
				// TODO Auto-generated method stub
				
			}
			
			
		}, new TextLineProcotolCodec()).start();
	}
}
