package lesson1.additional_1;

public class Salary implements Comparable<Salary>{

	private String name;
	private int baseSalary;
	private int bouns;
	public Salary(String name, int baseSalary, int bouns) {
		this.name = name;
		this.baseSalary = baseSalary;
		this.bouns = bouns;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getBaseSalary() {
		return baseSalary;
	}
	public void setBaseSalary(int baseSalary) {
		this.baseSalary = baseSalary;
	}
	public int getBouns() {
		return bouns;
	}
	public void setBouns(int bouns) {
		this.bouns = bouns;
	}
	@Override
	public String toString() {
		return "Salary [name=" + name + ",count = " +getAllSalary()+ ", baseSalary=" + baseSalary + ", bouns=" + bouns + "]";
	}
	public int getAllSalary()
	{
		return baseSalary * 13 + bouns;
	}
	@Override
	public int compareTo(Salary o)
	{
		int c1 = getAllSalary();
		int c2 = o.getAllSalary();
		return c1 == c2 ? 0 : c1 > c2 ? -1 : 1;
	}
}
