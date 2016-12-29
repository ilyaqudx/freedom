package lesson2.question2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Application {

	
	public static void main(String[] args) throws Exception 
	{
		int a = 10240;
		
		System.out.println(Integer.toBinaryString(a));
		System.out.println(Arrays.toString(getBytesBig(a, 4)));
		System.out.println(readIntBig(getBytesBig(a, 4), 4));
		System.out.println(Arrays.toString(getBytesSmall(a, 4)));
		System.out.println(readIntSmall(getBytesSmall(a, 4), 4));
		
		Files.write(Paths.get("D:/big.txt"), getBytesBig(a, 4));
		Files.write(Paths.get("D:/small.txt"), getBytesSmall(a, 4));
		int big = readIntBig(Files.readAllBytes(Paths.get("d:/big.txt")), 4);
		int sma = readIntSmall(Files.readAllBytes(Paths.get("d:/small.txt")),4);
		System.out.println("big = " + big);
		System.out.println("small = " + sma);
	}
	
	//大端存储方式  转字节数组
	public static final byte[] getBytesBig(int value , int len)
	{
		byte[] bytes = new byte[len];
		for (int i = 0; i < len; i++) 
		{
			bytes[i] = (byte)(value >> ((len - 1 - i) << 3));
		}
		return bytes;
	}
	//大端方式读取
	public static final int readIntBig(byte[] bytes,int len)
	{
		int value = 0;
		for (int i = 0; i < len; i++) 
		{
			value += (bytes[i] & 0xff) << ((len - 1 - i) << 3);
		}
		return value;
	}
	//小端  int转字节数组
	public static final byte[] getBytesSmall(int value , int len)
	{
		byte[] bytes = new byte[len];
		for (int i = 0; i < len; i++) 
		{
			bytes[i] = (byte)(value >> (i << 3));
		}
		return bytes;
	}
	//小端  bytes 转 int
	public static final int readIntSmall(byte[] bytes,int len)
	{
		int value = 0;
		for (int i = 0; i < len; i++) 
		{
			value += ((bytes[i] & 0xff) << (i << 3));
		}
		return value;
	}
}
