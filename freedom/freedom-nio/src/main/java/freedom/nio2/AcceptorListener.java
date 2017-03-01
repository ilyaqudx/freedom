package freedom.nio2;

public class AcceptorListener implements ServiceListener {

	private NioService service;
	
	public AcceptorListener()
	{
	}
	public AcceptorListener(NioService service)
	{
		this.service = service;
	}
	
	@Override
	public void fireStartup()
	{
		System.out.println("acceptor is running ,bind address : " + service.getBindAddress().getHostName() + ":" + service.getBindAddress().getPort());
	}

	@Override
	public void fireShutdown() {
		
	}
	@Override
	public void addService(AbstractNioService service)
	{
		this.service = service;
	}

}
