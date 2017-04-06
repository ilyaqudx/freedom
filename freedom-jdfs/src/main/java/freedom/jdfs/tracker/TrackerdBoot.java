package freedom.jdfs.tracker;

import java.net.InetSocketAddress;

import freedom.jdfs.nio.NioAcceptor;

public class TrackerdBoot {

	public static void main(String[] args) 
	{
		try 
		{
			new NioAcceptor(new InetSocketAddress(8888)).start();
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
