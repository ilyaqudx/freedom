package freedom.study.interview.design.singleton;

public class SingletonC {

	private SingletonC()
	{
		
	}
	
	public static final SingletonC getInstance()
	{
		return SingletonCHolder.instance;
	}
	
	private static final class SingletonCHolder
	{
		private static final SingletonC instance = new SingletonC();
	}
}
