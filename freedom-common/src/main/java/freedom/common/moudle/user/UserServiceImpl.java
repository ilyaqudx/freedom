package freedom.common.moudle.user;

import org.springframework.stereotype.Service;

import freedom.common.dao.DBFactory;

@Service
public class UserServiceImpl implements UserService {

	private UserDAO userDAO = DBFactory.getDAO(UserDAO.class);
	
	@Override
	public User getByImei(String imei) throws Exception 
	{
		return userDAO.getByImei(imei);
	}

	@Override
	public long insert(User user) throws Exception 
	{
		return userDAO.insert(user);
	}

	@Override
	public User get(long id) throws Exception {
		// TODO Auto-generated method stub
		return userDAO.get(id);
	}
}
