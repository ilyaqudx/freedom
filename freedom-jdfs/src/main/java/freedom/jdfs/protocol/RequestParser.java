package freedom.jdfs.protocol;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;

import freedom.jdfs.common.Header;
import freedom.jdfs.common.Packet;
import freedom.jdfs.common.buffer.IoBuffer;
import freedom.jdfs.exception.CommandNotFoundException;
import freedom.jdfs.exception.ProtocolParseException;

public class RequestParser {

	protected static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	protected static final CharsetEncoder UTF8_ENCODER = DEFAULT_CHARSET.newEncoder();
	protected static final CharsetDecoder UTF8_DECODER = DEFAULT_CHARSET.newDecoder();
	
	private static final Map<Byte, Parser> requests = new HashMap<Byte, Parser>();
	
	static{
		
		requests.put(ProtoCommon.STORAGE_PROTO_CMD_UPLOAD_FILE, (header,buffer)->
		{
			return new UploadFilePacket(buffer.get(),buffer.getLong(),buffer.getString(6, UTF8_DECODER));
		});
		
		requests.put(ProtoCommon.STORAGE_PROTO_CMD_DOWNLOAD_FILE, (header,buffer) ->
		{
			return new DownloadFilePacket( buffer.getLong(), buffer.getLong(), buffer.getString(16, UTF8_DECODER), 
					buffer.getString((int)header.getBodyLength() - 32, UTF8_DECODER));
		});
	}
	
	/**解析请求头
	 * @param buffer
	 * @return
	 */
	public static final Header parseHeader(IoBuffer buffer)
	{
		return new Header(buffer.getLong(),buffer.get(),buffer.get());
	}
	
	/**解析请求实体
	 * @param header
	 * @param buffer
	 * @param cmd
	 * @param clazz
	 * @return
	 * @throws Exception 
	 */
	public static final Packet  parse(Header header,IoBuffer buffer)throws Exception
	{
		Parser parser = requests.get(header.getCmd());
		if(null != parser){
			return parser.parse(header,buffer);
		}
		throw new CommandNotFoundException("cmd not found : " + header.getCmd());
	}
	
	
	static interface Parser{
		
		public Packet parse(Header header,IoBuffer buffer) throws Exception;
	}
}
