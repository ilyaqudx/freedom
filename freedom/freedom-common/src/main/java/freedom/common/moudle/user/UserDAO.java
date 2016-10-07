package freedom.common.moudle.user;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.ReturnGeneratedId;
import org.jfaster.mango.annotation.SQL;

@DB
public interface UserDAO {

	@ReturnGeneratedId
	@SQL("INSERT INTO User(name,sex,password,imei,createTime,gold) "
			+ "VALUES(:1.name,:1.sex,:1.password,:1.imei,:1.createTime,:1.gold)")
	public long insert(User user);
	
	@SQL("SELECT * FROM User WHERE imei = :1")
	public User getByImei(String imei);
	
	@SQL("SELECT * FROM User WHERE id = :1")
	public User get(long id);
	@SQL("UPDATE User SET gold = :2 WHERE id = :1")
	public void changeGold(int userId,long gold);
}
