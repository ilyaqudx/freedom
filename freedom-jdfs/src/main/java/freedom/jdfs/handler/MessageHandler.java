package freedom.jdfs.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import freedom.jdfs.command.Command;
import freedom.jdfs.command.DownloadFileCommand;
import freedom.jdfs.command.UploadFileCommand;
import freedom.jdfs.common.Header;
import freedom.jdfs.common.Packet;
import freedom.jdfs.common.Request;
import freedom.jdfs.exception.ProtocolParseException;
import freedom.jdfs.nio.NioSession;
import freedom.jdfs.protocol.ProtoCommon;
import freedom.jdfs.storage.StorageClientInfo;
import freedom.jdfs.storage.StorageServer;
import freedom.jdfs.storage.StorageTask;

public class MessageHandler {

	static Logger LOGGER = Logger.getLogger(MessageHandler.class);
	
	private static final Map<Byte, Command<? extends Packet>> commands = 
			new HashMap<Byte, Command<? extends Packet>>();
	
	static{
		commands.put(ProtoCommon.STORAGE_PROTO_CMD_UPLOAD_FILE, new UploadFileCommand());
		commands.put(ProtoCommon.STORAGE_PROTO_CMD_DOWNLOAD_FILE, new DownloadFileCommand());
	}

	public static void handleRequest(NioSession session, Request request,StorageTask storageTask) throws ProtocolParseException {
		StorageClientInfo clientInfo = storageTask.clientInfo;
		//请求头
		Header header = request.getHeader();
		//整个request的长度
		clientInfo.totalLength = header.getBodyLength() + ProtoCommon.HEADER_LENGTH;
		//storageTask只存储文件数据
		int receviedLength = storageTask.data.position() - storageTask.data.markValue();
		//已接收的数据长度
		clientInfo.totalOffset += receviedLength;
		//还剩余的数据由StorageTask重新接收
		storageTask.length = (int) Math.min(header.getBodyLength() - receviedLength,storageTask.size);
		//查询请求处理接口
		Command<? extends Packet> command = commands.get(request.getHeader().getCmd());
		if(command == null)
			LOGGER.error(String.format("cmd is not found : %d", request.getHeader().getCmd()));
		else
			command.execute(session, storageTask, request);
		//清除IoBuffer的数据
		storageTask.data.clear();
	}
	
	/**
	 * 处理接收的数据
	 * */
	public static final void handleReadBytes(NioSession session,StorageTask storageTask)
	{
		//取消读通知
		session.setIntestedRead(false);
		StorageClientInfo clientInfo = storageTask.clientInfo;
		if (clientInfo.isCompleteAll()){
			storageTask.reqCount++;
		}
		clientInfo.totalOffset += storageTask.length;
		//状态更改为写磁盘
		storageTask.state = StorageTask.STATE_DISK_WRITE;
		//storage_dio_queue_push(session,storageTask);
		StorageServer.context.storageDioService.pushToWrite(storageTask);
	}
}
