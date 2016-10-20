package freedom.common.kit;

import java.util.List;
import java.util.regex.Pattern;

/**字符串处理工具
 * @author majl
 *
 */
public class StrKit {

	public static final boolean isEmpty(String str)
	{
		return null == str;
	}
	
	public static final boolean isNotEmpty(String str)
	{
		return !isEmpty(str);
	}
	
	public static final boolean isBlank(String str)
	{
		return null == str || "".equals(str);
	}
	
	public static final boolean isNotBlank(String str)
	{
		return !isBlank(str);
	}
	
	
	/**
	 * 首字母大写
	 * */
	public static final String firstUpper(String str)
	{
		if(isBlank(str))
			return str;
		char[] chars = str.toCharArray();
		char first = chars[0];
		if(first >= 'a' && first <= 'z')
			chars[0] = (char) (first - 32);
		return String.valueOf(chars);
	}
	
	/**
	 * 首字母小写
	 * */
	public static final String firstLower(String str)
	{
		if(isBlank(str))
			return str;
		char[] chars = str.toCharArray();
		char first = chars[0];
		if(first >= 'A' && first <= 'Z')
			chars[0] = (char) (first + 32);
		return String.valueOf(chars);
	}
	
	/**将列表元素以固定分隔符分割组成字符串返回
	 * @param list
	 * @param spliter
	 * @return
	 */
	public static final <T> String join(List<T> list,String spliter)
	{
		return null == list ? "" : join(list.toArray(), spliter);
	}
	
	/**将列表元素以固定分隔符分割组成字符串返回
	 * @param array
	 * @param spliter
	 * @return
	 */
	public static final <T> String join(T[] array,String spliter)
	{
		if(array == null || array.length == 0)
			return "";
		StringBuilder builder = new StringBuilder();
		builder.append(array[0]);
		int count = array.length;
		for(int i = 1;i < count;i++)
		{
			builder.append(spliter).append(array[i]);
		}
		return builder.toString();
	}
	
	/**
	 * 是否是手机格式
	 * */
	public static final boolean isMobile(String str)
	{
		Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		return p.matcher(str).matches();
	}
}
