package lesson1.question5;

import java.util.Random;

public class Main {

	public static final void main(String[] args) throws Exception
	{
		question52();
	}

	private static void question51() {
		MyItem item1 = new MyItem((byte)1,(byte) 1,(byte) 1);
		MyItem item2 = new MyItem((byte)2,(byte) 2,(byte) 2);
		MyItem item3 = new MyItem((byte)3,(byte) 3,(byte) 3);
		
		ByteStore store = new ByteStore();
		try {
			store.putMyItem(0, item1);
			store.putMyItem(1, item2);
			store.putMyItem(2, item3);
			MyItem item4 = store.getMyItem(0);
			MyItem item5 = store.getMyItem(1);
			MyItem item6 = store.getMyItem(2);
			System.out.println(item1.equals(item4));
			System.out.println(item2.equals(item5));
			System.out.println(item3.equals(item6));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static final void question52() throws Exception
	{
		ByteStore store = new ByteStore();
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
