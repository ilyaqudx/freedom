package freedom.reactor;

import java.nio.channels.SelectionKey;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolDispatcher implements Dispatcher {

	private ExecutorService executorService;

	public ThreadPoolDispatcher(int poolSize) 
	{
		this.executorService = Executors.newFixedThreadPool(poolSize);
	}

	@Override
	public void stop() throws InterruptedException
	{
		executorService.shutdown();
	    executorService.awaitTermination(4, TimeUnit.SECONDS);
	}

	@Override
	public void onChannelReadEvent(final AbstractNioChannel channel,
			final Object readObject, final SelectionKey key) 
	{
		 executorService.execute(new Runnable()
		 {
			@Override
			public void run() {
				channel.getHandler().handleChannelRead(channel, readObject, key);
			}
		});
	}

}
