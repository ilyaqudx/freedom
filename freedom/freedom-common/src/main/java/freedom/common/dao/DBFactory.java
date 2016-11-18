package freedom.common.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.SQL;
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
	
	public static void main(String[] args) {
		String driverClassName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://rdsr46zx53ze0ebceo1n.mysql.rds.aliyuncs.com:3306/azy2";
        String username = "iquizoo"; // 这里请使用您自己的用户名
        String password = "db62083886"; // 这里请使用您自己的密码
        DataSource ds = new DriverManagerDataSource(driverClassName, url, username, password);
        mango = Mango.newInstance(ds); // 使用数据源初始化mango
        
        
        AccountDAO accountDAO = mango.create(AccountDAO.class);
        UserDAO userDAO = mango.create(UserDAO.class);
        
        Account a = accountDAO.get(1711);
        
        System.out.println(a);
        
        List<Account> accountList = accountDAO.list();
        
        List<Account> emptyList = new ArrayList<Account>();
        for (Account account : accountList) 
        {
        	System.out.println(account);
			int count = userDAO.countByAccount(account.getId());
			if(count == 0)
				emptyList.add(account);
		}
        
        
        for (Account account : emptyList) {
			System.out.println(account);
		}
	}
	
	
	public static class User{
		private int id;
		private String name;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@Override
		public String toString() {
			return "User [id=" + id + ", name=" + name + "]";
		}
	}
	
	public static class Account
	{
		private int id;
		private String name;
		private String phone;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		@Override
		public String toString() {
			return "Account [id=" + id + ", name=" + name + ", phone=" + phone
					+ "]";
		}
		
	}
	
	@DB
	interface AccountDAO
	{
		@SQL("SELECT * FROM Account WHERE createTime >= 2016-11-01 00:00:00")
		public List<Account> list();
		
		@SQL("SELECT * FROM Account WHERE id = :1")
		public Account get(int id);
	}
	
	@DB
	interface UserDAO{
		
		@SQL("SELECT * FROM User WHERE id = :1")
		public List<User> listByAccount(int accountId);
		@SQL("SELECT COUNT(*) FROM User WHERE accountId = :1")
		public int countByAccount(int accountId);
	}
	
}
