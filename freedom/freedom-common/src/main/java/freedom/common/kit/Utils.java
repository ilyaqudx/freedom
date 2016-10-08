package freedom.common.kit;

public class Utils {

	public static final void assertNull(Object obj,String msg)
	{
		if(null == obj)
			throw new NullPointerException(msg);
	}
}
