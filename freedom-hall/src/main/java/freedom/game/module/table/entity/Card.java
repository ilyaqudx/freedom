package freedom.game.module.table.entity;

import java.util.ArrayList;
import java.util.List;

public class Card implements Comparable<Card>{

	private int id;
	private int color;
	private int value;
	public static final int COLOR_TONG = 1,COLOR_TIAO = 2,COLOR_WAN = 3;
	public Card(int id, int color, int value)
	{
		this.id = id;
		this.color = color;
		this.value = value;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getCode()
	{
		return color * value;
	}
	public String colorString()
	{
		if(color == COLOR_TONG)
			return value + "筒";
		else if(color == COLOR_TIAO)
			return value + "条";
		else 
			return value + "万";
	}
	@Override
	public String toString() {
		return "Card[id = " + id + " , value = " + colorString() + "]";
	}
	
	public static void main(String[] args) {
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(1, 1, 2));
		list.add(new Card(2, 1, 7));
		list.add(new Card(3, 3, 3));
		list.add(new Card(4, 2, 9));
		list.add(new Card(5, 1, 1));
		list.add(new Card(6, 2, 7));
		list.add(new Card(7, 3, 3));
		
		
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = i + 1; j < list.size(); j++) {
				Card c1 = list.get(i);
				Card c2 = list.get(j);
				if(c1.getColor() > c2.getColor())
				{
					list.set(i, c2);
					list.set(j, c1);
				}
				else if(c1.getColor() == c2.getColor())
				{
					if(c1.getValue() > c2.getValue())
					{
						list.set(i, c2);
						list.set(j, c1);
					}
				}
			}
		}
		
		for (Card card : list) {
			System.out.println(card);
		}
	}
	@Override
	public int compareTo(Card o)
	{
		if(this.color > o.getColor())
			return 1;
		else if(this.color == o.getColor())
			return this.value > o.getValue() ? 1 : -1;
		return 0;
	}
	
}
