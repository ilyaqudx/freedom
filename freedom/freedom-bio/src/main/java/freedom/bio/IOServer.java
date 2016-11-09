package freedom.bio;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import freedom.bio.codec.Codec;
import freedom.bio.core.PacketHead;
import freedom.bio.core.Utils;
import freedom.bio.moudel.logon.LogonSuccess;
import freedom.bio.moudel.logon.LogonUtils;

public class IOServer {

	public static final void test() throws InstantiationException, IllegalAccessException
	{
		byte[] buffer     = Utils.getBytes(LogonUtils.buildLogonSuccess());
		byte[] headBuffer = Utils.getBytes(Utils.buildPackageHead(buffer.length));
		
		byte[] data = new byte[headBuffer.length + buffer.length];
		System.arraycopy(headBuffer, 0, data, 0, headBuffer.length);
		System.arraycopy(buffer, 0, data, headBuffer.length, buffer.length);
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
		PacketHead  head    = Utils.parse(in, PacketHead.class);
		LogonSuccess account = Utils.parse(in, LogonSuccess.class);
		System.out.println(head);
		System.out.println(account);
		
	}
	
	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException 
	{
		test();
		ServerSocket server = new ServerSocket();
		server.bind(new InetSocketAddress(10086));
		while(true)
		{
			final Socket socket = server.accept();
			new Thread(new Runnable()
			{
				@Override
				public void run() 
				{
					
					Codec codec = new Codec();
					while(socket.isConnected())
					{
						try
						{
							/*InputStream in = socket.getInputStream();
							DataInputStream dis = new DataInputStream(in);
							PacketHead head     = Utils.parse(dis, PacketHead.class);
							LogonAccount account = Utils.parse(dis, LogonAccount.class);
							System.out.println(head);
							System.out.println(account);
							
							byte[] buffer     = Utils.getBytes(LogonUtils.buildLogonSuccess());
							byte[] headBuffer = Utils.getBytes(Utils.buildPackageHead(buffer.length));
							OutputStream out = socket.getOutputStream();
							out.write(headBuffer);
							out.write(buffer);
							out.flush();*/
							
						} 
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
	}
}
