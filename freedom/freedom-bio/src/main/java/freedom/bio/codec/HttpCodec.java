package freedom.bio.codec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import freedom.bio.core.IOBuffer;
import freedom.bio.core.IoSession;
import freedom.common.kit.StrKit;

public class HttpCodec implements Codec{

	private IoSession session;
	
	public HttpCodec(IoSession session)
	{
		this.session = session;
	}

	@Override
	public boolean decode(IOBuffer buffer)
	{
		if(buffer.hasRemaining())
		{
			String str = buffer.readCppString(buffer.remaining());
			System.out.println(str);
			BufferedReader reader = new BufferedReader(new StringReader(str));
			try 
			{
				String protocol = reader.readLine();
				String[] lines = protocol.split(" ");
				
				int paramIndex = -1;
				if((paramIndex = lines[1].indexOf('?')) > -1)
				{
					String params = lines[1].substring(paramIndex + 1);
					if(StrKit.isNotBlank(params))
					{
						String[] array = params.split("&");
						for (String string : array)
						{
							String[] kv = string.split("=");
							System.out.println(String.format("key = %s , v = %s", kv[0],kv[1]));
						}
					}
				}
				
				
				System.out.println("Http request method : " + lines[0]);
				System.out.println("Http protocol version : " + lines[2]);
				StringBuffer response = new StringBuffer();
				response
				.append("http/1.1 200 OK\r\n")
				.append("Server: freedom-bio/0.1\r\n")
				.append("Content-Type: text/html;charset=utf8\r\n")
				.append("Accept-ranges: bytes\r\n")
				.append("\r\n")
				.append("<html><head><meta charset=\"utf-8\"/></head><body><input type=\"text\" placeholder=\"请输入用户名\"/></body></html>");
				
				session.write(response.toString());
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return false;
	}

	@Override
	public byte[] encode(Object msg)
	{
		return encodeString((String) msg);
	}
	
	private byte[] encodeString(String msg)
	{
		return msg.getBytes();
	}
	

}
