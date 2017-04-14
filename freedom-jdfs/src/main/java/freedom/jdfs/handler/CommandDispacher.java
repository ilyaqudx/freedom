package freedom.jdfs.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import freedom.jdfs.command.Command;
import freedom.jdfs.command.DownloadFileCommand;
import freedom.jdfs.command.UploadFileCommand;
import freedom.jdfs.nio.NioSession;
import freedom.jdfs.protocol.ProtoCommon;
import freedom.jdfs.storage.StorageTask;

public class CommandDispacher {

	static Logger LOGGER = Logger.getLogger(CommandDispacher.class);
	
	private static final Map<Byte, Command> commands = 
			new HashMap<Byte, Command>();
	
	static{
		commands.put(ProtoCommon.STORAGE_PROTO_CMD_UPLOAD_FILE, new UploadFileCommand());
		commands.put(ProtoCommon.STORAGE_PROTO_CMD_DOWNLOAD_FILE, new DownloadFileCommand());
	}
	
	public static final void dispatch(NioSession session,StorageTask storageTask)
	{
		byte cmd = storageTask.data.get(ProtoCommon.HEADER_LENGTH - 2);
		Command command = commands.get(cmd);
		if(command == null)
			LOGGER.error(String.format("cmd is not found : %d", cmd));
		else
			command.execute(session, storageTask);
	}
}
