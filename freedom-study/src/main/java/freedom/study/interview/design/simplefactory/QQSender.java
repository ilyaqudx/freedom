package freedom.study.interview.design.simplefactory;

public class QQSender implements Sender {

	@Override
	public void send(String message) 
	{
		System.out.println(String.format("send qq message : %s", message));
	}

}
