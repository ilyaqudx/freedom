package freedom.study.concurrent.masterwork;

import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Worker implements Runnable{

	private int count;
	private int avg;
	private Queue<Task> queue;
	private Map<String, Integer> resultMap;
	private CountDownLatch latch;
	
	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}

	public void setQueue(Queue<Task> queue) {
		this.queue = queue;
	}

	public void setResultMap(Map<String, Integer> resultMap) {
		this.resultMap = resultMap;
	}
	
	@Override
	public void run() 
	{
		try {
			Thread.sleep(new Random().nextInt(5000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true)
		{
			Task t = queue.poll();
			if(t == null)
			{
				resultMap.put("result-" + Thread.currentThread().getId(), avg / count);
				latch.countDown();
				break;
			}
			handle(t);
		}
	}

	private void handle(Task t)
	{
		count ++;
		avg += t.getWage();
	}

}
