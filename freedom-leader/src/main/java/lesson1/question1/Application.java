package lesson1.question1;

public class Application {

	/**
	 * 
	 *  byte a = 127;
		byte b = a << 2;
		System.out.println(b);
	 * 
	 *  此题第二行有错。因为进行位移后结果是一个int类型,不是byte
	 *  将变量b的类型修改成int即可
	 * */
	public static final void question()
	{
		byte value = 127;
		int  v2    = value << 2;
		System.out.println(v2);
	}
}
