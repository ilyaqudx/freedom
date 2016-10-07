package freedom.game.module.robot;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import freedom.common.constants.Consts;
import freedom.common.moudle.user.User;
import freedom.game.module.table.entity.Player;

public class Robot extends Player{

	private static AtomicLong ids = new AtomicLong(10000001);
	
	public Robot()
	{
		this(new User("robot" + UUID.randomUUID().toString().substring(28), 0, 550000));
		setId(ids.getAndIncrement());
		super.setRobot(Consts.TRUE);
	}
	
	public Robot(User user)
	{
		super(user);
	}
	
	public static void main(String[] args) 
	{
		String a =  "^";
		
		System.out.println(a.replace("", ""));
		
		String tt = String.format("%x", 10);
		System.out.println(tt);
	}
}
