package freedom.nio2;

public interface NioHandler {

	public void onCreated(NioSession session);
	
	public void onReceived(NioSession session,Object msg);
	
	public void onWritten(NioSession  session);
	
	public void onIdle(NioSession session,IdleState state);
	
	public void onException(NioSession session,Throwable throwable);

	public void onClosed(NioSession abstractNioSession);
}
