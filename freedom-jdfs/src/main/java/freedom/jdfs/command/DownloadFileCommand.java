package freedom.jdfs.command;

import java.io.File;

import freedom.jdfs.nio.NioSession;
import freedom.jdfs.protocol.DownloadPacket;
import freedom.jdfs.protocol.ProtoCommon;
import freedom.jdfs.storage.StorageClientInfo;
import freedom.jdfs.storage.StorageFileContext;
import freedom.jdfs.storage.StorageServer;
import freedom.jdfs.storage.StorageTask;

public class DownloadFileCommand implements Command {

	@Override
	public int execute(NioSession session, StorageTask storageTask) 
	{
		DownloadPacket packet = DownloadPacket.parsePacket(storageTask.data,storageTask.clientInfo.totalLength);
		
		System.out.println(packet);
		String fileName = packet.getFileName().substring(packet.getFileName().indexOf("/")+1);
		//check file name and group
		String absolutePath = String.format("%s/data/%s",StorageServer.storageConfig.getBase_path(),fileName);
		
		File file = new File(absolutePath);
		storageTask.data.clear();
		storageTask.data.putLong(file.length());
		storageTask.data.put(ProtoCommon.STORAGE_PROTO_CMD_RESP);
		storageTask.data.put(ProtoCommon.SUCCESS);
		storageTask.offset = 0;
		storageTask.length = Math.min(storageTask.size, (int)file.length() + ProtoCommon.HEADER_LENGTH);
		
		StorageClientInfo clientInfo = storageTask.clientInfo;
		clientInfo.fileContext = new StorageFileContext();
		clientInfo.fileContext.start = 0;
		clientInfo.fileContext.end   = file.length();
		clientInfo.fileContext.offset= 0;
		clientInfo.fileContext.fileName = absolutePath;
		clientInfo.fileContext.buffOffset = ProtoCommon.HEADER_LENGTH;
		clientInfo.fileContext.op    = ProtoCommon.FDFS_STORAGE_FILE_OP_READ;
		clientInfo.totalOffset = 0;
		clientInfo.totalLength = 10 + file.length();
		
		StorageServer.context.storageDioService.addReadTask(storageTask);
		
		return 0;
	}
}
