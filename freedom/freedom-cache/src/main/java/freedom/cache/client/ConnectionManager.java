package freedom.cache.client;

public class ConnectionManager {

	
	private Connection connection ;
	
	public static final ConnectionManager I = new ConnectionManager();
	
	private ConnectionManager(){};
	
	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}
	
	public Connection getConnection()
	{
		return this.connection;
	}
}
