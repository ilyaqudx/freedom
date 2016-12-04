package freedom.study.concurrent.future;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class UserServiceProxy implements UserService {

	public UserServiceProxy() 
	{
	}
	
	@Override
	public String getUserName(int userId)
	{
//		final Future<String> future = new AbstractFuture();
		final Future<String> future = new CountDownLatchFuture();
		new Thread(new Runnable() {
			
			@Override
			public void run() 
			{
				int count = 10000;
				final CyclicBarrier barrier = new CyclicBarrier(count);
				final String username = request();
				for (int i = 0; i < count; i++) 
				{
					new Thread(new Runnable()
					{
						public void run()
						{
							try {
								barrier.await();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (BrokenBarrierException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							future.set(username);
						}
					}).start();
				}
			}
			
			private String request()
			{
				try 
				{
					Thread.sleep(5000);
				} 
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				return "hello world";
			}
			
		}).start();
		
		String userName = future.get();
		return userName;
	}

}
