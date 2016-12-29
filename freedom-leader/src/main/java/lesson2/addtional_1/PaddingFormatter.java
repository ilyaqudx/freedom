package lesson2.addtional_1;

import java.util.Arrays;

public class PaddingFormatter extends Formatter 
{
	public PaddingFormatter(Formatter formatter) 
	{
		super(formatter.text);
	}

	public String padding()
	{
		int len = text.length();
		if(len < 10)
		{
			byte[] padding = new byte[10 - len];
			Arrays.fill(padding, (byte)33);
			return text + new String(padding);
		}
		return len == 10 ? text : text.substring(0,10);
	}
	
	public static void main(String[] args) {
		System.out.println((int)'!');
	}
	
	
}
