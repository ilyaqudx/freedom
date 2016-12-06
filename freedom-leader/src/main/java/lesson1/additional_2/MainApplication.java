package lesson1.additional_2;

import java.util.Random;

import lesson1.question5.MyItem;

public class MainApplication {

	public static void main(String[] args) throws Exception 
	{
		question2();
	}
	
	public static final void question2() throws Exception
	{
		IntStore store = new IntStore();
		Random r = new Random();
		for (int i = 0; i < 1000; i++)
		{
			store.putMyItem(i, new MyItem((byte)r.nextInt(128),(byte)r.nextInt(128),(byte)r.nextInt(128)));
		}
		
		int size = store.size();
		for (int i = 0; i < size; i++) 
		{
			System.out.println(store.getMyItem(i));
		}
		
		System.out.println("========我是排序分隔线=============");
		
		store.sort();
		
		for (int i = 0; i < size; i++) 
		{
			System.err.println(store.getMyItem(i));
		}
	}
}
