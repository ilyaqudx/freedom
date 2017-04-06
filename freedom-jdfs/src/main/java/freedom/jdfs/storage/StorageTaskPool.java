package freedom.jdfs.storage;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class StorageTaskPool {

	/**
	 * max task count
	 * */
	public static final int MAX_TASK_COUNT = 256;
	/**
	 * task queue
	 * */
	private static final Queue<StorageTask> queue = 
			new ArrayBlockingQueue<StorageTask>(MAX_TASK_COUNT);
	
	public static final StorageTask obtain()
	{
		return queue.poll();
	}
	
	public  static final void free(StorageTask task)
	{
		if(null != task)
			queue.add(task);
	}
	
}
