package freedom.cache.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import freedom.cache.Const;
import freedom.nio2.ProtocolCodec;

public class Connection {

	private Socket socket;
	private BioHandler handler;
	private ProtocolCodec codec;
	private byte[] buffer = new byte[4096];
	private byte[] fragment = new byte[2048];
	private boolean hasFragment = false;
	public Connection(Socket socket,BioHandler handler,ProtocolCodec codec) throws IOException
	{
		this.socket  = socket;
		this.handler = handler;
		this.codec   = codec;
	}
	
	private String read(BioHandler handler)throws IOException {
		int len = socket.getInputStream().read(buffer);
		int ret = len;
		if(len > 0)
		{
			if(hasFragment)
			{
				//之前有消息未处理完
				byte[] array = new byte[fragment.length + len];
				System.arraycopy(fragment, 0, array, 0, fragment.length);
				System.arraycopy(buffer, 0, array, fragment.length, len);
				buffer = array;
				ret = buffer.length;
			}
			
			int offset = 0;
			String message = null;
			while(offset < ret){
				int start = offset;
				while(offset < ret && buffer[offset] == Const.SPACE)
				{
					offset ++;
				}
				boolean findCR = false;
				boolean findLF = false;
				while(offset < ret){
					byte v = buffer[offset++];
					if(v ==  Const.CR_LF[0])
						findCR = true;
					else if(findCR && v ==  Const.CR_LF[1])
					{
						findLF = true;
						break;
					}
					else
						findCR = false;
				}
				
				if(findCR && findLF){
					//完整的命令
					int end = offset - 2;
					message = end > start ? new String(buffer,start,end) : null;
				}
			}
			
			if(offset < ret){
				hasFragment = true;
				//说明有剩余的数据
				if(fragment.length >= (ret - offset))
					System.arraycopy(buffer, offset, fragment, 0, ret - offset);
				else{
					fragment = Arrays.copyOfRange(buffer, offset, ret);
				}
			}
			
			if(message != null){
				handler.onReceived(this, message);
				return message;
			}
		}
		if(len == -1)
			throw new IOException("socket is closed");
		
		return null;
	}
	
	public String write(String message)
	{
		try
		{
			if(message != null)
			{
				socket.getOutputStream().write(message.getBytes());//可能缓冲区已满,写入0字节
				socket.getOutputStream().flush();//这儿为什么发送不了数据.read操作阻塞了操作(FD monitor)
				return read(handler);
			}
			return null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			try {
				this.socket.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				socket.getInputStream().close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				socket.getOutputStream().close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally{
			ConnectionManager.I().backConnection(this);
		}
		return null;
	}
	
	
}
