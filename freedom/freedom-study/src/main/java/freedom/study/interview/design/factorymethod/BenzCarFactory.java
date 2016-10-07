package freedom.study.interview.design.factorymethod;

public class BenzCarFactory implements CarFactory {

	@Override
	public Car build() {
		// TODO Auto-generated method stub
		return new BenzCar();
	}

}
