package lesson1.additional_2;

import java.util.Arrays;

public class ByteArrayHelper {

	public static void main(String[] args) 
	{
		int a = -102464646;
		byte[] bytes = getBytes(a);
		System.out.println(Arrays.toString(bytes));
		int value = writeInt(bytes);
		System.out.println(value);
	}
	
	public static final byte[] getBytes(int value)
	{
		int count = 4;
		byte[] buffer = new byte[count];
		for (int i = 0; i < count; i++) 
		{
			buffer[i] = (byte)(value >>> (3 - i) * 8);
		}
		return buffer;
	}
	
	public static final byte[] getBytes(long value)
	{
		int count = 8;
		byte[] buffer = new byte[count];
		for (int i = 0; i < count; i++) 
		{
			buffer[i] = (byte)(value >>> (7 - i) * 8);
		}
		return buffer;
	}
	
	public static final int writeInt(byte[] bytes)
	{
		int value = 0;
		for (int i = 0; i < 4; i++) 
		{
			value += (bytes[i] & 0xff) << (3 - i) * 8;
		}
		return value;
	}
	public static final long writeLong(byte[] bytes)
	{
		long value = 0;
		for (int i = 0; i < 8; i++) 
		{
			value += (bytes[i] & 0xff) << (7 - i) * 8;
		}
		return value;
	}
}
