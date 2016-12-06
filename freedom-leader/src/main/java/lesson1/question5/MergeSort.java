package lesson1.question5;

import java.util.Arrays;

public class MergeSort {

	
	public static void main(String[] args) 
	{
		int[] arr = new int[]{34,85,02,58,836,192,44,28,29};
		sort(arr, 0, arr.length-1);
		System.out.println(Arrays.toString(arr));
	}
	
	public static final void sort(int[] arr,int low,int high)
	{
		if(low < high)
		{
			int middle = (low + high) / 2;
			
			sort(arr,low,middle);
			sort(arr,middle+1,high);
			merge(arr,low,high,middle);
		}
	}
	
	private static final void merge(int[] arr,int low,int high,int middle)
	{
		int[] temp = new int[high - low + 1];
		int left  = low;
		int right = middle + 1;
		int count = 0;
		
		while(left <= middle && right <= high)
		{
			if(arr[left] > arr[right])
				temp[count++] = arr[right++];
			else
				temp[count++] = arr[left++];
		}
		
		while(left <= middle)
			temp[count++] = arr[left++];
		while(right <= high)
			temp[count++] = arr[right++];
		
		for (int i = 0; i < count; i++) 
		{
			arr[low++] = temp[i];
		}
	}
}
