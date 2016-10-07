
package freedom.socket;


public class ByteKit {

	public static final int bytes2Short(byte[] buffer)
	{
		int value = 0;
		for (int i = 0; i < 2; i++) 
		{
			value += buffer[i] << (8 - i * 8); 
		}
		return value;
	}
	public static final int bytes2Int(byte[] buffer)
	{
		int value = 0;
		for (int i = 0; i < 4; i++) 
		{
			value += buffer[i] << (24 - i * 8); 
		}
		return value;
	}
	public static final int bytes2Long(byte[] buffer)
	{
		int value = 0;
		for (int i = 0; i < 8; i++) 
		{
			value += buffer[i] << (56 - i * 8); 
		}
		return value;
	}
	public static final String bytes2String(byte[] buffer)
	{
		return new String(buffer);
	}
	
	public static final byte[] short2Bytes(int value)
	{
		byte[] buffer = new byte[2];
		for (int i = 0; i < 2; i++) 
		{
			buffer[i] = (byte) ((value >>> (8 - i * 8)) & 0xFF) ;
		}
		return buffer;
	}
	public static final byte[] int2Bytes(int value)
	{
		byte[] buffer = new byte[4];
		for (int i = 0; i < 4; i++) 
		{
			buffer[i] = (byte) ((value >>> (24 - i * 8)) & 0xFF) ;
		}
		return buffer;
	}
	
	public static final byte[] long2Bytes(long value)
	{
		byte[] buffer = new byte[8];
		for (int i = 0; i < 8; i++) 
		{
			buffer[i] = (byte) ((value >>> (56 - i * 8)) & 0xFF) ;
		}
		return buffer;
	}
	
	public static final byte[] string2Bytes(String value)
	{
		return null == value ? null : value.getBytes();
	}
}
