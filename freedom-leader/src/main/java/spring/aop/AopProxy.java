package spring.aop;

import java.lang.reflect.InvocationHandler;

public interface AopProxy extends InvocationHandler {

	public Object getProxy();
	
	public void addAdvice(Advice advice);
}
