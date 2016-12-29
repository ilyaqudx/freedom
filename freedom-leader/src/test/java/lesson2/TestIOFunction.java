package lesson2;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class TestIOFunction {

	public static final String NEW_LINE = "\r\n";
	
	public static void main(String[] args) 
	{
		StringBuffer sb = new StringBuffer();
		sb.append("输入要执行的功能 : ").append(NEW_LINE)
		.append("1 : 加密").append(NEW_LINE)
		.append("2 : 反转字符串").append(NEW_LINE)
		.append("3 : 转成大写").append(NEW_LINE)
		.append("4 : 转成小宝").append(NEW_LINE)
		.append("5 : 扩展或者裁剪到10个字符,不足部分用 ！填充").append(NEW_LINE)
		.append("6 : 可执行多个命令.命令之间用,隔开");
		
		Scanner reader=new Scanner(System.in);
		while(true)
		{
			String text = reader.next();
			System.out.println("请选择对字符串 : " + text + " 的操作\r\n");
			System.out.println(sb.toString());
			String cmd  = reader.next();
			if("1".equals(cmd)){
				System.out.println("加密");
			}else if("2".equals(cmd))
				System.out.println("反转字符串");
			else if("3".equals(cmd))
				System.out.println("转成大写");
		}
	}
	
	public static final void testByteArrayInputStream()
	{
		byte[] buf = "STRING".getBytes();
		ByteArrayInputStream bais = new ByteArrayInputStream(buf);
		int a = bais.read();
		System.out.println((char)a);
	}
}
