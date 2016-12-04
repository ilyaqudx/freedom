package freedom.study.concurrent.future;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import freedom.study.concurrent.masterwork.Master;
import freedom.study.concurrent.masterwork.Task;
import freedom.study.concurrent.masterwork.Worker;

public class MasterWorkerMain {

	public static void main(String[] args) {
		
		List<Task> taskList = new ArrayList<Task>();
		Random r = new Random();
		for (int i = 0; i < 100; i++) {
			Task t = new Task();
			t.setWage(r.nextInt(17500) + 2500);
			taskList.add(t);
		}
		Master master = new Master(new Worker(), 10);
		master.submit(taskList);
		int avg = master.execute();
		System.out.println(avg);
	}
}
