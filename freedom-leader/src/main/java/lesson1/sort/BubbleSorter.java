package lesson1.sort;

public class BubbleSorter implements Sorter {

	@Override
	public void sort(int[] arr)
	{
		if(null == arr || arr.length == 0)
			throw new IllegalArgumentException("arr is null or length is 0");
		for (int i = 0; i < arr.length - 1; i++) 
		{
			for (int j = i + 1; j < arr.length; j++) 
			{
				int a = arr[i];
				int b = arr[j];
				if(a > b)
				{
					int t  = a;
					arr[i] = b;
					arr[j] = t;
				}
			}
		}
	}

}
