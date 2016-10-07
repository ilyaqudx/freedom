package freedom.study.interview.concurrent;

public class ABCThreadSeqRun {

	public static void main(String[] args) throws InterruptedException 
	{
		Thread a = new Thread(new Worker("A"));
		Thread b = new Thread(new Worker("B"));
		Thread c = new Thread(new Worker("C"));
		
		a.start();
		a.join();
		b.start();
		b.join();
		c.start();
		c.join();
	}
	
	static class Worker implements Runnable
	{
		private String name;
		public Worker(String name)
		{
			this.name = name;
		}
		
		public void run() 
		{
			System.out.println(name);
		}
	}
}
