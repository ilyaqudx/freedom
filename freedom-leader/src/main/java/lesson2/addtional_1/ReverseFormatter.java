package lesson2.addtional_1;


public class ReverseFormatter extends Formatter {

	public ReverseFormatter(Formatter formatter) {
		super(formatter.text);
	}

	public String reverse()
	{
		char[] cs = text.toCharArray();
		char[] ts = new char[cs.length];
		int max   = cs.length - 1;
		int index = 0;
		for (int i = max; i >= 0; i--) 
		{
			ts[index++] = cs[i];
		}
		return new String(ts);
	}
}
