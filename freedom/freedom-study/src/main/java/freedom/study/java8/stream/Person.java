package freedom.study.java8.stream;

public class Person {

	private long id;
	private String name;
	private int age;
	private int sex;
	
	public Person(long id, String name, int age, int sex)
	{
		this.id = id;
		this.name = name;
		this.age = age;
		this.sex = sex;
	}
	
	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", age=" + age + ", sex=" + sex + "]";
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
}
