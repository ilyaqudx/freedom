package freedom.nio;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NioHandler implements IoHandler {

	@Override
	public void connected(IoSession session, Object msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void received(IoSession session, Object msg)
	{
		// TODO Auto-generated method stub
		//System.out.println(String.format("%d session handler message : %s", session.getId(),msg));
		try 
		{
			char[] en = new char[]{'a','b','c','d','e','f'};
			Map<String,Object> map = new HashMap<String,Object>();
			char[] arr = new char[1024000];
			for (int i = 0; i < 10240; i++) 
			{
				arr[i] = en[i % 6];
			}
			//map.put("name", new String(arr));
			//map.put("age", 10);
			session.write(new String(arr));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
