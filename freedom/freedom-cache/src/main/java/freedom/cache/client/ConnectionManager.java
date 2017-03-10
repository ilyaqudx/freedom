package freedom.cache.client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


public class ConnectionManager implements Runnable{

	private static final int CONNECTION_COUNT = 3;
	
	private ThreadLocal<String> connectionUrlLocal = new ThreadLocal<String>();
	
	private Map<String, ArrayBlockingQueue<Connection>> connections = 
			new ConcurrentHashMap<String, ArrayBlockingQueue<Connection>>();
	
	private Executor executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
		
		@Override
		public Thread newThread(Runnable r) 
		{
			return new Thread(r,"connection-manager-thread");
		}
	});
	
	private static final ConnectionManager I = new ConnectionManager();
	
	private ConnectionManager() 
	{
		executor.execute(this);
	};
	
	public static final ConnectionManager I()
	{
		return I;
	}
	
	public void backConnection(Connection connection){
		if(null != connection){
			connections.get(connectionUrlLocal.get()).add(connection);
		}
	}
	
	public Connection getConnection() throws InterruptedException, IOException
	{
		ServerNode address = ServerNodeManager.I.getServer(InetAddress.getLocalHost().getHostAddress());
		String url = address.getAddress();
		boolean newCreate = false;
		ArrayBlockingQueue<Connection> array = connections.get(url);
		synchronized (this) {
			if(null == array){
				newCreate = true;
				array = new ArrayBlockingQueue<Connection>(CONNECTION_COUNT);
				connections.put(url, array);
			}
		}
		
		if(newCreate){
			String[] remoteAddress = url.split(":");
			for (int i = 0; i < CONNECTION_COUNT; i++) {
				BioConnector connector = new BioConnector(remoteAddress[0], Integer.parseInt(remoteAddress[1]), new BioHandler(), null);
				Connection connection = connector.connect();
				array.add(connection);
			}
		}
		connectionUrlLocal.set(url);
		return array.take();
	}

	@Override
	public void run() 
	{
		while(true){
			try {
				synchronized (this) {
					this.wait();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
