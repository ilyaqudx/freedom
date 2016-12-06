package lesson1.additional_2;

import lesson1.question5.MyItem;

public class IntStore {

	//当前存放MyItem数量
	private int size;
	//最大长度
	private int maxSize = 1000;
	//实际存放MyItem的字节数组
	private int[] storeByteArray = new int[1000];
	
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
		storeByteArray[index] = ByteArrayHelper.writeInt(new byte[]{item.getType(),item.getColor(),item.getPrice(),0});
		++size;
	}
	
	public MyItem getMyItem(int index)
	{
		if(index < 0 || index > maxSize - 1)
			throw new IllegalArgumentException("index must in 0 - 999");
		int value = storeByteArray[index];
		byte[] bytes = ByteArrayHelper.getBytes(value);
		return new MyItem(bytes[0], bytes[1], bytes[2]);
	}
	
	public int size()
	{
		return this.size;
	}
	
	public void sort()
	{
		for (int i = 0; i < size - 1; i++) 
		{
			for (int j = i + 1; j < size; j++)
			{
				int p1 = storeByteArray[i];
				int p2 = storeByteArray[j];
				byte[] bytes1 = ByteArrayHelper.getBytes(p1);
				byte[] bytes2 = ByteArrayHelper.getBytes(p2);
				if(bytes1[2] < bytes2[2])
				{
					//交换
					storeByteArray[i] = p2;
					storeByteArray[j] = p1;
				}
			}
		}
	}
}
