package freedom.study.interview.design.simplefactory;

public class SenderFactoryB {

	public static final Sender buildQQSender()
	{
		return new QQSender();
	}
	
	public static final Sender buildEmailSender()
	{
		return new EmailSender();
	}
}
