package lesson1.question4;

import java.util.Random;

public class RandomUtils {

	public static final String chars = "abcdefghijklmnopqrstuvwxyz";
	
	/**
	 * 产生n位字符串
	 * */
	public static final String randomString(int len)
	{
		Random r = new Random();
		int charLen = chars.length();
		char[] cs = new char[len];
		for (int i = 0; i < len; i++) {
			cs[i] = chars.charAt(r.nextInt(charLen));
		}
		return new String(cs);
	}
	
	public static final int nextInt(int bound)
	{
		return new Random().nextInt(bound);
	}
}
