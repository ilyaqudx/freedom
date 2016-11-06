package freedom.game.module.table.state;

import freedom.common.constants.Consts;
import freedom.game.GameManager;
import freedom.game.module.robot.Robot;
import freedom.game.module.robot.RobotManager;
import freedom.game.module.room.sender.TableMessageSender;
import freedom.game.module.table.entity.Player;
import freedom.game.module.table.entity.Table;
/**
 * 牌桌等人状态逻辑
 * */
public class WaitPlayerState extends GameState {

	public WaitPlayerState(Table table) 
	{
		super(table,0);
	}
	
	@Override
	public void action()
	{
		boolean full = table.getSeatNumber() <= 0;
		if(full){
			//判断是否已全部准备
			boolean allReady = true;
			for (Player p : table.getUsers())
			{
				if(p.getReady() == Consts.FALSE)
				{
					allReady = false;
					break;
				}
			}
			
			if(allReady && --table.delayStart <= 0)
			{
				logic.start();
				logic.setDelayState(6000,table.START_GAME);
				System.out.println("开始游戏  : " + table.getId());
			}
		}else
		{
			//检查是否需要添加机器人
			if(logic.hasRealPlayer() && System.currentTimeMillis() - table.getLogic().getLastInTime() >= 1000)
			{
				if(table.getSeat()){
					//添加机器人
					Robot robot = GameManager.context.getBean(RobotManager.class)
							.getRobot();
					logic.sitdown(robot);
					robot.setReady(Consts.TRUE);
					GameManager.context.getBean(TableMessageSender.class)
					.sendEntryRoomMessage(robot, table);
				}
			}
		}
	}

}
