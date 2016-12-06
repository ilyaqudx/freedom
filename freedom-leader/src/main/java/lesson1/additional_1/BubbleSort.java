package lesson1.additional_1;

public class BubbleSort {

	public static final void sort(Salary[] arr)
	{
		int size = arr.length;
		for (int i = 0; i < size - 1; i++) 
		{
			for (int j = i + 1; j < size; j++)
			{
				int p1 = arr[i].count();
				int p2 = arr[j].count();
				if(p1 < p2)
				{
					//交换
					Salary temp = arr[i];
					arr[i] = arr[j];
					arr[j] = temp;
				}
			}
		}
	}
}
