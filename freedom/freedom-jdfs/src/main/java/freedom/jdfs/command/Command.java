package freedom.jdfs.command;

import freedom.jdfs.protocol.RecvPackageInfo;

public interface Command {

	
	public void execute(RecvPackageInfo packet);
}
