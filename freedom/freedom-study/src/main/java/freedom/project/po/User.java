package freedom.project.po;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class User extends Model{

	private String name;
	private long money;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getMoney() {
		return money;
	}
	public void setMoney(long money) {
		this.money = money;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", money=" + money + "]";
	}
	
	public static class UserRowMapper implements RowMapper<User> {

	    @Override
	    public User mapRow(ResultSet rs, int rowNum) throws SQLException
	    {
	        User user = new User();
	        user.setId(rs.getInt("id"));
	        user.setName(rs.getString("name"));
	        user.setMoney(rs.getLong("money"));

	        return user;
	    }

	}
	
}
