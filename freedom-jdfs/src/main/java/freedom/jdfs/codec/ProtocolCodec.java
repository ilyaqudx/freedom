package freedom.jdfs.codec;

import java.nio.BufferUnderflowException;

import freedom.jdfs.common.Header;
import freedom.jdfs.common.Packet;
import freedom.jdfs.common.Request;
import freedom.jdfs.common.buffer.IoBuffer;
import freedom.jdfs.exception.ProtocolParseException;
import freedom.jdfs.handler.MessageHandler;
import freedom.jdfs.nio.NioSession;
import freedom.jdfs.protocol.ProtoCommon;
import freedom.jdfs.protocol.RequestParser;
import freedom.jdfs.storage.StorageClientInfo;
import freedom.jdfs.storage.StorageTask;

/**
 * 协议解析
 * */
public class ProtocolCodec {

	public static final boolean decode(NioSession session,StorageTask storageTask,StorageClientInfo clientInfo) throws ProtocolParseException
	{
		IoBuffer buffer = storageTask.data;
		if(storageTask.state == StorageTask.STATE_INIT){
			return parseAndHandleRequest(session, storageTask, buffer);
		}
		else if(storageTask.state == StorageTask.STATE_NIO_READ){
			//判断本次数据是否装满
			if(!storageTask.isComplete())
				return true;
			MessageHandler.handleReadBytes(session, storageTask);
		}
		return false;
	}

	private static boolean parseAndHandleRequest(NioSession session,StorageTask storageTask, IoBuffer buffer)throws ProtocolParseException {
		buffer.flip();
		buffer.mark();
		if(buffer.remaining() < ProtoCommon.HEADER_LENGTH){
			buffer.reset();
			return true;
		}
		Header header = RequestParser.parseHeader(buffer);
		long bodyLength = header.getBodyLength();
		if(bodyLength <= 0){
			throw new ProtocolParseException("bodyLength < 0");
		}
		try 
		{
			Packet  packet  = RequestParser.parse(header, buffer);
			MessageHandler.handleRequest(session,new Request(header, packet), storageTask);
		}
		catch(BufferUnderflowException e)
		{
			//单纯认为是数据不够
			buffer.reset();
			return true;
		}
		catch (Exception e) 
		{
			throw new ProtocolParseException(e.getMessage());
		}
		return false;
	}
}
