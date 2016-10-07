package freedom.study.interview.design.build;

public class RenovtionDirector implements Director {

	private Builder moodBuilder = new MoodBuilder();
	private Builder motope = new MotopeBuilder();
	private FloolBuilder floor = new FloolBuilder();
	
	public void build()
	{
		moodBuilder.build();
		motope.build();
		floor.build();
	}
	
	public Product get()
	{
		return new BeautyHouse();
	}

}
