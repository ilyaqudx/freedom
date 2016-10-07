package freedom.nio;

import java.util.List;

import freedom.nio.DefaultFilterChain.FilterEntry;


public interface FilterChain {

	public void addLast(String name,Filter filter);
	
	public void fireConnect(Object data);
	
	public void fireRead(Object data);
	
	public void fireWrite(Object data);
	
	public List<FilterEntry> getEntries();
	
}
