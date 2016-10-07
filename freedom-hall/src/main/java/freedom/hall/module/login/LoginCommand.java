package freedom.hall.module.login;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import freedom.common.kit.StrKit;
import freedom.common.moudle.user.User;
import freedom.common.moudle.user.UserService;
import freedom.game.SessionManager;
import freedom.hall.Cmd;
import freedom.hall.module.login.LoginMessage.In;
import freedom.hall.module.login.LoginMessage.Out;
import freedom.socket.command.AbstractCommand;
import freedom.socket.command.LogicException;

@Component(Cmd.Req.LOGIN)
public class LoginCommand extends AbstractCommand<LoginMessage> {

	public static final int LOGIN_TYPE_VISITOR = 2,LOGIN_TYPE_ACCOUNT = 1;
	
	@Autowired
	private UserService userService;
	@Autowired
	private SessionManager sessionManager;
	
	@Override
	public LoginMessage execute(LoginMessage msg) throws Exception
	{
		In in = msg.getIn();
		int loginType = msg.getIn().getLoginType();
		User user = null;
		if(loginType == LOGIN_TYPE_VISITOR)
		{
			user = userService.getByImei(in.getImei());
			if(null == user)
			{
				user = new User();
				user.setImei(in.getImei());
				user.setCreateTime(new Date());
				user.setName("visitor" + UUID.randomUUID().toString().substring(31));
				user.setGold(new Random().nextInt(999999999));
				long userId = userService.insert(user);
				user.setId(userId);
			}
			Out out = new Out();
			out.setUser(user);
			msg.setOut(out);
			msg.setResCmd(Cmd.Res.LOGIN);
		}
		else if(loginType == LOGIN_TYPE_ACCOUNT)
		{
			user = userService.get(in.getUserId());
			if(user == null)
				msg.setEx(new LogicException(-1, "帐号不存在"));
			else if(StrKit.isBlank(in.getPassword()) || !in.getPassword().equals(user.getPassword()))
				msg.setEx(new LogicException(-1, "密码错误"));
			else
			{
				Out out = new Out();
				out.setUser(user);
				msg.setOut(out);
				msg.setResCmd(Cmd.Res.LOGIN);
			}
		}
		
		if(!msg.hasError())
		{
			sessionManager.add(user.getId(), msg.getSession());
		}
		
		return msg;
	}

}
