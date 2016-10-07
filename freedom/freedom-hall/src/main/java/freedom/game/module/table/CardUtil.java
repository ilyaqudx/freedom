package freedom.game.module.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freedom.game.module.table.entity.Card;
import freedom.game.module.table.entity.Operator;
import freedom.game.module.table.entity.Operator.OPT;
import freedom.game.module.table.entity.Player;
import freedom.game.module.table.entity.Table;


public class CardUtil {

	public static void main(String[] args) 
	{
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(1,1, 1));
		cards.add(new Card(1,1, 1));
		cards.add(new Card(1,1, 1));
		cards.add(new Card(1,1, 2));
		cards.add(new Card(1,1, 2));
		cards.add(new Card(1,1, 2));
		cards.add(new Card(1,1, 3));
		cards.add(new Card(1,1, 3));
		cards.add(new Card(1,1, 3));
		cards.add(new Card(1,1, 5));
		cards.add(new Card(1,1, 5));
		cards.add(new Card(1,1, 5));
		cards.add(new Card(1,1, 7));
		/*cards.add(new Card(1,1, 7));
		GameResult result = computeCardResult(cards);
		result.setZimo(true);
		result.setGangShangHua(true);
		result.setHaiDiLao(true);
		System.out.println(result.getMainType());
		int fan = getCardFan(result);
		System.out.println(fan + " 番");
		System.out.println((( 1 << fan ) * 5) + " gold");*/
		canHu(cards, new Card(1,1,9));
	}
	
	public static final boolean hasResponseAfterPutCard(Player p)
	{
		boolean gangFlag = p.isGangFlag();
		List<Card> handCard = p.getHandCard();
		boolean hu   = canHu(handCard);
		List<Operator> gang = canGangByPutCard(p);
		List<Operator> opts = new ArrayList<Operator>();
		if(hu)
			opts.add(new Operator(OPT.HU,p.getPutCard(),true,gangFlag ? Operator.MARK_GANG_SHANG_HUA : Operator.MARK_ZI_MO));
		if(!gang.isEmpty())
			opts.addAll(gang);
		p.setOpts(opts);
		return !opts.isEmpty();
	}
	
	/**
	 * 计算可响应玩家
	 * */
	public static final List<Player> hasResponse(Table table)
	{
		Player currentPlayer = table.getCurrentPlayer();
		boolean gangFlag = currentPlayer.isGangFlag();
		Card outCard = currentPlayer.getOutCard();
		List<Player> responsePlayers = new ArrayList<Player>();
		for (Player p : table.getUsers()) 
		{
			if(p.getId() == table.getCurrentPlayer().getId() 
					|| p.isHu())
				continue;
			List<Card> handCard = p.getHandCard();
			boolean hu   = canHu(handCard, outCard);
			boolean gang = canGangByOutCard(handCard,outCard);
			boolean peng = gang ? true : canPeng(handCard, outCard);
			List<Operator> opts = new ArrayList<Operator>();
			if(hu)
			{
				opts.add(new Operator(OPT.HU,outCard,false,gangFlag ? Operator.MARK_GANG_SHANG_PAO : Operator.MARK_DIAN_PAO));
			}
			if(gang)
				opts.add(new Operator(OPT.GANG, outCard ,false,Operator.MARK_ZHI_GANG));
			if(peng)
				opts.add(new Operator(OPT.PENG, outCard));
			if(!opts.isEmpty())
				responsePlayers.add(p);
			p.setOpts(opts);
		}
		return responsePlayers;
	}
	
	/**
	 * 自己摸牌后判断是否能杠(1-暗杠,2-吧杠)
	 * */
	private static final List<Operator> canGangByPutCard(Player player)
	{
		List<Card> handCard = player.getHandCard();
		List<List<Card>> pengOrGangCard = player.getPengCardList();
		int angangCount = 1;
		Card c = handCard.get(0);
		
		List<Operator> gangOperator = new ArrayList<Operator>();
		//暗杠
		for (int i = 1; i < handCard.size(); i++) 
		{
			if(sameCard(handCard.get(i), c))
			{
				angangCount++;
				if(angangCount == 4)
				{
					gangOperator.add(new Operator(OPT.GANG,c,true,Operator.MARK_AN_GANG) );
				}
			}else
			{
				c = handCard.get(i);
				angangCount = 1;
			}
		}
		
		//碰杠
		for (List<Card> list : pengOrGangCard) 
		{
			if(list.size() == 3)
			{
				for (Card card : handCard)
				{
					if(sameCard(list.get(0), card))
					{
						gangOperator.add(new Operator(OPT.GANG, card,true,Operator.MARK_PENG_GANG));
						break;
					}
				}
			}
		}
		return gangOperator;
	}
	
	private static final boolean canGangByOutCard(List<Card> handCard,Card outCard)
	{
		int count = 0;
		for (Card card : handCard) 
		{
			if(sameCard(card, outCard))
			{
				count++;
				if(count == 3)
					break;
			}
		}
		return count == 3;
	}
	
	private static final boolean canPeng(List<Card> handCard,Card outCard)
	{
		int count = 0;
		for (Card card : handCard) 
		{
			if(sameCard(card, outCard))
			{
				count++;
				if(count == 2)
					break;
			}
		}
		return count == 2;
	}

	public static final int getCardFan(GameResult result)
	{
		int fan = CardFan.cardTypeFan.get(result.getMainType()).fan;
		if(result.isHaiDiLao())
			fan += CardFan.HAI_DI_LAO.fan;
		if(result.isJiangDui())
			fan += CardFan.JIANG_DUI.fan;
		if(result.isJinGouDiao())
			fan += CardFan.JING_GOU_DIAO.fan;
		if(result.isYaoJiu())
			fan += CardFan.YAO_JIU.fan;
		if(result.getGangCount() > 0)
			fan += CardFan.GANG.fan * result.getGangCount();
		if(result.isZimo())
			fan += CardFan.ZI_MO.fan;
		if(result.isGangShangHua())
			fan += CardFan.GANG_SHANG_HUA.fan;
		if(result.isGangShangPao())
			fan += CardFan.GANG_SHANG_PAO.fan;
		if(result.isQiangGang())
			fan += CardFan.QIANG_GANG.fan;
		return fan;
	}
	
	public static final GameResult computeCardResult(List<Card> cards)
	{
		boolean qiDui     = isQiDui(cards);
		boolean qingYiSe  = isQingYiSe(cards);
		boolean daDui     = isDaDui(cards);
		int     gangCount = getGangCount(cards);
		boolean hasGang   = gangCount > 0;
		CardType type = CardType.COMMON;
		if(qingYiSe)
		{
			if(daDui)
				type = CardType.QING_DA_DUI;
			else if(qiDui)
				type = hasGang ? CardType.QING_LONG_QI_DUI : CardType.QING_QI_DUI;
			else
				type = CardType.QING_YI_SE;
		}
		else if(qiDui)
			type = hasGang ? CardType.LONG_QI_DUI : CardType.QI_DUI;
		else if(daDui)
			type = CardType.DA_DUI;
		
		GameResult result = new GameResult(type,isHaiDiLao(cards), isJingGouDiao(cards), isYaoJiu(cards), isJiangDui(cards), gangCount);
		result.setFan(getCardFan(result));
		return result;
	}
	
	private static final boolean isHaiDiLao(List<Card> cards)
	{
		return false;
	}
	
	private static final boolean isYaoJiu(List<Card> cards)
	{
		for (Card card : cards) 
			if(card.getValue() > 3 && card.getValue() < 7)
				return false;
		return true;
	}
	
	private static final boolean isJingGouDiao(List<Card> handCard)
	{
		return handCard.size() == 2;
	}
	
	private static final boolean isJiangDui(List<Card> cards)
	{
		for (Card card : cards) 
		{
			int value = card.getValue();
			if(value != 2 && value != 5 && value != 8)
				return false;
		}
		return true;
	}
	
	private static final boolean isDaDui(List<Card> handCard)
	{
		boolean hasDouble = false;
		int count = handCard.size();
		for (int i = 0; i < count - 1;) 
		{
			Card c1 = handCard.get(i);
			Card c2 = handCard.get(i+1);
			if(sameCard(c1, c2))
			{
				if(i == count - 2)
					return true;
				else if(sameCard(c1, handCard.get(i+2)))
					i += 3;
				else if(!hasDouble)
				{
					i += 2;
					hasDouble = true;
				}
				else
					return false;
			}else
				return false;
		}
		return true;
	}

	private static boolean isQiDui(List<Card> cards) 
	{
		int group = cards.size() / 2;
		for (int i = 0; i < group; i++) 
		{
			int index = i * 2;
			if(!sameCard(cards.get(index), cards.get(index + 1)))
				return false;
		}
		return true;
	}
	
	private static int getGangCount(List<Card> cards) 
	{
		int gangGroup = 0;
		int counter = 1;
		Card c = cards.get(0);
		for (int i = 1; i < cards.size(); i++)
		{
			Card c2 = cards.get(i);
			if(sameCard(c, c2))
			{
				counter++;
				gangGroup = counter == 4 ? gangGroup+1 : gangGroup;
			}
			else{
				counter = 1;
				c = cards.get(i);
			}
		}
		return gangGroup;
	}
	
	private static final boolean isQingYiSe(List<Card> cards)
	{
		int color = cards.get(0).getColor();
		for (int i = 1; i < cards.size(); i++) 
		{
			if(cards.get(i).getColor() != color)
				return false;
		}
		return true;
	}
	
	public static final boolean sameColor(int c1,int c2)
	{
		return c1 == c2;
	}
	
	public static final boolean sameCard(Card c1,Card c2)
	{
		if(sameColor(c1.getColor(), c2.getColor()))
			return c1.getValue() == c2.getValue();
		return false;
	}
	
	public static final boolean canHu(List<Card> handCard)
	{
		return canHu(handCard, null);
	}
	
	@SuppressWarnings("unused")
	public static boolean canHu(List<Card> handCard,Card outCard){
		if(null == handCard)return false;
		
		int count = null == outCard ? handCard.size() : handCard.size()+1;
		List<Card> tempHandCard = new ArrayList<Card>(count);
		
		tempHandCard.addAll(handCard);
		if(null != outCard)
			tempHandCard.add(outCard);
		Collections.sort(tempHandCard);
		for (Card card : tempHandCard)
		{
			System.out.println(card);
		}
		
		if(count == 2){
			if(sameCard(tempHandCard.get(0), tempHandCard.get(1)))
				return true;
		}else if(count == 14 && isQiDui(tempHandCard)){
			return true;
		}
		
		int startCardIndex = 0;
		Card firstCard = null;
		Card secondCard = null;
		Card thirdCard = null;
		boolean hasDouble = false;
		boolean allLinked = true;
		//copy card
		List<Card> checkCard = new ArrayList<Card>();
		checkCard.addAll(tempHandCard);
		
		
		
		while(checkCard.size() >= 2){
			hasDouble = false;
			checkCard.clear();
			checkCard.addAll(tempHandCard);
			for (int i = startCardIndex; i < checkCard.size() - 1; i++) {
				//check double
				if(sameCard(checkCard.get(i), checkCard.get(i+1))){
					Card c1 = checkCard.remove(i+1);	//344556 88 123789
											//-1:3556 88 123789
					Card c2 = checkCard.remove(i);
					hasDouble = true;
					startCardIndex = i+1;
					//System.out.println("找到将牌 :c1 : "+c1.toString()+",c2:"+c2.toString());
					break;
				}
			}
			
			if(hasDouble){
				//continue check
				boolean singleFail = false;//单轮判断失败
				while(true){
					if(checkCard.size()%3 != 0){
						return false;
					}
					for (int i = 0; i < checkCard.size()-2; i++) {
						firstCard = checkCard.get(i);
						secondCard = checkCard.get(i+1);
						thirdCard = checkCard.get(i+2);
						
						if(sameCard(firstCard, thirdCard)){
							checkCard.remove(thirdCard);
							checkCard.remove(secondCard);
							checkCard.remove(firstCard);
							//System.out.println("找到一沓牌 : " + firstCard.toString());
						}else{
							allLinked = checkLinkedCard(checkCard);
							
							if(!allLinked){
								//System.out.println("没有找到连牌,重新找将牌");
								singleFail = true;
							}
						}
						break;
					}
					
					if(checkCard.size() == 0){
						//check success 
						System.out.println("牌数量为 0 :成功");
						return true;
					}
					
					if(singleFail){
						break;
					}
				}
			}else{
				System.out.println("没有找到将牌,直接 失败");
				return false;
			}
		}
		return false;
	}
	
	private static boolean checkLinkedCard(List<Card> checkCard) {
		Card first = checkCard.get(0);
		Card second = null;
		Card third = null;
		for (int i = 1; i < checkCard.size(); i++) {
			if(null == second && sameColor(first.getColor(), checkCard.get(i).getColor())
					&& checkCard.get(i).getValue() - first.getValue() == 1){
				second = checkCard.get(i);
			}else if(null == third && sameColor(first.getColor(), checkCard.get(i).getColor())
					&& checkCard.get(i).getValue() - first.getValue() == 2){
				third = checkCard.get(i);
				break;
			}
		}
		//System.out.println("first:"+first+",second:"+second+",third:"+third);
		if(first == null || second == null || third == null){
			//System.out.println("没找到连牌");
			return false;
		}
		
		//find linked success remove card
		checkCard.remove(third);
		checkCard.remove(second);
		checkCard.remove(first);
		//System.out.println("找到连牌");
		return true;
		
	}
	
	public static final class GameResult
	{
		private CardType mainType;
		private boolean haiDiLao;
		private boolean jinGouDiao;
		private boolean yaoJiu;
		private boolean jiangDui;
		private int gangCount;
		private boolean gangShangPao;
		private boolean gangShangHua;
		private boolean zimo;
		private boolean qiangGang;
		private int fan;
		public GameResult(CardType mainType, boolean haiDiLao,
				boolean jinGouDiao, boolean yaoJiu, boolean jiangDui,
				int gangCount) {
			this.mainType = mainType;
			this.haiDiLao = haiDiLao;
			this.jinGouDiao = jinGouDiao;
			this.yaoJiu = yaoJiu;
			this.jiangDui = jiangDui;
			this.gangCount = gangCount;
		}
		
		public int getFan() {
			return fan;
		}

		public void setFan(int fan) {
			this.fan = fan;
		}

		public boolean isQiangGang() {
			return qiangGang;
		}

		public void setQiangGang(boolean qiangGang) {
			this.qiangGang = qiangGang;
		}

		public boolean isGangShangPao() {
			return gangShangPao;
		}

		public void setGangShangPao(boolean gangShangPao) {
			this.gangShangPao = gangShangPao;
		}

		public boolean isGangShangHua() {
			return gangShangHua;
		}

		public void setGangShangHua(boolean gangShangHua) {
			this.gangShangHua = gangShangHua;
		}

		public boolean isZimo() {
			return zimo;
		}

		public void setZimo(boolean zimo) {
			this.zimo = zimo;
		}

		public CardType getMainType() {
			return mainType;
		}
		public void setMainType(CardType mainType) {
			this.mainType = mainType;
		}
		public boolean isHaiDiLao() {
			return haiDiLao;
		}
		public void setHaiDiLao(boolean haiDiLao) {
			this.haiDiLao = haiDiLao;
		}
		public boolean isJinGouDiao() {
			return jinGouDiao;
		}
		public void setJinGouDiao(boolean jinGouDiao) {
			this.jinGouDiao = jinGouDiao;
		}
		public boolean isYaoJiu() {
			return yaoJiu;
		}
		public void setYaoJiu(boolean yaoJiu) {
			this.yaoJiu = yaoJiu;
		}
		public boolean isJiangDui() {
			return jiangDui;
		}
		public void setJiangDui(boolean jiangDui) {
			this.jiangDui = jiangDui;
		}
		public int getGangCount() {
			return gangCount;
		}
		public void setGangCount(int gangCount) {
			this.gangCount = gangCount;
		}
		@Override
		public String toString() {
			return "CardResult [mainType=" + mainType + ", haiDiLao="
					+ haiDiLao + ", jinGouDiao=" + jinGouDiao + ", yaoJiu="
					+ yaoJiu + ", jiangDui=" + jiangDui + ", gangCount="
					+ gangCount + "]";
		}
	}
	
	public static enum CardType{
		COMMON,
		DA_DUI,
		QI_DUI,
		LONG_QI_DUI,
		QING_YI_SE,
		QING_DA_DUI,
		QING_QI_DUI,
		QING_LONG_QI_DUI
	}
	
	public static enum CardFan{
		GANG(1),
		GANG_SHANG_HUA(1),
		GANG_SHANG_PAO(1),
		QIANG_GANG(1),
		ZI_MO(1),
		YAO_JIU(1),
		JIANG_DUI(1),
		JING_GOU_DIAO(1),
		HAI_DI_LAO(1),
		
		COMMON(0),
		DA_DUI(1),
		QI_DUI(2),
		LONG_QI_DUI(2),
		QING_YI_SE(2),
		QING_DA_DUI(3),
		QING_QI_DUI(4),
		QING_LONG_QI_DUI(4);
		
		
		private int fan;
		private static final Map<CardType, CardFan> cardTypeFan = new HashMap<CardType,CardFan>();
		static{
			cardTypeFan.put(CardType.COMMON, COMMON);
			cardTypeFan.put(CardType.DA_DUI, DA_DUI);
			cardTypeFan.put(CardType.QI_DUI, QI_DUI);
			cardTypeFan.put(CardType.LONG_QI_DUI, LONG_QI_DUI);
			cardTypeFan.put(CardType.QING_YI_SE, QING_YI_SE);
			cardTypeFan.put(CardType.QING_DA_DUI, QING_DA_DUI);
			cardTypeFan.put(CardType.QING_QI_DUI, QING_QI_DUI);
			cardTypeFan.put(CardType.QING_LONG_QI_DUI, CardFan.QING_LONG_QI_DUI);
		}
		
		CardFan(int fan)
		{
			this.fan = fan;
		}
	}
}


