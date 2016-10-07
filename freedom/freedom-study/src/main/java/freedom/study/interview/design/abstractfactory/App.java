package freedom.study.interview.design.abstractfactory;

public class App {

	
	public static void main(String[] args) 
	{
		
		/**
		 * 抽象工厂模式
		 * */
		Codec codec = new ProtobufCodec();
		codec.getDecoder().decode(null);
		codec.getEncoder().encod(null);
		
		/**
		 * 此工厂模式相当于一个复合工厂模式,可以生产多种产品,但多种产品之间是属性同一个物体的一部分,即多种产品间是有关联性的,都是部件.
		 * 
		 * */
	}
}
