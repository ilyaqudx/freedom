package freedom.study.interview.design.singleton;

public class App {

	public static void main(String[] args) {
		
		
		for (int i = 0; i < 10; i++)
		{
			new Thread(new Runnable() {
				public void run() {
					for (int j = 0; j < 10; j++)
					{
						SingletonB s = SingletonB.getInstance();
								System.out.println(s);
					}
				}
			}).start();
		}
		
	}
}
