package freedom.study.interview.design.simplefactory;

/**
 * 
 * */
public class SenderFactoryA {

	public static final Sender newInstance(String type)
	{
		if("qq".equals(type))
			return new QQSender();
		else if("email".equals(type))
			return new EmailSender();
		return null;
	}
}
