package freedom.nio;


public interface IoHandler {

	public void connected(IoSession session, Object msg);

	public void received(IoSession session, Object msg);

	public void disconnected(IoSession session, Object msg);

	public void idle(IoSession session, Object msg);

	public void write(IoSession session, Object msg);
}
