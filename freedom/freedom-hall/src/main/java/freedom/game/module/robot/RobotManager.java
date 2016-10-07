package freedom.game.module.robot;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class RobotManager {

	private ConcurrentLinkedQueue<Robot> robots = new 
			ConcurrentLinkedQueue<Robot>();
	
	@PostConstruct
	public void init()
	{
		for (int i = 0; i < 200; i++) 
		{
			Robot robot = new Robot();
			robots.offer(robot);
		}
	}
	
	public Robot getRobot()
	{
		return robots.poll();
	}
	
	public void gcRobot(Robot robot)
	{
		robots.offer(robot);
	}
}
