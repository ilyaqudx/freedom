package freedom.jdfs.command;

import freedom.jdfs.nio.NioSession;
import freedom.jdfs.storage.StorageTask;


public abstract class AbstractCommand implements Command {

	
	
	@Override
	public int execute(NioSession session, StorageTask storageTask) 
	{
		return 0;
	}

}
