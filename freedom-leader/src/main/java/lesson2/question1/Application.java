package lesson2.question1;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class Application {

	public static void main(String[] args) throws IOException
	{
		String str = "中国";
		
		byte[] utf8 	  = str.getBytes("UTF-8");
		byte[] gbk  	  = str.getBytes("GBK");
		byte[] iso_8859_1 = str.getBytes("ISO-8859-1");
		
		System.out.println("UTF-8 : " + Arrays.toString(utf8));
		System.out.println("GBK   : " + Arrays.toString(gbk));
		System.out.println("ISO-8859-1 : " + Arrays.toString(iso_8859_1));
		System.out.println("A".getBytes("GBK").length);
		
		
		String utf8Str    = unicode2UTF8("中国化地都是大小写进水膛上Hello ABC !!++");
		String unicodeStr = utf82Unicode(utf8Str);
		System.out.println("unicode ->   utf-8 : " + utf8Str);
		System.out.println("utf-8   -> unicode : " + unicodeStr);
	}
	//unicode转UTF-8
	public static final String unicode2UTF8(String str) throws IOException
	{
		int index = 0;
		byte[] utf8 = new byte[str.length() * 3];
		char[] cs = str.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			int c = cs[i];
			if (c >= 0x0000 && c <= 0x007f)  {
				//英文字符1个字节
	            utf8[index++] = (byte)(c & 0x7F);
	        }  
	        else if (c >= 0x0080 && c <= 0x07ff)  {   
	        	//其它字符2个字节
	            utf8[index++] = (byte)((c >> 6) & 0x1f);
	            utf8[index++] = (byte)(c & 0x3f);
	        }  
	        else if (c >= 0x0800 && c <= 0xffff)  
	        {   //汉字3个字节
	            utf8[index++] = (byte)(0xE0 | ((c >> 12) & 0x0f));
	            utf8[index++] = (byte)(0x80 | ((c >> 6 ) & 0x3f));
	            utf8[index++] = (byte)(0x80 | (c & 0x3f));
	        }  
		}
		return new String(utf8,0,index,"UTF-8");
	}
	//utf-8 转 unicode
	public static final String utf82Unicode(String str) throws IOException{
		int index = 0,newLen = 0;
		byte[] unicode = new byte[str.length() * 2];
		byte[] utf8 = str.getBytes("UTF-8");
		while(index < utf8.length){
			if((utf8[index] & 0x80) == 0){ //如果二进制以0开头，则UTF-8用一个字节存储
				unicode[newLen++] = 0;
				unicode[newLen++] = utf8[index++];
			}
			else if((utf8[index] & 0xE0) == 0xC0){//如果2进制以110开头,则UTF-8用2字节存储
				int a = utf8[index++] << 6 & 0x07C0;
				int b = utf8[index++] & 0x3f; 
				int c = a | b;
				unicode[newLen++] = (byte)(c >> 8);
				unicode[newLen++] = (byte) c;
			}
			else if((utf8[index] & 0xF0) == 0xE0){//如果2进制以1110开头,则UTF-8用3个字节存储，汉字占3个字节
				int a = utf8[index++] << 12 & 0xf000;
				int b = utf8[index++] << 6  & 0x0ff3;
				int c = utf8[index++] & 0x3f;
				int d = a | b | c;
				unicode[newLen++] = (byte)(d >> 8);
				unicode[newLen++] = (byte) d;
			}
		}
		return new String(unicode,0,newLen,"unicode");
	}
}
