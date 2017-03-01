package freedom.nio2;

public class ConnectorListener implements ServiceListener {

	private NioService service;
	
	@Override
	public void fireStartup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fireShutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addService(AbstractNioService abstractService) {
		// TODO Auto-generated method stub
		this.service = abstractService;
	}

}
