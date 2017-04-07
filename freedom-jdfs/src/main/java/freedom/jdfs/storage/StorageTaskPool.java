package freedom.jdfs.storage;

import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import freedom.jdfs.LogKit;

public class StorageTaskPool {

	
	/**
	 * max task count
	 * */
	public static final int MAX_TASK_COUNT = 256;
	/**
	 * 每次分配多少task
	 * */
	public static final int ALLOC_COUNT_ONCE = 10;
	
	private int min_buffer_size = 256 * 1024;
	private int max_buffer_size = 256 * 1024;
	private int allocCount = 0;
	public StorageTaskPool()
	{
		
	}
	/**
	 * task queue
	 * */
	private final Queue<StorageTask> queue = 
			new ConcurrentLinkedQueue<StorageTask>();
	
	private void alloc()
	{
		synchronized (queue) {
			if(!queue.isEmpty())
				return;
			if(allocCount >= MAX_TASK_COUNT){
				LogKit.warn(String.format("storage task alloc fail,current active task is full : %d", MAX_TASK_COUNT),this.getClass());
				for (int i = 0; i < ALLOC_COUNT_ONCE; i++) 
				{
					allocCount++;
					StorageTask task = new StorageTask();
					task.size = max_buffer_size;
					task.buffer = ByteBuffer.allocate(max_buffer_size);
					queue.add(task);
				}
			}
		}
	}
	
	
	public StorageTask obtain()
	{
		StorageTask storageTask = queue.poll();
		if(null != storageTask)
			return storageTask;
		alloc();
		return queue.poll();
	}
	
	public void free(StorageTask task)
	{
		if(null != task)
			queue.add(task);
	}
	
	public static void main(String[] args) {
		int block = ((136 + 7) & (~7)) + 1008;
		System.out.println(block);
	}
	
}
