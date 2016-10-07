package freedom.study.interview.design.simplefactory;

public class App {

	public static void main(String[] args) {
		
		b();
	}
	
	private static final void a()
	{
		/**
		 * 简单工厂模式-A
		 * */
		Sender qqSender = SenderFactoryA.newInstance("qq");
		qqSender.send("let go to have launch");
		Sender emailSender = SenderFactoryA.newInstance("email");
		emailSender.send("submit text");
		//传入字符串为类型,如果传入不存在的类型则会返回NULL,会有安全问题
		Sender unknown = SenderFactoryA.newInstance("wx");
		/**
		 * 如果现在要增加一种微信的发送器.需要2步
		 * 1-创建一个WxSender实例(这一步很好,直接进行增加扩展就行了.扩展性很好,不会影响其他的类:符合开闭原则)
		 * 2-在SenderFactory中的newInstance方法中添加if else 条件判断 返回相应的实例(不好,违背了开闭原则,对修改关闭,对扩展开放)
		 */
	}
	
	private static final void b()
	{
		Sender qqSender = SenderFactoryB.buildQQSender();
		Sender emailSender = SenderFactoryB.buildEmailSender();
		qqSender.send("let go to have launch");
		emailSender.send("submit text");
		
		//提供多个方法,直接返回相应的类型,相对较安全
		//如果新增一种发送器,添加相应的SENDER实例,同时只需要在工厂方法中增加相应的方法返回新的实例即可.不会对已有代码进行修改和侵入.
		//虽然也会修改工厂类,相对来说比A模式要好.
	}
}
