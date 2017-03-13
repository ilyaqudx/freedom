package freedom.cache.ha;

import freedom.cache.Command;

public class MasterSlaveService {

	public static final MasterSlaveService I = 
			new MasterSlaveService();
	private MasterSlaveService(){}
	
	public Object execute(Command command)
	{
		final String data = command.key;
		
		return null;
	}
}
