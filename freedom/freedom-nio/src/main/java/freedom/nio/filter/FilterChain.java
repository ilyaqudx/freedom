package freedom.nio.filter;

import java.util.List;

import freedom.nio.WriteRequest;
import freedom.nio.filter.DefaultFilterChain.FilterEntry;


public interface FilterChain {

	public void addLast(String name,Filter filter);
	
	public void fireConnect(Object data);
	
	public void fireRead(Object data);
	
	public void fireWrite(WriteRequest request);
	
	public void fireSent(WriteRequest request);
	
	public List<FilterEntry> getEntries();
	
}
