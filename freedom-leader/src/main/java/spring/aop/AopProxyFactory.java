package spring.aop;

import java.lang.reflect.Proxy;

public class AopProxyFactory {

	@SuppressWarnings("unchecked")
	public static final <T> T getProxy(T entity) throws IllegalArgumentException, InstantiationException, IllegalAccessException
	{
		AopProxy aop = new JdkAopProxy(entity);
		aop.addAdvice(new TransactionAspect());
		Class<?>[] classes = entity.getClass().getInterfaces();
		T proxy = (T) Proxy.newProxyInstance(entity.getClass().getClassLoader(),classes,aop);
		System.out.println(proxy.getClass());
		
		return proxy;
	}
}
