package spring.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class JdkAopProxy implements AopProxy , InvocationHandler {

	private Object target;
	private Set<Advice> advices = new HashSet<Advice>();
	public JdkAopProxy(Object target)
	{
		if(target == null)
			throw new NullPointerException("target is null");
		this.target = target;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)throws Throwable 
	{
		for (Advice advice : advices) 
		{
			advice.before(this);
		}
		Object value = method.invoke(target, args);
		for (Advice advice : advices)
		{
			advice.after(this);
		}
		return value;
	}

	@Override
	public Object getProxy()
	{
		return this;
	}

	@Override
	public void addAdvice(Advice advice)
	{
		if(advice == null)
			throw new NullPointerException("advice is null");
		advices.add(advice);
	}

}
