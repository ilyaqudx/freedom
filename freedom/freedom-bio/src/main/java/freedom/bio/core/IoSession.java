package freedom.bio.core;

import freedom.bio.ServerThread;

public class IoSession {

	private ServerThread serverThread;
	public IoSession(ServerThread serverThread)
	{
		this.serverThread = serverThread;
	}
	
	public void write(Object msg) 
	{
		if(msg == null)
			return;
		serverThread.addPacket(msg);
	}

}
