package freedom.study.interview.design.build;

public class App {

	public static void main(String[] args) {
		Director director = new RenovtionDirector();
		director.build();
		Product house = director.get();

		System.out.println(house);
		
		
		Director appDirector = new AppDirector();
		appDirector.build();
		Product app = appDirector.get();
		
		System.out.println(app);
	}
}
