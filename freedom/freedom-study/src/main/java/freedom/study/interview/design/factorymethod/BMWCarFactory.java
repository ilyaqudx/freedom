package freedom.study.interview.design.factorymethod;

public class BMWCarFactory implements CarFactory {

	@Override
	public Car build() {
		// TODO Auto-generated method stub
		return new BMWCar();
	}

}
