package freedom.jdfs.command;

import freedom.jdfs.common.Packet;
import freedom.jdfs.common.Request;
import freedom.jdfs.nio.NioSession;
import freedom.jdfs.storage.StorageTask;


public abstract class AbstractCommand<P extends Packet> implements Command<P> {

	@SuppressWarnings("unchecked")
	@Override
	public int execute(NioSession session , StorageTask storageTask,Request request) throws Exception 
	{
		return doCommand(session,storageTask, (P)request.getPacket());
	}
	
	protected abstract int doCommand(NioSession session,StorageTask storageTask,P packet) throws Exception;

}
