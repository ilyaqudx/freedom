package lesson2.question2.test;

public class SalaryStore {

	private int size;
	private int max;
	private byte[] bytes;
	
	public SalaryStore(int max)
	{
		this.max = max > 0 ? max : Integer.MAX_VALUE;
		this.bytes = new byte[max * 7];
	}

	public boolean add(Salary salary)
	{
		if(null == salary || max == size)
			return false;
		byte[] data = salary.getBytes();
		System.arraycopy(data, 0, bytes, size * 7, 7);
		size ++;
		return true;
	}
	
	public byte[] getBytes()
	{
		return this.bytes;
	}
}
