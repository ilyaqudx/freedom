package freedom.jdfs.command;

import freedom.jdfs.protocol.RecvPackageInfo;

public interface Command {

	
	public void execute0(RecvPackageInfo packet)throws Exception;
}
