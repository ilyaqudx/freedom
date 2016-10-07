package freedom.socket.command;

import io.netty.channel.ChannelHandlerContext;
import freedom.socket.server.message.Request;


public interface Command<M extends CommandMessage<?, ?>> {

	public M executeCommand(Request request,ChannelHandlerContext session) throws Exception;
}
