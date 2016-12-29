package lesson2.addtional_1;

public class LowerFormatter extends Formatter {

	public LowerFormatter(Formatter formatter) {
		super(formatter.text);
	}

	
	public String toLower()
	{
		return text.toLowerCase();
	}
}
