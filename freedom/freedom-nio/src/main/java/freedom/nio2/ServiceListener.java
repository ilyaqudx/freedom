package freedom.nio2;

public interface ServiceListener {

	public void fireStartup();
	
	public void fireShutdown();

	public void addService(AbstractNioService abstractService);
}
