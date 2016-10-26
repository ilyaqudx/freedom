package freedom.project.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.google.common.base.Joiner;

import freedom.project.dao.IBaseDao;
import freedom.project.po.Model;

public abstract class BaseDaoImpl<T extends Model,M extends RowMapper<T>> implements IBaseDao<T,M>{

	@Autowired
	protected JdbcTemplate template;
	
	protected Class<T> clazz;
	protected Class<RowMapper<T>> mapper;
	protected String simpleName;
	@SuppressWarnings("unchecked")
	public BaseDaoImpl()
	{
		Type[] types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
		clazz = (Class<T>) types[0];
		mapper = (Class<RowMapper<T>>) types[1];
		simpleName = clazz.getSimpleName();
	}
	
	@Override
	public T get(long id)
	{
		try
		{
			return template.queryForObject(String.format("SELECT * FROM %s WHERE id = ?", simpleName),new Object[]{id},mapper.newInstance());
		} 
		catch (DataAccessException | InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int insert(T entity) 
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO ").append(simpleName).append("(%) VALUES(%s)");
		Field[] fields = clazz.getDeclaredFields();
		List<String> keys = new ArrayList<String>();
		List<Object> values = new ArrayList<Object>();
		for (Field field : fields)
		{
			try 
			{
				field.setAccessible(true);
				Object value     = field.get(entity);
				if(value != null)
				{
					String fieldName = field.getName();
					values.add(field.get(entity));
					keys.add(fieldName);
				}
			} 
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			} 
			catch (IllegalAccessException e) 
			{
				e.printStackTrace();
			}
		}
		String keyString   = Joiner.on(',').join(keys);
		String valueString = Joiner.on(',').join(values);
		buffer.append(keyString).append(valueString).append(")");
		return template.update(buffer.toString());
	}
	
	

	@Override
	public int update(T entity)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE ").append(simpleName).append(" SET ");
		Field[] fields = clazz.getDeclaredFields();
		List<String> prepareString = new ArrayList<String>();
		List<Object> prepareValues = new ArrayList<Object>();
		for (Field field : fields)
		{
			try 
			{
				field.setAccessible(true);
				String fieldName = field.getName();
				if("id".equals(fieldName))
					continue;
				//直接写 KEY = VALUE，时间如何处理
				prepareValues.add(field.get(entity));
				prepareString.add(String.format("%s = ?", fieldName));
			} 
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			} 
			catch (IllegalAccessException e) 
			{
				e.printStackTrace();
			}
		}
		String keyString   = Joiner.on(',').join(prepareString);
		buffer.append(keyString).append(" WHERE id = ?");
		prepareValues.add(entity.getId());
		Object[] values    = prepareValues.toArray(); 
		return template.update(buffer.toString(), values);
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> list()
	{
		return (List<T>) template.queryForList(String.format("SELECT * FORM %s", simpleName));
	}

}
