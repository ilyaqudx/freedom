package freedom.common.moudle.user;

public interface UserService {

	public User getByImei(String imei)throws Exception;

	public long insert(User user)throws Exception;
	
	public User get(long id)throws Exception;
}
