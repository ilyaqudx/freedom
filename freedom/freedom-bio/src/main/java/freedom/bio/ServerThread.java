package freedom.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import freedom.bio.codec.Codec;
import freedom.bio.core.IOBuffer;

public class ServerThread{

	private SocketThread read;
	private SocketThread write;
	
	public ServerThread(Socket socket)
	{
		Codec codec = new Codec();
		this.read   = new ReadThread(socket,codec);
		this.write  = new WriteThread(socket, codec);
		new Thread().start();
		new Thread(new WriteThread(socket,codec)).start();
	}
	
	static class SocketThread
	{
		protected Socket socket;
		protected Codec  codec;
		public SocketThread(Socket socket,Codec codec)
		{
			this.codec  = codec;
			this.socket = socket;
		}
	}
	
	static final class ReadThread extends SocketThread implements Runnable
	{
		public ReadThread(Socket socket, Codec codec)
		{
			super(socket, codec);
		}

		@Override
		public void run() 
		{
			int    fragment     = 0;
			byte[] readBuffer   = new byte[16348];
			while(socket.isConnected())
			{
				try 
				{
					InputStream in = socket.getInputStream();
					int len = in.read(readBuffer,fragment,readBuffer.length - fragment);
					int ret = fragment + len;
					byte[] data = new byte[ret];
					System.arraycopy(readBuffer, 0, data, 0, ret);
					IOBuffer buffer = IOBuffer.alloc(data);
					int startPos = buffer.pos();
					while(buffer.hasRemaining())
					{
						if(codec.decode(buffer) && buffer.pos() <= startPos)
							break;
					}
					
					if(buffer.hasRemaining())
					{
						//缓存未解读完的数据
						fragment = data.length - buffer.pos();
						System.arraycopy(data, buffer.pos(), readBuffer, 0, fragment);
					}
				} 
				catch 
				(IOException e) 
				{
					e.printStackTrace();
					try 
					{
						socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		
	}
	
	static final class WriteThread extends SocketThread implements Runnable
	{
		
		public WriteThread(Socket socket, Codec codec)
		{
			super(socket, codec);
		}

		@Override
		public void run()
		{
			while(socket.isConnected())
			{
				
			}
		}
		
	}

}
