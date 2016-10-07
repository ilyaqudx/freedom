package freedom.nio;

public class NioHandler implements IoHandler {

	@Override
	public void connected(IoSession session, Object msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void received(IoSession session, Object msg) {
		// TODO Auto-generated method stub
		//System.out.println(String.format("%d session handler message : %s", session.getId(),msg));
	}

	@Override
	public void disconnected(IoSession session, Object msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void idle(IoSession session, Object msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(IoSession session, Object msg) {
		// TODO Auto-generated method stub

	}

}
