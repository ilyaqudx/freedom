package lesson2.addtional_1;

public class UpperFormatter extends Formatter {

	public UpperFormatter(Formatter formatter)
	{
		super(formatter.text);
	}

	
	public String upper()
	{
		return text.toUpperCase();
	}
}
