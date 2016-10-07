package freedom.socket.command;

import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.alibaba.fastjson.JSON;

import freedom.socket.server.message.Request;


public abstract class AbstractCommand<M extends CommandMessage<?, ?>> implements Command<M> {

	@SuppressWarnings("unchecked")
	protected <T,V> M getCommandMessage(Object body) throws Exception
	{
		Type[] types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
		Class<M> clazzM = (Class<M>) types[0];
		Type[] messageTypes = ((ParameterizedType) clazzM.getGenericSuperclass()).getActualTypeArguments();
		Class<T> reqType = (Class<T>) messageTypes[0];
		CommandMessage<T, V> m = (CommandMessage<T, V>) clazzM.newInstance();
		try 
		{
			if(reqType == Void.class || null == body)
				return (M) m;
			return (M) m.setIn(JSON.parseObject(body.toString(), reqType));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public M executeCommand(Request request,ChannelHandlerContext session)throws Exception
	{
		M message = getCommandMessage(request.getBody());
		message.setSession(session);
		return this.execute(message);
	}
	
	public abstract M execute(M msg)throws Exception;
}
