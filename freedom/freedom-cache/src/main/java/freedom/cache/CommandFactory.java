package freedom.cache;

public class CommandFactory {

	public static final Command buildSet(String key,String value)
	{
		Command command = Command.SET;
		command.key     = key;
		command.value   = value;
		return command;
	}
	
	public static final Command buildGet(String key)
	{
		Command command = Command.GET;
		command.key     = key;
		return command;
	}
	
	public static final Command buildDel(String key)
	{
		Command command = Command.DEL;
		command.key     = key;
		return command;
	}
}
