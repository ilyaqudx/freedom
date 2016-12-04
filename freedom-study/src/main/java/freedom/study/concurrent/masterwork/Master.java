package freedom.study.concurrent.masterwork;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

public class Master {

	private ConcurrentLinkedQueue<Task> workQueue = new ConcurrentLinkedQueue<Task>();
	
	private Map<String, Thread> workers = new ConcurrentHashMap<String, Thread>();
	
	private Map<String, Integer> resultMap = new ConcurrentHashMap<String, Integer>();
	
	private CountDownLatch latch = null;
	
	public Master(Worker worker,int workerCount)
	{
		latch = new CountDownLatch(workerCount);
		for (int i = 0; i < workerCount; i++)
		{
			workers.put("work-" + i, new Thread(worker));
		}
		worker.setQueue(workQueue);
		worker.setResultMap(resultMap);
		worker.setLatch(latch);
	}
	
	public void submit(List<Task> taskList)
	{
		workQueue.addAll(taskList);
	}
	
	public int execute()
	{
		for (Thread t : workers.values()) 
		{
			t.start();
		}
		
		try {
			latch.await();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return computeResult();
	}

	private int computeResult()
	{
		int avg = 0;
		for (int i : resultMap.values())
		{
			avg += i;
		}
		return avg / 10;
	}
}
