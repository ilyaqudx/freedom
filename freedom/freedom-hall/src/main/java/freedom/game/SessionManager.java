package freedom.game;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
@Component
public class SessionManager {

	private Map<Long,ChannelHandlerContext> sessionMap = new
			ConcurrentHashMap<Long, ChannelHandlerContext>();
	
	public void add(long userId,ChannelHandlerContext session)
	{
		sessionMap.put(userId, session);
	}
	
	public boolean isOnline(long userId)
	{
		return sessionMap.get(userId) == null;
	}
	
	public ChannelHandlerContext get(long userId)
	{
			return sessionMap.get(userId);
	}
}
