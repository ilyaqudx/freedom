package freedom.game.module.table.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import freedom.common.moudle.user.User;

public class Player {

	private long id;
	private String name;
	private int sex;
	private int seat;
	private long gold;
	private String head;
	private long roomId;
	private long tableId;
	private int ready;
	private int robot;
	private List<Card> handCard = new ArrayList<Card>();
	private LinkedList<List<Card>> pengCardList = new LinkedList<List<Card>>();
	private LinkedList<List<Card>> gangCardList = new LinkedList<List<Card>>();
	private List<Card> putCardList  = new ArrayList<Card>();
	private List<Card> outCardList  = new ArrayList<Card>();
	private int selectLackColor;
	private Card putCard;
	private Card outCard;
	private boolean gangFlag;//是否是杠牌后的操作(杠摸牌-杠上花,杠后打-杠上炮)
	private Card huCard;
	private Player dianPaoPlayer;
	private int huType;
	private boolean hu;
	public boolean isOperator;//是否已操作(结束时置为false)
	
	public static final int HU_TYPE_DIAN_PAO = 1,HU_TYPE_GANG_SHANG_PAO = 2,
			HU_TYPE_QIANG_GANG = 3 , HU_TYPE_GANG_SHANG_HUA = 4,
			HU_TYPE_ZI_MO = 5;
	
	public boolean isGangFlag() {
		return gangFlag;
	}
	public void setGangFlag(boolean gangFlag) {
		this.gangFlag = gangFlag;
	}
	public Player getDianPaoPlayer() {
		return dianPaoPlayer;
	}
	public void setDianPaoPlayer(Player dianPaoPlayer) {
		this.dianPaoPlayer = dianPaoPlayer;
	}
	public int getHuType() {
		return huType;
	}
	public void setHuType(int huType) {
		this.huType = huType;
	}
	private List<Operator> opts = new ArrayList<Operator>();
	public Card getHuCard() {
		return huCard;
	}
	public void setHuCard(Card huCard) {
		this.huCard = huCard;
	}
	public boolean hasOpt()
	{
		return !this.opts.isEmpty();
	}
	public boolean hasOpt(int opt)
	{
		if (hasOpt()) {
			for (Operator o : opts) 
				if(o.getOpt().ordinal() == opt)
					return true;
		}
		return false;
	}
	public List<Operator> getOpts() {
		return opts;
	}
	public void setOpts(List<Operator> opts) {
		this.opts = opts;
	}
	public Card getCard(int cardId)
	{
		for (Card card : handCard) 
		{
			if(card.getId() == cardId)
			{
				return card;
			}
		}
		return null;
	}
	public boolean isHu() {
		return hu;
	}

	/**
	 * @param hu			胡标记
	 * @param diaoPaoPlayer	点炮玩家
	 * @param huCard		点炮的牌
	 * @param huType		胡的类型
	 */
	public void setHu(boolean hu,Player diaoPaoPlayer,Card huCard,int huType) {
		this.hu = hu;
		this.huCard = huCard;
		this.huType = huType;
		this.dianPaoPlayer = diaoPaoPlayer;
	}

	public void putCard(Card card)
	{
		this.putCard = card;
		this.handCard.add(card);
		Collections.sort(handCard);
	}
	
	/**由AI自动出牌
	 * @return
	 */
	public Card autoOutCard()
	{
		Card card = handCard.get(0/*handCard.size() -1*/);
		outCard(card,false);
		return card;
	}
	
	/**
	 * 玩家手动出牌,记录下来
	 * */
	public void addOutCard(Card card)
	{
		this.outCard = card;
	}
	
	public void outCard(Card outCard,boolean gangFlag)
	{
		this.gangFlag = gangFlag;
		boolean remove = handCard.remove(outCard);
		System.out.println("本家出牌删除出牌结果: " + remove + " : " + outCard);
	}
	
	
	public Card getPutCard() {
		return putCard;
	}
	public void setPutCard(Card putCard) {
		this.putCard = putCard;
	}
	public Card getOutCard() {
		return outCard;
	}
	public void setOutCard(Card outCard) {
		this.outCard = outCard;
	}
	public boolean isSelectedLackColor()
	{
		return selectLackColor != 0;
	}
	public int getSelectLackColor() {
		return selectLackColor;
	}

	public void setSelectLackColor(int selectLackColor) {
		this.selectLackColor = selectLackColor;
	}

	public int getRobot() {
		return robot;
	}

	public void setRobot(int robot) {
		this.robot = robot;
	}

	public int getReady() {
		return ready;
	}

	public void setReady(int ready) {
		this.ready = ready;
	}

	

	public List<Card> getHandCard() {
		return handCard;
	}
	public void setHandCard(List<Card> handCardList) {
		this.handCard = handCardList;
	}
	public LinkedList<List<Card>> getPengCardList() {
		return pengCardList;
	}
	public void setPengCardList(LinkedList<List<Card>> pengCardList) {
		this.pengCardList = pengCardList;
	}
	public LinkedList<List<Card>> getGangCardList() {
		return gangCardList;
	}
	public void setGangCardList(LinkedList<List<Card>> gangCardList) {
		this.gangCardList = gangCardList;
	}
	public List<Card> getPutCardList() {
		return putCardList;
	}
	public void setPutCardList(List<Card> putCardList) {
		this.putCardList = putCardList;
	}
	public List<Card> getOutCardList() {
		return outCardList;
	}
	public void setOutCardList(List<Card> outCardList) {
		this.outCardList = outCardList;
	}
	public Player()
	{
	}
	public Player(User user)
	{
		BeanUtils.copyProperties(user, this);
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getSeat() {
		return seat;
	}
	public void setSeat(int seat) {
		this.seat = seat;
	}
	public long getGold() {
		return gold;
	}
	public void setGold(long gold) {
		this.gold = gold;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + ", sex=" + sex
				+ ", seat=" + seat + ", gold=" + gold + ", head=" + head + "]";
	}
}
