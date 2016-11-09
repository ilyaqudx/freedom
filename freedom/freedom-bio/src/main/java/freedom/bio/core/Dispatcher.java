package freedom.bio.core;

import freedom.bio.core.Command.Main;
import freedom.bio.core.Command.Sub;
import freedom.bio.moudel.logon.LogonCommand;


public class Dispatcher {

	public static final PacketRes dipatch(PacketReq packet)
	{
		PacketHead head = packet.getHead();
		if(head.getMainCmd() == Main.GAME)
		{
			if(head.getSubCmd() == Sub.ACCOUNT_LOGON)
			{
				try 
				{
					return CommandContext.I.getCommand(LogonCommand.class).execute(packet.getData());
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return new PacketRes(Utils.buildPackageHead(0));
	}
}
