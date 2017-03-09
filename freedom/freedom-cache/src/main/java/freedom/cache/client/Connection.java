package freedom.cache.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import freedom.cache.Const;
import freedom.nio2.ProtocolCodec;

public class Connection {

	private Socket socket;
	private BioHandler handler;
	private ProtocolCodec codec;
	/**发送队列*/
	private BlockingQueue<String> writeQueue = new ArrayBlockingQueue<String>(500);
	/**接收队列*/
	private BlockingQueue<String> readQueue  = new ArrayBlockingQueue<String>(500);
	private byte[] buffer = new byte[4096];
	private byte[] fragment = new byte[2048];
	private boolean hasFragment = false;
	public Connection(Socket socket,BioHandler handler,ProtocolCodec codec) throws IOException
	{
		this.socket  = socket;
		this.handler = handler;
		this.codec   = codec;
		final InputStream  in  = socket.getInputStream();
		OutputStream       out = socket.getOutputStream();
		PrintWriter writer =  new PrintWriter(out);
		new Thread(()->{
			
			while(socket.isConnected())
			{
				try
				{
					String message = writeQueue.take();
					if(message != null){
						System.out.println("准备发送数据");
						//out.write(message.getBytes());//可能缓冲区已满,写入0字节
						//out.flush();//这儿为什么发送不了数据.我晕!
						writer.write(message);
						System.out.println("发送数据成功");
					}
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			
			
		}, "write-thread").start();
		
		new Thread(() ->{
			
			while(socket.isConnected())
			{
				try
				{
					int len = in.read(buffer);
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
								
								if(message != null){
									handler.onReceived(this, message);
								}
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
					}
					if(len == -1)
						throw new IOException("socket is closed");
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
					try {
						socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			
		},"read-thread").start();
	}
	
	
	public void write(String message)
	{
		writeQueue.add(message);
		System.out.println("待发送队列长度【" +writeQueue.size()+ "】");
	}
	
}
