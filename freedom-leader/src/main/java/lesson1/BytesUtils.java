package lesson1;

public class BytesUtils {

	public static final int readInt(byte[] bytes)
	{
		int value = 0;
		for (int i = 0; i < 4; i++) 
		{
			value += (bytes[i] & 0xFF) << (3 - i) * 8;
		}
		return value;
	}
}
