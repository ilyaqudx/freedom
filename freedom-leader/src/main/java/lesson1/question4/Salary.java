package lesson1.question4;

public class Salary {
	private String name;
	private int baseSalary;
	private int bonue;

	public Salary(String name, int baseSalary, int bonue) {
		this.name = name;
		this.baseSalary = baseSalary;
		this.bonue = bonue;
	}

	@Override
	public String toString() {
		return "Salary [name=" + name + ", baseSalary=" + baseSalary
				+ ", bonue=" + bonue + "]";
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

	public int getBonue() {
		return bonue;
	}

	public void setBonue(int bonue) {
		this.bonue = bonue;
	}

	public int getAllSalary() {
		return baseSalary * 13 + bonue;
	}
}