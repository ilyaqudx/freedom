package freedom.study.interview.design.build;

public class AppDirector implements Director {

	private Builder manager= new AppProjectManagerBuilder();
	private Builder producter  = new AppProducterBuilder();
	private Builder ui = new AppUIDesignerBuilder();
	private Builder server = new AppServerEngineerBuilder();
	private Builder client = new AppClientEngineerBuilder();
	private Builder tester = new AppTesterBuilder();
	
	@Override
	public void build()
	{
		producter.build();
		ui.build();
		server.build();
		client.build();
		tester.build();
		manager.build();
	}

	@Override
	public Product get()
	{
		return new AppProduct();
	}

}
