package freedom.project.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import freedom.project.po.Model;

public interface IBaseDao<T extends Model,M extends RowMapper<T>> {

	public int insert(T entity);
	
	public T get(long id);
	
	public int update(T entity);
	
	public List<T> list();
}
