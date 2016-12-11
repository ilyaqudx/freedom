package lesson2.question2.test2;

public class Group implements Comparable<Group>{

	public String name;
	public long salary;
	public int  count;
	public Group(String name, long salary, int count) {
		super();
		this.name = name;
		this.salary = salary;
		this.count = count;
	}
	@Override
	public String toString() 
	{
		return "Group [name=" + name + ", salary=" + salary + ", count="
				+ count + "]";
	}
	@Override
	public int compareTo(Group o)
	{
		return salary == o.salary ? 0 
				: salary > o.salary ? -1 : 1;
	}
	
}
