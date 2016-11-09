package freedom.bio.core;

import java.util.HashMap;
import java.util.Map;

import freedom.bio.moudel.logon.LogonCommand;

public class CommandContext {

	public static final CommandContext I = new CommandContext();
	
	@SuppressWarnings("rawtypes")
	private static final Map<Class<? extends Command>,Command> commands = new HashMap<Class<? extends Command>,Command>();

	static
	{
		addCommand(LogonCommand.class);
	}
	
	@SuppressWarnings("rawtypes")
	private static final void addCommand(Class<? extends Command> clazz)
	{
		try 
		{
			commands.put(clazz,clazz.newInstance());
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
	
	public Command getCommand(Class<? extends Command> command)
	{
		return commands.get(command);
	}
}
