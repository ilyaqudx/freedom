package freedom.nio2;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

	private Map<Long,NioSession> sessions = 
			new ConcurrentHashMap<Long, NioSession>();
	
	public void add(NioSession session)
	{
		if(null != session)
			sessions.put(session.getId(), session);
	}
	
	public void checkIdle()
	{
		final Collection<NioSession> all = sessions.values();
		for (NioSession nioSession : all)
		{
			
		}
	}
}
