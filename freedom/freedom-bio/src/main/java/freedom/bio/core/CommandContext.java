package freedom.bio.core;

import java.util.HashMap;
import java.util.Map;

import freedom.bio.moudel.logon.LogonCommand;

public class CommandContext {

	public static final CommandContext I = new CommandContext();
	
	@SuppressWarnings("rawtypes")
	private static final Map<String,Command> commands = new HashMap<String,Command>();

	static
	{
		addCommand(LogonCommand.class,Command.Main.GAME,Command.Sub.ACCOUNT_LOGON);
	}
	
	@SuppressWarnings("rawtypes")
	private static final void addCommand(Class<? extends Command> clazz,short main,short sub)
	{
		try 
		{
			commands.put(String.format("%d%d", main,sub),clazz.newInstance());
		} 
		catch (InstantiationException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
	
	public Command getCommand(short main,short sub)
	{
		return commands.get(String.format("%d%d", main,sub));
	}
}
