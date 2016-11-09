package freedom.game.module.table.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Lists;

import freedom.game.module.table.GameLogic;
import freedom.game.module.table.Logic;
import freedom.game.module.table.state.DelayState;
import freedom.game.module.table.state.OutCardState;
import freedom.game.module.table.state.PutCardState;
import freedom.game.module.table.state.ResponseState;
import freedom.game.module.table.state.SelectLackCardState;
import freedom.game.module.table.state.SettlementState;
import freedom.game.module.table.state.StartGameState;
import freedom.game.module.table.state.State;
import freedom.game.module.table.state.WaitPlayerState;
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
	private Logic logic;
	public volatile freedom.game.module.table.state.State state;
	public volatile freedom.game.module.table.state.State nextState;
	private List<Player> users = new ArrayList<Player>();
	public List<Card> tableCard = new ArrayList<Card>();
	public Player currentPlayer;
	private int firstSeat;
	public int timeout;
	public volatile long lastInTime;
	public volatile long delayStart = 10000;
	//响应类型(1-摸牌响应,2-打牌响应)
	public int responseType;
	//一轮中可响应列表
	public Map<Long, ResponseInfo> responseList = new HashMap<Long, ResponseInfo>();
	
	public static final int RESPONSE_TYPE_IN = 1,RESPONSE_TYPE_OUT = 2;
	
	public freedom.game.module.table.state.State WAIT_PLAYER,START_GAME,SELECT_LACK,PUT_CARD,RESPONSE,OUT_CARD,SETTLEMENT,DELAY;
	
	public Table(Room room)
	{
		this.room = room;
		this.logic= new GameLogic(this);
		this.WAIT_PLAYER = new WaitPlayerState(this);
		this.START_GAME  = new StartGameState(this);
		this.SELECT_LACK = new SelectLackCardState(this);
		this.PUT_CARD    = new PutCardState(this);
		this.RESPONSE    = new ResponseState(this);
		this.OUT_CARD    = new OutCardState(this);
		this.SETTLEMENT  = new SettlementState(this);
		this.DELAY       = new DelayState(this);
		this.init();
	}

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

	/**
	 * 初始化桌子
	 * */
	private void init()
	{
		logic.initCard();
		logic.setState(WAIT_PLAYER);
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

	public State getState()
	{
		return state;
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
	
	public TableScene getScene()
	{
		return new TableScene(this);
	}
	
	
	
	
	/**2016-10-21 00:18
	 * 现在状态单独来出来处理逻辑,并且各个状态之间可以相互转换且并不是单一转换.
	 * 
	 * 仍然存在的问题:
	 * 	如果现在需要增加一个状态,那么在结构上只需要新增加一个状态类,并添加到TABLE中即可
	 *  但是在逻辑上来说,因为状态之间的走向基本是强关联的,所以如果需要在中间插入一个新的状态
	 *  改动非常大,且容易出错.这个问题需要好好的思考,如果思考完了,可以作为遇到了什么困难来回答.
	 *  
	 *  --比如在现在有麻将流程逻辑中,在正式打牌前进行换3张这个逻辑如果实现?
	 *  
	 *  采用状态模式来处理游戏的状态逻辑是非常好的,应该记录下来,作为对项目的改进,设计
	 * */
	public void update()
	{
		state.action();
	}
	
	public int getSeatNumber()
	{
		return seat.get();
	}
	
	public Player getPlayer(long playerId)
	{
		for (Player player : getUsers()) 
		{
			if(playerId == player.getId())
				return player;
		}
		return null;
	}
	
	

	public Logic getLogic() {
		// TODO Auto-generated method stub
		return this.logic;
	}

	public List<Integer> getEmptySeats() 
	{
		return emptySeats;
	}

	public List<Integer> getSitedSeats()
	{
		return sitedSeats;
	}
}
