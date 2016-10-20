package freedom.study.interview.design.singleton;

public class SingletonA {

	private static final SingletonA instance = new SingletonA();
	
	private SingletonA()
	{
		
	}
	
	public static final SingletonA getInstance()
	{
		return instance;
	}
}
