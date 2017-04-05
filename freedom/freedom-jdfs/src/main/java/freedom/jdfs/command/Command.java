package freedom.jdfs.command;

import freedom.jdfs.protocol.PacketHeader;

public interface Command {

	
	public void execute(PacketHeader packet);
}
