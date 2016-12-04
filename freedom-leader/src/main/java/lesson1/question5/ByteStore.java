package lesson1.question5;

public class ByteStore {

	//当前存放MyItem数量
	private int size;
	//最大长度
	private int maxSize = 1000;
	//实际存放MyItem的字节数组
	private byte[] storeByteArray = new byte[1000 * 3];
	
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
		storeByteArray[start] = item.getType();
		storeByteArray[start+1] = item.getColor();
		storeByteArray[start+2] = item.getPrice();
		++size;
	}
	
	public MyItem getMyItem(int index)
	{
		if(index < 0 || index > maxSize - 1)
			throw new IllegalArgumentException("index must in 0 - 999");
		int start = index * 3;
		return new MyItem(storeByteArray[start], storeByteArray[start+1], storeByteArray[start+2]);
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
				int p1 = storeByteArray[i * 3 + 2];
				int p2 = storeByteArray[j * 3 + 2];
				if(p1 > p2)
				{
					//交换
					byte tempType  = storeByteArray[i * 3 + 0];
					byte tempColor = storeByteArray[i * 3 + 1];
					byte tempPrice = storeByteArray[i * 3 + 2];
					storeByteArray[i * 3 + 0] = storeByteArray[j * 3 + 0];
					storeByteArray[i * 3 + 1] = storeByteArray[j * 3 + 1];
					storeByteArray[i * 3 + 2] = storeByteArray[j * 3 + 2];
					storeByteArray[j * 3 + 0] = tempType;
					storeByteArray[j * 3 + 1] = tempColor;
					storeByteArray[j * 3 + 2] = tempPrice;
				}
			}
		}
	}
}
