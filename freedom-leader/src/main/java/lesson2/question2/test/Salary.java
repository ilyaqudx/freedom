package lesson2.question2.test;

import lesson1.additional_2.ByteArrayHelper;

public class Salary {

	private String name;
	private int salary;
	private int bonus;
	public Salary(String name, int salary, int bonus) {
		super();
		this.name = name;
		this.salary = salary;
		this.bonus = bonus;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	public int getBonus() {
		return bonus;
	}
	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
	public long count()
	{
		return this.salary * 13 + this.bonus;
	}
	public byte[] getBytes()
	{
		byte[] buffer = new byte[7];
		System.arraycopy(name.getBytes(), 0, buffer, 0, 5);
		buffer[5] = (byte) salary;
		buffer[6] = (byte) bonus;
		return buffer;
	}
}
