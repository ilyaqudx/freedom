package freedom.gate.module.login.command;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import freedom.common.moudle.user.User;
import freedom.common.moudle.user.UserDAO;
import freedom.gate.CMD;
import freedom.gate.module.login.message.LoginMessage;
import freedom.gate.module.login.message.LoginMessage.In;
import freedom.socket.command.AbstractCommand;

@Component(CMD.LOGIN)
public class LoginCommand extends AbstractCommand<LoginMessage>{

	public static final int LOGIN_TYPE_VISITOR = 1 ,LOGIN_TYPE_ACCOUNT = 2;
	
	@Autowired
	private UserDAO userDAO;
	
	@Override
	public LoginMessage execute(LoginMessage msg)throws Exception 
	{
		In in = msg.getIn();
		int loginType = in.getLoginType();
		if(loginType == LOGIN_TYPE_VISITOR)
			visitorLogin(in.getImei());
		else if(loginType == LOGIN_TYPE_ACCOUNT)
			accountLogin(in.getUserId(), in.getPassword());
		return msg;
	}
	
	
	private User visitorLogin(String imei)
	{
		User user = userDAO.getByImei(imei);
		if(null == user)
		{
			user = new User();
			user.setName("ID"  + imei);
			user.setCreateTime(new Date());
			userDAO.insert(user);
		}
		return user;
	}

	
	private User accountLogin(long userId,String password) throws Exception
	{
		User user = userDAO.get(userId);
		if(user == null)
			throw new Exception("用户不存在");
		else if(!user.getPassword().equals(password))
			throw new Exception("密码不正确");
		return user;
	}
	

}
