package freedom.study.concurrent.future;

public class FutureMain {

	public static void main(String[] args) 
	{
		String userName = new UserServiceProxy().getUserName(110);
		
		System.out.println(userName);
	}
}
