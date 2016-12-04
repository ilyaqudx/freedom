package freedom.study.bit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ByteOperator {

	
	public static final void main(String[] args) throws IOException
	{
		byte a = 127;
		byte b = (byte)129;
		System.out.println(a + b);
		
		System.out.println("------------------分隔线----------------");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		out.writeInt(654654965);
		
		/*int value = Integer.MAX_VALUE + 500;
		System.out.println("value : " + value);
		byte[] bytes = getBytes(value);*/
		byte[] bytes = baos.toByteArray();
		System.out.println(getInt(bytes));
		
		System.out.println("------------------分隔线----------------");
		
		ByteArrayInputStream bais = new ByteArrayInputStream(getBytes(913456));
		DataInputStream dis = new DataInputStream(bais);
		System.out.println(dis.readInt());
		
		
		//-------验证负数byte 直接用int接收转为正数的方案---------------
		int ff = -1024;
		System.out.println(ff & 0xffffffffl);
		short sss = (short) 32769;
		System.out.println(sss);
		int   sssi = sss & 0xffff;
		System.out.println(sssi);
	}
	
	
	public static final byte[] getBytes(int value)
	{
		byte[] bytes = new byte[4];
		for (int i = 0; i < 4; i++) 
		{
			bytes[i] = (byte)(value >>> (3 - i) * 8);
		}
		return bytes;
	}
	
	public static final int getInt(byte[] bytes)
	{
		int value = 0;
		for (int i = 0; i < bytes.length; i++) 
		{
			value += (bytes[i] & 0xff) << ((3-i) * 8);
		}
		return value;
	}
	
	/**
	 * 获取无符号byte值
	 * */
	public static final int getUnsingedByte(byte value)
	{
		return value & 0xff;
	}
}
