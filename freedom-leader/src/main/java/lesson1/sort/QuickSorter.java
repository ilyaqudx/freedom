package lesson1.sort;

public class QuickSorter implements Sorter {

	@Override
	public void sort(int[] arr)
	{
		if(null == arr || arr.length == 0)
			throw new IllegalArgumentException("arr is null or length is 0");
		quicksort(arr, 0, arr.length - 1);
	}
	
	private void quicksort(int[] arr,int low,int high)
	{
		if(low <  high)
		{
			int middle = partion(arr,low,high);
			//left
			quicksort(arr, low, middle - 1);
			//right
			quicksort(arr, middle + 1,high);
		}
	}

	private int partion(int[] arr, int low, int high)
	{
		int base = arr[low];
		while(low < high)
		{
			while(arr[high] >= base && low < high)
				high--;
			if(low < high)
				arr[low++] = arr[high];
			while(arr[low] <= base && low < high)
				low++;
			if(low < high)
				arr[high--] = arr[low];
		}
		arr[low] = base;
		return low;
	}

}
