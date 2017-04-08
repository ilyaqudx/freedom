package freedom.jdfs.storage.dio;

import java.util.concurrent.BlockingQueue;

import freedom.jdfs.storage.StorageTask;

public class StorageDioReadTask extends Thread {

	private BlockingQueue<StorageTask> queue;

	public StorageDioReadTask(String name, BlockingQueue<StorageTask> queue) {
		super(name);
		this.queue = queue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				StorageTask storageTask = queue.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
