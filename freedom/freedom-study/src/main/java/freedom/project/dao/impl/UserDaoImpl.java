package freedom.project.dao.impl;

import org.springframework.stereotype.Service;

import freedom.project.dao.IUserDao;
import freedom.project.po.User;
import freedom.project.po.User.UserRowMapper;
@Service
public class UserDaoImpl extends BaseDaoImpl<User,UserRowMapper> implements IUserDao {

	@Override
	public int update(User entity)
	{
		return 0;
	}


}
