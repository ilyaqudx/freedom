package freedom.jdfs.command;

import freedom.jdfs.common.Packet;
import freedom.jdfs.common.Request;
import freedom.jdfs.nio.NioSession;
import freedom.jdfs.storage.StorageTask;

public interface Command<P extends Packet> {

	
	public int execute(NioSession session,StorageTask storageTask,Request request);
}
