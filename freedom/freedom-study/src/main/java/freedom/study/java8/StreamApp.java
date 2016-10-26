package freedom.study.java8;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StreamApp {

	public static void main(String[] args)
	{
		/*List<User> users = new ArrayList<StreamApp.User>()
		{
			{
				add(new User(1, "zhangsan", "", 3));
				add(new User(2, "张三", "", 2));
				add(new User(3, "李四", "", 3));
			}
		};
		
		List<String> names = users.stream().filter(user -> 2 == user.getAge())
		.map(User::getName)
		.collect(Collectors.toList());
		
		for (String string : names) {
			System.out.println(string);
		}*/
	}
	
	static class User{
		
		
		public User(int id, String name, String password, int age)
		{
			this.id = id;
			this.name = name;
			this.password = password;
			this.age = age;
		}
		private int id;
		private String name;
		private String password;
		private int age;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		@Override
		public String toString() {
			return "User [id=" + id + ", name=" + name + ", password=" + password + ", age=" + age + "]";
		}
	}
}
