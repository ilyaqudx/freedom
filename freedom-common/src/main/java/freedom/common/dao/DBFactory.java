package freedom.common.dao;

import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.jfaster.mango.datasource.DriverManagerDataSource;
import org.jfaster.mango.operator.Mango;

public class DBFactory {

	private static Mango mango = null;
	
	private static final ConcurrentHashMap<Class<?>, Object> daos = 
			new ConcurrentHashMap<Class<?>, Object>();
	
	static
	{
		String driverClassName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/majiang_hall";
        String username = "root"; // 这里请使用您自己的用户名
        String password = ""; // 这里请使用您自己的密码
        DataSource ds = new DriverManagerDataSource(driverClassName, url, username, password);
        mango = Mango.newInstance(ds); // 使用数据源初始化mango
	}
	
	@SuppressWarnings("unchecked")
	public static final <T> T getDAO(Class<T> daoClass)
	{
		Object entity = daos.get(daoClass);
		if(null == entity){
			entity = mango.create(daoClass);
			daos.put(daoClass,entity);
		}
		return (T) entity;
	}
}
