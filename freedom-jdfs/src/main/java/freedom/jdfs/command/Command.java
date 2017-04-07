package freedom.jdfs.command;

import freedom.jdfs.nio.NioSession;
import freedom.jdfs.storage.StorageTask;

public interface Command {

	
	public int execute(NioSession session,StorageTask storageTask);
}
