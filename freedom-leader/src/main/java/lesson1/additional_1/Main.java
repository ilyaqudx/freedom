package lesson1.additional_1;

import java.util.Random;

public class Main {

	public static void main(String[] args) 
	{
		Salary[] salarys = new Salary[88];
		Random r = new Random();
		for (int i = 0; i < 88; i++) 
		{
			salarys[i] = new Salary("dsjfl" + i, r.nextInt(20001) + 2000, r.nextInt(5001) + 500);
		}
		
		BubbleSort.sort(salarys);
		for (Salary salary : salarys)
		{
			System.out.println(salary);
		}
	}
}
