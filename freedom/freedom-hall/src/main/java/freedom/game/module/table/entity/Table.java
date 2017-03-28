package freedom.game.module.table.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Lists;

import freedom.common.constants.Consts;
import freedom.game.GameManager;
import freedom.game.module.robot.Robot;
import freedom.game.module.robot.RobotManager;
import freedom.game.module.room.sender.TableMessageSender;
import freedom.game.module.table.MajongService;
import freedom.hall.module.room.entity.Room;


/**
 * 
 * 麻将桌的设计
 * 
 * 
 * Table  对应现实中的牌桌
 * 			功能:[牌组,庄家,当前玩家,当前出牌,当前牌桌状态,已出牌集合,操作集合,玩家列表,超时倒计时,麻将规则,麻将逻辑]
 * 			牌桌状态:(具体的逻辑都有LOGIC根据RULE制定的规则去执行)
 * 				等待:
 * 					
 * 				游戏中:
 * 					摸牌/打牌/响应
 * 				结算:
 * Rule   规则章程(对应现实中的规则)
 * 			功能:规定包含的规则如:牌型(金勾钓,将对,么九),番型(大对对应的番),操作(碰,吃,胡,杠)
 * 				是否能抢杠,是否能多响,未过庄是否能胡,查叫查大还是查小,花猪赔叫.
 * Logic  游戏桌逻辑的处理,按规则执行逻辑
 * 			
 * 					
 * */
public class Table {

	private long id;
	private String sn;
	private String name;
	//座位资源
	private AtomicInteger seat = new AtomicInteger(4);
	//真实的座位号
	List<Integer> emptySeats = Lists.newArrayList(0,1,2,3);
	List<Integer> sitedSeats = Lists.newArrayList();
	private Room room;
	volatile State state;
	volatile State nextState;
	volatile PlayingState playingState;
	List<Player> users = new ArrayList<Player>();
	List<Card> tableCard = new ArrayList<Card>();
	Player currentPlayer;
	int timeout;
	long startTime;
	private int firstSeat;
	private long lastInTime;
	private long delayStart = 10000;
	private Logic logic ;
	
	public int getFirstSeat() {
		return firstSeat;
	}

