package freedom.jdfs.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import freedom.jdfs.command.Command;
import freedom.jdfs.command.UploadFileCommand;
import freedom.jdfs.nio.NioSession;
import freedom.jdfs.protocol.ProtoCommon;
import freedom.jdfs.storage.StorageTask;

public class CommandRouter {

	static Logger LOGGER = Logger.getLogger(CommandRouter.class);
	
	private static final Map<Byte, Command> commands = 
			new HashMap<Byte, Command>();
	
	static{
		commands.put(ProtoCommon.STORAGE_PROTO_CMD_UPLOAD_FILE, new UploadFileCommand());
	}
	
	public static final void route(NioSession session,StorageTask storageTask)
	{
		byte cmd = storageTask.buffer.get(ProtoCommon.HEADER_LENGTH - 1);
		Command command = commands.get(cmd);
		if(command == null)
			LOGGER.error(String.format("cmd is not found : %d", cmd));
		else
			command.execute(session, storageTask);
	}
}
