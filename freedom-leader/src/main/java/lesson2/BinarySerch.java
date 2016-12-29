package lesson2;

public class BinarySerch {

	
	public static void main(String[] args) 
	{
		int[] arr = new int[]{3,5,77,88,99,101,133};
		int index = search(arr,0,arr.length,101);
		System.out.println(index);
	}
	
	public static final int search(int[] a, int fromIndex, int toIndex,
            int key)
	{
		int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = a[mid];

            if (midVal < key)
                low = mid + 1;
            else if (midVal > key)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found.
	}
}
