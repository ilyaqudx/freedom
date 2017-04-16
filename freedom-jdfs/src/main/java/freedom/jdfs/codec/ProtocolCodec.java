package freedom.jdfs.codec;

import java.nio.ByteBuffer;

import freedom.jdfs.exception.ProtocolParseException;
import freedom.jdfs.handler.CommandDispacher;
import freedom.jdfs.nio.NioSession;
import freedom.jdfs.protocol.PacketHeader;
import freedom.jdfs.protocol.ProtoCommon;
import freedom.jdfs.storage.StorageClientInfo;
import freedom.jdfs.storage.StorageServer;
import freedom.jdfs.storage.StorageTask;

/**
 * 协议解析
 * */
public class ProtocolCodec {

	public static final boolean decode(NioSession session,StorageTask storageTask,StorageClientInfo clientInfo) throws ProtocolParseException
	{
		ByteBuffer buffer = storageTask.data;
		if(clientInfo.totalLength == 0)
		{
			buffer.position(0);
			int remaining = buffer.remaining();
			//no enough header
			if(remaining < ProtoCommon.HEADER_LENGTH)
			{
				return true;
			}
			PacketHeader header = parseHeader(buffer);
			clientInfo.totalLength = header.getBodyLen();
			if(clientInfo.totalLength <= 0)
			{
				//must to close session;
				throw new ProtocolParseException(String.format("【Channel %d total_length <= 0 , buffer : %s】", session.id,storageTask.data));
			}
			clientInfo.totalLength += ProtoCommon.HEADER_LENGTH;
			storageTask.length = (int) Math.min(clientInfo.totalLength , storageTask.size);
			buffer.position(remaining);
		}
		
		//判断本次数据是否装满
		if(storageTask.isComplete())
		{
			//取消读通知
			session.setIntestedRead(false);
			if (clientInfo.isCompleteAll())
			{
				/*
				 * 此代码可以移动到处理结束时进行添加
				 *  current req recv done */
				//clientInfo.stage = StorageTask.FDFS_STORAGE_STAGE_NIO_SEND;
				storageTask.reqCount++;
			}

			boolean firstPacket = clientInfo.totalOffset == 0;
			clientInfo.totalOffset += storageTask.length;
			if (firstPacket)
			{
				//第一个请求包
				CommandDispacher.dispatch(session, storageTask);
			}
			else
			{
				/* continue write to file */
				//storage_dio_queue_push(session,storageTask);
				StorageServer.context.storageDioService.addWriteTask(storageTask);
			}
			return false;
		}
		return true;
	}
	/**
	 * 解析消息头
	 * */
	private static final PacketHeader parseHeader(ByteBuffer buffer)
	{
		return new PacketHeader(buffer.getLong(),buffer.get(),buffer.get());
	}
}
