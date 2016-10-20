package freedom.study.interview.design.factorymethod;

public class AudiCardFactory implements CarFactory {

	@Override
	public Car build() {
		// TODO Auto-generated method stub
		return new AudiCar();
	}

}
