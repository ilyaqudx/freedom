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
			return null == cache.remove(command.key) ? "0" : "1";
		}
		
		return "1";
	}
}