	public void setFirstSeat(int firstSeat) {
		this.firstSeat = firstSeat;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public enum State{
		/**
		 * 1 等人
		 * 2 发牌
		 * 3 选择缺牌 
		 * 9-是一个过滤状态,主要作用是延时,并且需要设置超时后的下一状态
		 * */
		INIT(0),START_GAME(1),SELECT_LACK_CARD(2),PLAYING (3),DELAY(9), FINISH(4);
		
		private int v;
		
		State(int v)
		{
			this.v = v;
		}
	}
	
	public enum PlayingState{
		/**
		 * 30 - 玩家摸牌(瞬时状态)
		 * 31 - 等待玩家打牌
		 * 32 - 等待响应
		 * 
		 * */
		PUT_CARD(31),WAIT_PLAYER_OUT(32),WAIT_RESPONSE(33);
		
		private int v;
		
		PlayingState(int v)
		{
			this.v = v;
		}	
	}
	
	public Table(Room room)
	{
		this.room  = room;
		this.logic = new Logic(this);
		this.init();
	}
	
	/**
	 * 初始化桌子
	 * */
	private void init()
	{
		this.state = State.INIT;
		this.logic .initCard();
	}
	
	public boolean getSeat()
	{
		return seat.decrementAndGet() >= 0;
	}
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public PlayingState getPlayingState() {
		return playingState;
	}

	public void setPlayingState(PlayingState playingState) {
		this.playingState = playingState;
	}

	public List<Player> getUsers() {
		return users;
	}

	public void setUsers(List<Player> users) {
		this.users = users;
	}

	public List<Card> getTableCard() {
		return tableCard;
	}

	public void setTableCard(List<Card> tableCard) {
		this.tableCard = tableCard;
	}
	
	public Scene getScene()
	{
		return new Scene(this);
	}
	
	public void update()
	{
		if(state == State.INIT)
		{
			boolean full = seat.get() <= 0;
			if(full){
				//判断是否已全部准备
				boolean allReady = true;
				for (Player p : users)
				{
					if(p.getReady() == Consts.FALSE)
					{
						allReady = false;
						break;
					}
				}
				
				if(allReady && --delayStart <= 0){
					logic.start();
					state = State.START_GAME;
					logic.timeout(6000);//6s 发牌时间
					System.out.println("开始游戏  : " + id);
				}
			}else
			{
				//检查是否需要添加机器人
				if(logic.hasRealPlayer() && System.currentTimeMillis() - lastInTime >= 1000)
				{
					if(getSeat()){
						//添加机器人
						Robot robot = GameManager.context.getBean(RobotManager.class)
								.getRobot();
						logic.sitdown(robot);
						robot.setReady(Consts.TRUE);
						GameManager.context.getBean(TableMessageSender.class)
						.sendEntryRoomMessage(robot, this);
						
						//GameManager.context.getBean(TableMessageSender.class).sendReadyMessage(robot, this);
					}
				}
			}
		}
		else if(state == State.START_GAME)
		{
			if(logic.isTimeout())
			{
				//发牌总共超时时间给6s
				GameManager.context.getBean(TableMessageSender.class)
				.sendStartSelectLackCard(this);
				this.state =  State.SELECT_LACK_CARD;
				logic.timeout(10 * 1000);//8秒选择缺门
			}
		}
		else if(state == State.SELECT_LACK_CARD)
		{
			boolean isTimeout = logic.isTimeout();
			boolean allSelected = true;
			for (Player p : users) {
				if(!p.isSelectedLackColor())
				{
					if(isTimeout){
						//已超时,系统默认选择缺门
						p.setSelectLackColor(new Random().nextInt(3) + 1);
					}
					else
					{
						allSelected = false;
						break;
					}
				}
			}
			
			if(allSelected)
			{
				//选择缺门结束
				GameManager.context.getBean(TableMessageSender.class)
				.sendSelectLackCardResult(this);
				this.currentPlayer = users.get(0);
				this.playingState = PlayingState.PUT_CARD;
				logic.delay(2000, State.PLAYING);
			}
		}
		else if(state == State.PLAYING)
		{
			if(playingState == PlayingState.PUT_CARD)
			{
				boolean isTimeout = logic.isTimeout();
				if(!tableCard.isEmpty())
				{
					Card card = this.tableCard.remove(0);
					this.currentPlayer.putCard(card);
					
					//摸牌
					GameManager.context.getBean(TableMessageSender.class)
					.sendPutCardInPlaying(this);
					System.out.println("玩家" +this.currentPlayer.getName()+ "摸牌成功");
					//判断是否能胡能杠
					/*if(CardUtil.canHu(currentPlayer.getHandCard()))
					{
						Operator opt = new Operator(OPT.HU, card);
						this.currentPlayer.getOpts().add(opt);
						GameManager.context.getBean(TableMessageSender.class)
						.sendWaitResponseAfterPutCard(this);
						timeoutInPlaying(PlayingState.WAIT_RESPONSE, 5000);
					}*/
					if(MajongService.hasResponseAfterPutCard(currentPlayer))
					{
						GameManager.context.getBean(TableMessageSender.class)
						.sendWaitResponseAfterPutCard(this);
						logic.timeoutInPlaying(PlayingState.WAIT_RESPONSE, 5000);
					}else
					{
						logic.timeoutInPlaying(PlayingState.WAIT_PLAYER_OUT,5000);
						System.out.println("进入等待打牌状态 : " + isTimeout + ",timeout = " + timeout);
					}
				}else
				{
					this.state = State.FINISH;
					System.out.println("牌摸完了,进入结算");
				}
			}
			else if(playingState == PlayingState.WAIT_PLAYER_OUT)
			{
				boolean isTimeout = logic.isTimeout();
				if(isTimeout)
				{
					System.out.println("等待玩家 " +this.currentPlayer.getName()+ "打牌超时");
					//如果超时还没有打牌,则摸什么打什么
					this.currentPlayer.outCard();
					GameManager.context.getBean(TableMessageSender.class)
					.sendPlayerOutCard(this);
					
					//打牌后判断是否有玩家可以有响应(胡/杠/碰)
					List<Player> responsePlayers = MajongService.hasResponse(this);
					if(responsePlayers.isEmpty())
						logic.nextPlayerPutCard();
					else
					{
						//发送等待响应消息
						GameManager.context.getBean(TableMessageSender.class)
						.sendWaitResponseAfterOutCard(this);
						logic.timeoutInPlaying(PlayingState.WAIT_RESPONSE, 10000);
					}
						
				}
			}
			else if(playingState == PlayingState.WAIT_RESPONSE)
			{
				boolean timeout = logic.isTimeout();
				if(timeout)
				{
					//如果没有响应则代表取消操作,下一个玩家继续
					logic.resetOpts();
					logic.nextPlayerPutCard();
				}
			}
		}
		else if(state == State.FINISH)
		{
			
		}
		else if(state == State.DELAY)
		{
			if(logic.isTimeout())
			{
				this.state = nextState;
			}
		}
	}
	
	public Player getPlayer(long playerId)
	{
		for (Player player : users) 
		{
			if(playerId == player.getId())
				return player;
		}
		return null;
	}
	
	/**
	 * 牌桌场景类(对于服务器来说,就是牌桌数据)
	 * */
	public static final class Scene{
		
		private long roomId;
		private long tableId;
		private int timeout;
		private State state;
		private long currentPlayer;
		private List<Player> players;
		private List<Card> tableCard;
		
		private Scene(Table table)
		{
			this.roomId = table.getRoom().getId();
			this.tableId= table.getId();
			this.state = table.getState();
			this.players = table.getUsers();
			this.tableCard = table.getTableCard();
			//this.currentPlayer = table.getCurrentPlayer().getId();
			this.timeout = table.getTimeout();
		}
		
		public long getRoomId() {
			return roomId;
		}
		public void setRoomId(long roomId) {
			this.roomId = roomId;
		}
		public long getTableId() {
			return tableId;
		}
		public void setTableId(long tableId) {
			this.tableId = tableId;
		}
		public int getTimeout() {
			return timeout;
		}
		public void setTimeout(int timeout) {
			this.timeout = timeout;
		}
		public State getState() {
			return state;
		}
		public void setState(State state) {
			this.state = state;
		}
		public long getCurrentPlayer() {
			return currentPlayer;
		}
		public void setCurrentPlayer(long currentPlayer) {
			this.currentPlayer = currentPlayer;
		}
		public List<Player> getPlayers() {
			return players;
		}
		public void setPlayers(List<Player> players) {
			this.players = players;
		}
		public List<Card> getTableCard() {
			return tableCard;
		}
		public void setTableCard(List<Card> tableCard) {
			this.tableCard = tableCard;
		}
	}
}
