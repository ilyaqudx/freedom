package freedom.bio;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

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
		ServerSocket server = new ServerSocket();
		server.bind(new InetSocketAddress(10086));
		while(true)
		{
			new ServerThread(server.accept()).start();
		}
	}
}
