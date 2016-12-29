package freedom.jdfs.handler;

import freedom.jdfs.protocol.ProtoCommon;
import freedom.jdfs.protocol.RecvPackageInfo;

public class CmdRouter {

	public static final void route(RecvPackageInfo packet)
	{
		if(packet.cmd == ProtoCommon.STORAGE_PROTO_CMD_UPLOAD_FILE)
		{
			
		}
		else if(packet.cmd == ProtoCommon.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE)
		{
			new QueryStorageAddressCommand().execute(packet);
		}
	}
}
