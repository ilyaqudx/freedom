package lesson1.additional_1;

import java.text.DecimalFormat;
import java.util.Random;

import lesson1.question4.RandomUtils;

public class Application {

	public static void main(String[] args) 
	{
		int count = 10000;
		
		Random random = new Random();
		Salary[] salarys = new Salary[count];
		for (int i = 0; i < count; i++) 
		{
			salarys[i] = new Salary(RandomUtils.randomString(5),random.nextInt(999996) + 5,random.nextInt(100001));
			System.out.println(salarys[i]);
		}
		
		QuickSort.sort(salarys);
		
		DecimalFormat decimalFormat=new DecimalFormat(".0000");
		for (int i = 0; i < 100; i++) 
		{
			Salary salary = salarys[i];
			String all = decimalFormat.format((float)salary.getAllSalary() / 10000);
			System.out.println(String.format("%s : %s 万", salary.getName(),all));
		}
	}
}
