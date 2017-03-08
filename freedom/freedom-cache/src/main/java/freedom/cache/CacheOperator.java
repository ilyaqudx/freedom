package freedom.cache;

public class CacheOperator {

	private MemCache cache = new MemCache();
	
	public String execute(Command command){
		
		if(command == Command.SET)
		{
			cache.put(command.key, command.value);
		}
		else if(command == Command.GET)
		{
			return cache.get(command.key);
		}
		else if(command == Command.DEL)
		{
			cache.remove(command.key);
		}
		
		return "success";
	}
}
