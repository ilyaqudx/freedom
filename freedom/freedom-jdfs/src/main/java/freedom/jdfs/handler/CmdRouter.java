package freedom.jdfs.handler;

import freedom.jdfs.protocol.ProtoCommon;
import freedom.jdfs.protocol.RecvPackageInfo;

public class CmdRouter {

	public static final void route(RecvPackageInfo packet)
	{
		if(packet.cmd == ProtoCommon.STORAGE_PROTO_CMD_UPLOAD_FILE)
		{
			
		}
	}
}
