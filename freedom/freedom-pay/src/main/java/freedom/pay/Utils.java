package freedom.pay;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Joiner;

public class Utils {

	public static final <T> String getWXSignString(T res)
	{
		Set<Object> params = new TreeSet<Object>();
		Class<?> clazz = res.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields)
		{
			f.setAccessible(true);
			try 
			{
				String k = f.getName();
				Object v = f.get(res);
				if(!"sign".equals(k) && v != null)
				{
					if("packageInfo".equals(k))
						params.add("package="+v);
					else
						params.add(k + "=" + v);
				}
			} 
			catch (Exception e)
			{
				
			}
		}
		return Joiner.on('&').join(params.iterator()).toString();
	}
	
	public static final <T> String getAlipaySignString(T res)
	{
		Set<Object> params = new TreeSet<Object>();
		Class<?> clazz = res.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields)
		{
			f.setAccessible(true);
			try 
			{
				String k = f.getName();
				Object v = f.get(res);
				if(!"sign".equals(k) && !"sign_type".equals(k) && v != null)
				{
					params.add(k + "=" + v);
				}
			} 
			catch (Exception e)
			{
				
			}
		}
		return Joiner.on('&').join(params.iterator()).toString();
	}
	
	public static final <T> String toXml(T res,String format)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("<xml>");
		Class<?> clazz = res.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) 
		{
			try 
			{
				f.setAccessible(true);
				String k = f.getName();
				Object v = f.get(res);
				if(null != v)
				{
					String newField = String.format(format, k,v,k);
					buffer.append(newField);
				}
			} 
			catch (Exception e) 
			{
			}
		}
		return buffer.append("</xml>").toString();
	}
}
