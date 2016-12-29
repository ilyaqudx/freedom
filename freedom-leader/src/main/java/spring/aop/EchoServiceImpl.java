package spring.aop;

public class EchoServiceImpl implements EchoService {

	@Override
	public String echo(String text)
	{
		System.out.println("echo text : " + text);
		return text;
	}

}
