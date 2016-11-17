package freedom.game.module.table.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Lists;

import freedom.common.constants.Consts;
import freedom.game.GameManager;
import freedom.game.module.robot.Robot;
import freedom.game.module.robot.RobotManager;
import freedom.game.module.room.sender.TableMessageSender;
import freedom.game.module.table.CardUtil;
import freedom.hall.module.room.entity.Room;

public class Table {

	private long id;
	private String sn;
	private String name;
	//座位资源
	private AtomicInteger seat = new AtomicInteger(4);
	//真实的座位号
	private List<Integer> emptySeats = Lists.newArrayList(0,1,2,3);
	private List<Integer> sitedSeats = Lists.newArrayList();
	private Room room;
	private volatile State state;
	private volatile State nextState;
	private volatile PlayingState playingState;
	private List<Player> users = new ArrayList<Player>();
	private List<Card> tableCard = new ArrayList<Card>();
	private Player currentPlayer;
	private int timeout;
	private long startTime;
	private int firstSeat;
	private long lastInTime;
	private long delayStart = 10000;
	
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
		this.room = room;
		this.init();
	}
	
	/**
	 * 初始化桌子
	 * */
	private void init()
	{
		this.state = State.INIT;
		this.initCard();
	}
	
	protected void initCard()
	{
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				for (int j2 = 0; j2 < 9; j2++) {
					tableCard.add(new Card(i*36 + j*9 + j2 + 1,i+1,j2+1));
				}
			}
		}
	}
	
	protected void shuffleCard()
	{
		Collections.shuffle(tableCard);
		Collections.shuffle(tableCard);
		Collections.shuffle(tableCard);
	}
	
	public boolean getSeat()
	{
		return seat.decrementAndGet() >= 0;
	}
	
	public void sitdown(Player player)
	{
		player.setRoomId(room.getId());
		player.setTableId(id);
		player.setSeat(emptySeats.remove(0));
		sitedSeats.add(player.getSeat());
		users.add(player);
		lastInTime = System.currentTimeMillis();
	}
	
	public void start()
	{
		//-洗牌
		shuffleCard();
		//-发牌
		firstPutCard();
		//-通知客户端显示发牌动画
		GameManager.context.getBean(TableMessageSender.class)
		.sendStartGameFirstPutCard(this);
	}
	
	private void firstPutCard()
	{
		int[][] first = new int[][]{
				{1,1},{1,1},{1,1},{1,3},
				{1,3},{1,3},{1,4},{1,4},
				{1,5},{1,2},{1,2},{1,2},
				{1,6}
		};
		int[][] second = new int[][]{
				{1,8},{1,2},{1,5},{1,6},
				{1,3},{1,4},{1,4},{1,8},
				{1,5},{1,5},{1,6},{1,7},
				{1,7}
		};
		int[][] up = new int[][]{
				{2,1},{2,1},{2,2},{2,2},
				{2,3},{2,3},{2,4},{2,4},
				{2,5},{2,5},{2,6},{2,6},
				{2,6}
		};
		int[][] left = new int[][]{
				{3,1},{3,1},{3,2},{3,2},
				{3,3},{3,3},{3,4},{3,4},
				{3,5},{3,5},{3,6},{3,6},
				{3,6}
		};
		List<int[][]> random = new ArrayList<int[][]>();
		random.add(first);
		random.add(second);
		random.add(up);
		random.add(left);
		for (int i = 0; i < 4; i++) 
		{
			List<Card> handCard = users.get(i).getHandCard();
			int[][] temp = random.get(i);
			for (int j = 0; j < temp.length; j++) 
			{
				for (Card card : tableCard) {
					if(card.getColor() == temp[j][0] && card.getValue() == temp[j][1])
					{
						handCard.add(card);
						tableCard.remove(card);
						break;
					}
				}
			}
		}
		/*int[] putRule = new int[]{4,4,4,1};
		for (int i = 0; i < 4; i++)
		{
			for (Player p : users)
			{
				for (int j = 0; j < putRule[i]; j++) 
				{
					p.getHandCard().add(tableCard.remove(0));
				}
			}
		}*/
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
	
	public Player ready(long playerId)
	{
		for (Player p : users) 
		{
			if(p.getId() == playerId)
			{
				p.setReady(Consts.TRUE);
				return p;
			}
		}
		return null;
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
					start();
					state = State.START_GAME;
					this.timeout(6000);//6s 发牌时间
					System.out.println("开始游戏  : " + id);
				}
			}else
			{
				//检查是否需要添加机器人
				if(hasRealPlayer() && System.currentTimeMillis() - lastInTime >= 1000)
				{
					if(getSeat()){
						//添加机器人
						Robot robot = GameManager.context.getBean(RobotManager.class)
								.getRobot();
						sitdown(robot);
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
			if(isTimeout())
			{
				//发牌总共超时时间给6s
				GameManager.context.getBean(TableMessageSender.class)
				.sendStartSelectLackCard(this);
				this.state =  State.SELECT_LACK_CARD;
				this.timeout(10 * 1000);//8秒选择缺门
			}
		}
		else if(state == State.SELECT_LACK_CARD)
		{
			boolean isTimeout = isTimeout();
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
				this.delay(2000, State.PLAYING);
			}
		}
		else if(state == State.PLAYING)
		{
			if(playingState == PlayingState.PUT_CARD)
			{
				boolean isTimeout = isTimeout();
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
					if(CardUtil.hasResponseAfterPutCard(currentPlayer))
					{
						GameManager.context.getBean(TableMessageSender.class)
						.sendWaitResponseAfterPutCard(this);
						timeoutInPlaying(PlayingState.WAIT_RESPONSE, 5000);
					}else
					{
						timeoutInPlaying(PlayingState.WAIT_PLAYER_OUT,5000);
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
				boolean isTimeout = isTimeout();
				if(isTimeout)
				{
					System.out.println("等待玩家 " +this.currentPlayer.getName()+ "打牌超时");
					//如果超时还没有打牌,则摸什么打什么
					this.currentPlayer.outCard();
					GameManager.context.getBean(TableMessageSender.class)
					.sendPlayerOutCard(this);
					
					//打牌后判断是否有玩家可以有响应(胡/杠/碰)
					List<Player> responsePlayers = CardUtil.hasResponse(this);
					if(responsePlayers.isEmpty())
						nextPlayerPutCard();
					else
					{
						//发送等待响应消息
						GameManager.context.getBean(TableMessageSender.class)
						.sendWaitResponseAfterOutCard(this);
						timeoutInPlaying(PlayingState.WAIT_RESPONSE, 10000);
					}
						
				}
			}
			else if(playingState == PlayingState.WAIT_RESPONSE)
			{
				boolean timeout = isTimeout();
				if(timeout)
				{
					//如果没有响应则代表取消操作,下一个玩家继续
					resetOpts();
					nextPlayerPutCard();
				}
			}
		}
		else if(state == State.FINISH)
		{
			
		}
		else if(state == State.DELAY)
		{
			if(isTimeout())
			{
				this.state = nextState;
			}
		}
	}
	
	private void resetOpts()
	{
		for (Player p : users) 
		{
			if(p.hasOpt())
			{
				p.getOpts().clear();
			}
		}
	}
	
	public void nextPlayerPutCard()
	{
		Player player = computeNextPlayer(currentPlayer);
		this.currentPlayer = player;
		timeoutInPlaying(PlayingState.PUT_CARD, 0);
	}

	public Player computeNextPlayer(Player operatorPlayer) {
		int seat = operatorPlayer.getSeat();
		Player player = null;
		while(null == player || player.isHu())
		{
			if(seat == this.users.size() - 1)
				player = users.get(0);
			else
				player = users.get(seat + 1);
			seat = player.getSeat();
		}
		return player;
	}
	
	private int playingStateTimeout(PlayingState state)
	{
		int timeout = 10;
		switch (state) {
		case PUT_CARD:
			timeout = 0;
			break;
		case WAIT_PLAYER_OUT:
			timeout = 10;
		case WAIT_RESPONSE:
			timeout = 10;
		}
		return timeout * 1000;
	}
	
	/**
	 * 指定下一玩家和状态
	 * */
	public void nextPlayer(Player nextPlayer,PlayingState playingState)
	{
		this.currentPlayer = nextPlayer;
		timeoutInPlaying(playingState,playingStateTimeout(playingState));
	}
	
	
	
	private boolean hasRealPlayer()
	{
		for (Player player : users) 
		{
			if(player.getRobot() == Consts.FALSE)
				return true;
		}
		return false;
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
	
	public void timeout(int timeout)
	{
		this.timeout = timeout;
		this.startTime = System.currentTimeMillis();
	}
	
	public void timeoutInPlaying(PlayingState playingState,int timeout)
	{
		//此处先设置状态,后设置超时时间,可以解决本家手动打牌后,之后的玩家(目前是机器人)马上因为超时而直接出牌,
		//打牌后状态设置为摸牌,超时时间为0(应该是0引起的,考虑是否移除摸牌状态)
		//-bug?同时另一个BUG,和牌现在不能提示.不知是不是将状态先设置的原因,之前不存在此情况
		this.playingState = playingState;
		timeout(timeout);
	}
	
	private void delay(int timeout,State nextState)
	{
		this.state = State.DELAY;
		timeout(timeout);
		this.nextState = nextState;
	}
	
	private boolean isTimeout()
	{
		return System.currentTimeMillis() - startTime >= timeout;
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
