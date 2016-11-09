package freedom.bio.core;

import freedom.bio.ServerThread;

public class IoSession {

	private ServerThread serverThread;
	public IoSession(ServerThread serverThread)
	{
		this.serverThread = serverThread;
	}
	
	public void write(PacketRes res) 
	{
		if(res == null)
			return;
		serverThread.addPacket(res);
	}

}
