package freedom.study.interview.design.simplefactory;

public class EmailSender implements Sender {

	@Override
	public void send(String message) 
	{
		System.out.println(String.format("send email message : %s", message));
	}

}
