package freedom.nio2;

public interface EventListener {

	public void onCreated(NioSession session);
	
	public void onRecevied(NioSession session,int len);
	
	public void onWriten(NioSession session,int len);
	
	public void onException(NioSession session);
	
	public void onIdel(NioSession session,IdleState state);
}
