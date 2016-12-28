package freedom.jdfs.command;

import freedom.jdfs.protocol.RecvPackageInfo;

public abstract class AbstractCommand implements Command {

	@Override
	public void execute0(RecvPackageInfo packet) throws Exception
	{
		execute(packet.body);
	}
	
	public abstract void execute(byte[] data)throws Exception;
}
