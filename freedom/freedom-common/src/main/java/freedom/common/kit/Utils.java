package freedom.common.kit;

public class Utils {

	public static final void assertNull(Object obj,String msg)
	{
		if(null == obj)
			throw new NullPointerException(msg);
	}
	
	public static final long checkRange(long v,long min,long max)
	{
		if(max < min)
			throw new IllegalArgumentException("max < min");
		if(between(v, min, max))
			return v;
		return v < min ? min : max;
	}
	
	public static final boolean between(long v,long min,long max)
	{
		if(v >= min && v <= max)
			return true;
		return false;
	}
}
