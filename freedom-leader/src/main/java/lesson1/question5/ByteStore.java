package lesson1.question5;

import java.util.Arrays;

public class ByteStore {

	//当前存放MyItem数量
	private int size;
	//最大长度
	private int maxSize = 1000;
	//实际存放MyItem的字节数组
	private byte[] storeByteArray = new byte[maxSize * 3];
	
	/**
	 * @param index item start pos  ,range 0 - 999
	 * @throws Exception 
	 * */
	public void putMyItem(int index,MyItem item) throws Exception
	{
		if(index < 0 || index > maxSize - 1)
			throw new IllegalArgumentException("index must in 0 - 999");
		if(null == item)
			throw new IllegalArgumentException("item can not is null");
		int start = index * 3;
		storeByteArray[start]   = item.getType();
		storeByteArray[start+1] = item.getColor();
		storeByteArray[start+2] = item.getPrice();
		++size;
	}
	
	public MyItem getMyItem(int index)
	{
		if(index < 0 || index > maxSize - 1)
			throw new IllegalArgumentException("index must be in 0 - 999");
		int start = index * 3;
		return new MyItem(storeByteArray[start], storeByteArray[start+1], storeByteArray[start+2]);
	}
	
	public int size()
	{
		return this.size;
	}
	
	public void sort()
	{
		sort0(0, size - 1);
	}
	
	private void sort0(int low,int high)
	{
		if(low < high)
		{
			int pviot = partion(low,high);
			sort0(low, pviot - 1);
			sort0(pviot + 1, high);
		}
	}
	
	private int partion(int low,int high)
	{
		byte[] base = copy(low);
		while(low < high)
		{
			while(storeByteArray[high * 3 + 2] <= base[2] && low < high)
				high--;
			if(low < high)
			{
				swap(high, low);
				low ++;
			}
			while(storeByteArray[low * 3 + 2] >= base[2] && low < high)
				low++;
			if(low < high)
			{
				swap(low, high);
				high --;
			}
		}
		System.arraycopy(base, 0, storeByteArray, low * 3, 3);
		return low;
	}
	
	private byte[] copy(int index)
	{
		return Arrays.copyOfRange(storeByteArray, index * 3, index * 3 + 3);
	}
	
	private void swap(int fromIndex,int toIndex)
	{
		byte[] temp = copy(fromIndex);
		System.arraycopy(temp, 0, storeByteArray, toIndex * 3, 3);
	}
}
