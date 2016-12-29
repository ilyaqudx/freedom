package lesson2.addtional_1;

import java.io.UnsupportedEncodingException;

public class EncryptFommater extends Formatter {

	public EncryptFommater(Formatter formater) 
	{
		super(formater.text);
	}

	public String encrypt() 
	{
		return encrypt0();
	}
	
	private String encrypt0()
	{
		try 
		{
			return new String(text.getBytes("gbk"),"UTF-8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		return text;
	}
}