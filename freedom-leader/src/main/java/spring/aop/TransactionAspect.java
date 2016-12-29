package spring.aop;

public class TransactionAspect implements Aspect,Advice{

	@Override
	public void before(AopProxy proxy) 
	{
		System.out.println("开启事务...");
	}

	@Override
	public void after(AopProxy proxy) 
	{
		System.out.println("事务结束...");
	}

	
}
