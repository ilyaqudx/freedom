package lesson1.sort;

public class MergeSorter implements Sorter {

	@Override
	public void sort(int[] arr) 
	{
		if(null == arr || arr.length == 0)
			throw new IllegalArgumentException("arr is null or length is 0");
		sort0(arr,0,arr.length - 1);
	}
	
	public void sort0(int[] arr,int low,int high)
	{
		if(low < high)
		{
			int middle = (high + low) / 2;
			sort0(arr,low,middle);
			sort0(arr,middle + 1,high);
			merge(arr,low,high,middle);
		}
	}

	private void merge(int[] arr,int low,int high,int middle)
	{
		int[] temp = new int[high - low + 1];
		int   left = low;
		int  right = middle + 1;
		int  index = 0;
		while(left <= middle && right <= high)
		{
			if(arr[left] > arr[right])
				temp[index++] = arr[right++];
			else
				temp[index++] = arr[left++];
		}
		
		while(left <= middle)
			temp[index++] = arr[left++];
		while(right <= high)
			temp[index++] = arr[right++];
		
		for (int i = 0; i < index; i++) 
		{
			arr[low++] = temp[i];
		}
	}
}
