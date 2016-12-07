package lesson1.question4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Application {
	
	
	public static void main(String[] args) 
	{
		int count = 10000;
		List<Salary> salarys = new ArrayList<Salary>(count);
		for (int i = 0; i < count; i++) 
		{
			salarys.add(new Salary(RandomUtils.randomString(5),RandomUtils.nextInt(999996) + 5,RandomUtils.nextInt(100001)));
		}
		
		Collections.sort(salarys, new Comparator<Salary>() {
		
			public int compare(Salary o1, Salary o2)
			{
				return o1.getAllSalary() == o2.getAllSalary() ? 0 :
					o1.getAllSalary() > o2.getAllSalary() ? -1 : 1;
			}
		});
		
		List<Salary> first = salarys.subList(0, 10);
		for (Salary salary : first) 
		{
			System.out.println(String.format("%s总收入 : %d , 基本工资 : %d,奖金 : %d", salary.getName(),salary.getAllSalary(),salary.getBaseSalary(),salary.getBonue()));
		}
	}
}

