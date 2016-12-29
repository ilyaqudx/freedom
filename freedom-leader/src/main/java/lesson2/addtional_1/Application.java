package lesson2.addtional_1;

import java.util.Scanner;

public class Application {

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
			System.out.println("输入要操作的字符串");
			String text = reader.next();
			System.out.println("请选择对字符串 : " + text + " 的操作\r\n");
			System.out.println(sb.toString());
			String cmd  = reader.next();
			handleCmd(text, cmd);
		}
	}

	private static void handleCmd(String text, String cmd)
	{
		if("1".equals(cmd)){
			System.out.println(new EncryptFommater(new Formatter(text)).encrypt());
		}else if("2".equals(cmd))
			System.out.println(new ReverseFormatter(new Formatter(text)).reverse());
		else if("3".equals(cmd))
			System.out.println(new UpperFormatter(new Formatter(text)).upper());
		else if("4".equals(cmd))
			System.out.println(new LowerFormatter(new Formatter(text)).toLower());
		else if("5".equals(cmd))
			System.out.println(new PaddingFormatter(new Formatter(text)).padding());
		else if(cmd.indexOf("6") == 0)
		{
			String   cmds = cmd.substring(1);
			String[] arr  = cmds.split(",");
			for (String s : arr) 
			{
				if(null != s && !"".equals(s))
					handleCmd(text, s.trim());
			}
		}
		else 
			System.out.println("命令有误!!!重新输入执行的操作");
	}
}
