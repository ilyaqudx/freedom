package freedom.cache.client;

public class BioHandler {

	
	public void onReceived(Connection connection,String message)
	{
		System.out.println(message);
		//connection.write("get name  \r\n");
	}
	
	public void onCreated(Connection connection)
	{
		new Thread(()->{
			while(true){
				try {
					Thread.sleep(50);
					connection.write("get name\r\n");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
	}
}
