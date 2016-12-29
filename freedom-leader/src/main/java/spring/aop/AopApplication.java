package spring.aop;

public class AopApplication {

	public static void main(String[] args) throws Exception 
	{
		EchoService service = AopProxyFactory.getProxy(new EchoServiceImpl());
		
		
		service.echo("hello world");
	}
}
