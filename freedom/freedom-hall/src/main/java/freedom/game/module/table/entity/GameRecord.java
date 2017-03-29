package freedom.game.module.table.entity;

import java.util.Date;
import java.util.List;

/**
 * 游戏记录(每一局游戏都会有一个记录)
 * */
public class GameRecord {

	private long id;
	private long roomId;
	private long tableId;
	private int  dizhu;
	private Date startTime;
	private Date endTime;
	private List<Player> players;
	private List<Card> outCardList;//出牌集合
	private List<Card> tableCardList;//桌上剩余的牌
}
