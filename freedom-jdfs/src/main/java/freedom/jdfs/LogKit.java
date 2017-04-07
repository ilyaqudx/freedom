package freedom.jdfs;

import org.apache.log4j.Logger;


/**
 * 日志工具
 * */
public class LogKit {

	public static final String DEBUG = "LOG_DEBUG";
	
	public static final void info(String message,Class<?> target)
	{
			Logger.getLogger(target).info(message);
	}
	
	public static final void debug(String message,Class<?> target)
	{
			Logger.getLogger(target).debug(message);
	}
	
	public static final void warn(String message,Class<?> target)
	{
		Logger.getLogger(target).warn(message);
	}
	
	public static final void error(String message,Class<?> target)
	{
		Logger.getLogger(target).error(message);
	}
	
	public static final void error(String message,Throwable throwable,Class<?> target)
	{
		Logger.getLogger(target).error(message, throwable);
	}
}
