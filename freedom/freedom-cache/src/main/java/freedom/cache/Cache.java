package freedom.cache;

public interface Cache<K,V> {

	public void set(K key,V value);
	
	public V get(K key);
	
	public void delete(K key);
	
	public void expire(K key,int timeout);
}
