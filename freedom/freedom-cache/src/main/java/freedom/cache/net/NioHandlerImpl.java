package freedom.cache.net;

import freedom.cache.CacheOperator;
import freedom.cache.Command;
import freedom.cache.CommandParser;
import freedom.cache.CommandParserImpl;
import freedom.nio2.IdleState;
import freedom.nio2.NioHandler;
import freedom.nio2.NioSession;

public class NioHandlerImpl implements NioHandler {

	private CommandParser commandParser = new CommandParserImpl();
	private CacheOperator cacheOperator = new CacheOperator();
	
	@Override
	public void onCreated(NioSession session) {
		// TODO Auto-generated method stub
		
		/*new Thread(()->{
			while(true){
			try {
				Thread.sleep(5);
				session.write("i am server!!!\r\n");
					
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
		}).start();*/
	}

	@Override
	public void onReceived(NioSession session, Object msg)
	{
		Command command = null;
		try
		{
			command = commandParser.parse((byte[])msg);
		}
		catch (Exception e)
		{
			session.write("invalid command : " + new String((byte[])msg) + "\r\n");
		}
		
		if(command != null){
			String  response = cacheOperator.execute(command);
			session.write(response == null ? "null\r\n" : response + "\r\n");
		}
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
	public void onWriteSuspend(NioSession session, long qps) {
		// TODO Auto-generated method stub

	}

}
