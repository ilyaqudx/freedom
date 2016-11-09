package freedom.bio.core;

public class Dispatcher {

	
	public static final void dipatch(IoSession session,PacketReq packet)
	{
		try
		{
			/* 怎么返回数据给客户端才好(将数据发送给输出线程)
			 * 需要提供一个可操作写回数据的类
			 */
			PacketHead head = packet.getHead();
			Command command = CommandContext.I.getCommand(head.getMainCmd(),head.getSubCmd());
			if(null != command)
			{
				PacketRes res   = command.execute(session,packet.getData());
				session.write(res);
			}else
				throw new Exception(String.format("Command not found!code : 【%d,%d】", head.getMainCmd(),head.getSubCmd()));
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
}
