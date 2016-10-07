package freedom.study.interview.design.singleton;

public class SingletonB {

	private static SingletonB instance = null;
	
	private SingletonB(){}
	
	public static final SingletonB getInstance()
	{
		if(null == instance)
		{
			System.out.println("ready instance SingletonB");
			synchronized (SingletonB.class) {
				if(null == instance)
				{
					System.err.println("building singletonB");
					instance = new SingletonB();
				}
			}
		}
		return instance;
	}
}
