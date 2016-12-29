package lesson2.question4.test2;

public class SalaryStore {

	private int size;
	private int max;
	private byte[] bytes;
	
	public SalaryStore(int max)
	{
		this.max = max > 0 ? max : Integer.MAX_VALUE;
		this.bytes = new byte[max * 13];
	}

	public boolean add(Salary salary)
	{
		if(null == salary || max == size)
			return false;
		byte[] data = salary.getBytes();
		System.arraycopy(data, 0, bytes, size * 13, 13);
		size ++;
		return true;
	}
	
	public byte[] getBytes()
	{
		return this.bytes;
	}
}
