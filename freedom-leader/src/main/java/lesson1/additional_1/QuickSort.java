package lesson1.additional_1;

import java.util.List;

public class QuickSort {

	public static final void sort(List<Salary> salarys)
	{
		if(null == salarys || salarys.isEmpty())
			return;
		Salary[] arr = new Salary[salarys.size()];
		salarys.toArray(arr);
		sort(arr, 0, arr.length - 1);
		for (Salary salary : arr)
		{
			System.out.println(salary);
		}
	}
	
	private static final void sort(Salary[] arr,int low,int high)
	{
		if(low < high)
		{
			int pivot = partion(arr,low,high);
			sort(arr, low, pivot -1);
			sort(arr, pivot + 1,high);
		}
	}
	
	private static final int partion(Salary[] arr,int low,int high)
	{
		Salary base = arr[low];
		while(low < high)
		{
			while(arr[high].compareTo(base) != -1 && low < high)
				high--;
			if(low < high)
				arr[low++] = arr[high];
			while(arr[low].compareTo(base) != 1 && low < high)
				low++;
			if(low < high)
				arr[high--] = arr[low];
		}
		arr[low] = base;
		return low;
	}
}
