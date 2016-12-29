package freedom.jdfs.command;

import freedom.jdfs.protocol.RecvPackageInfo;

public abstract class AbstractCommand implements Command {

	@Override
	public void execute(RecvPackageInfo packet)
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
