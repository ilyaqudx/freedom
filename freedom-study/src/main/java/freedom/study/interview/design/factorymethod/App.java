package freedom.study.interview.design.factorymethod;

public class App {

	public static void main(String[] args) {
		Car benz = new BenzCarFactory().build();
		Car bmw  = new BMWCarFactory().build();
		Car audi = new AudiCardFactory().build();
		
		System.out.println(benz);
		System.out.println(bmw);
		System.out.println(audi);
	}
}
