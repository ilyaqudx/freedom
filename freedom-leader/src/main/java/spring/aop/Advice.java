package spring.aop;

public interface Advice {

	public void before(AopProxy proxy);

	public void after(AopProxy proxy);
}
