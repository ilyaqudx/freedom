package freedom.jdfs.command;

import freedom.jdfs.protocol.PacketHeader;

public abstract class AbstractCommand implements Command {

	@Override
	public void execute(PacketHeader packet)
	{
		try 
		{
			doExecute(packet.body);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public abstract void doExecute(byte[] data)throws Exception;
}
