package freedom.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import freedom.bio.codec.Codec;
import freedom.bio.codec.HttpCodec;
import freedom.bio.core.IOBuffer;
import freedom.bio.core.IoSession;

public class ServerThread{

	public Socket 		socket;
	public SocketThread read;
	public SocketThread write;
	public Codec        codec;
	
	public ServerThread(Socket socket)
	{
		this.socket = socket;
		this.codec  = new HttpCodec(new IoSession(this));
	}
	
	public void start()
	{
		this.read   = new ReadThread(socket,codec);
		this.write  = new WriteThread(socket, codec);
		new Thread(read).start();
		new Thread(write).start();
	}
	
	static abstract class SocketThread implements Runnable
	{
		protected Socket socket;
		protected Codec  codec;
		public SocketThread(Socket socket,Codec codec)
		{
			this.codec  = codec;
			this.socket = socket;
		}
	}
	
	static final class ReadThread extends SocketThread 
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
					byte[] readData = new byte[2048];
					int len = in.read(readData);
					if(len == -1)
						throw new IOException("远程主机断开连接!");
					
					int ret = fragment + len;
					byte[] data = new byte[ret];
					System.arraycopy(readBuffer, 0, data, 0, fragment);
					System.arraycopy(readData, 0, data, fragment, len);
					IOBuffer buffer = IOBuffer.alloc(data);
					int startPos = buffer.pos();
					while(buffer.hasRemaining())
					{
						if(codec.decode(buffer) && buffer.pos() <= startPos)
							break;
					}
					
					if(buffer.hasRemaining())
					{
						//缓存剩余数据
						fragment = data.length - buffer.pos();
						System.arraycopy(data, buffer.pos(), readBuffer, 0, fragment);
					}
				} 
				catch (IOException e) 
				{
					//e.printStackTrace();
					try 
					{
						socket.getInputStream().close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
					}finally{
						break;
					}
				}
			}
		}
		
	}
	
	static final class WriteThread extends SocketThread
	{
		
		public LinkedBlockingQueue<Object> packets = new LinkedBlockingQueue<Object>();
		
		public WriteThread(Socket socket, Codec codec)
		{
			super(socket, codec);
		}

		@Override
		public void run()
		{
			while(socket.isConnected())
			{
				try 
				{
					Object packet = packets.take();
					byte[]    data   = codec.encode(packet);
					OutputStream out = socket.getOutputStream();
					out.write(data);
					out.flush();
					System.out.println("数据发送结束 : " + packet);
					out.close();
				} 
				catch (IOException | InterruptedException e) 
				{
				}finally {
					try {
						socket.getOutputStream().close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
			}
		}
		
	}

	public void addPacket(Object res) 
	{
		((WriteThread)this.write).packets.offer(res);
	}

}
