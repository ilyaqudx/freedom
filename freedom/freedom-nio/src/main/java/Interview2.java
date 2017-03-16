import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Interview2 {

	
	
	/**
	 * 题目:给定一个数n(1-100),算出2 ~ n-1进制所有位的总和
	 * 例子:
	 * 			n = 4
	 * 			2进制   100
	 * 			3进制      11
	 * 		总和为 : 1 + 1 + 1 = 3
	 * */
	
	
	public static final void main(String[] args)
	{
		
		final int n = 100;
		int temp = n;
		
		//首先求出进制数
		final List<Integer> count = new ArrayList<Integer>();
		for (int i = 2; i < n; i++) 
		{
			List<Integer> countList = new ArrayList<Integer>();
			temp = n;
			while(temp >= i){
				int mod = temp % i;
				countList.add(mod);
				temp /= i;
			}
			if(temp > 0)
				countList.add(temp);
			count.addAll(countList);
			System.out.println(String.format("%d 的 %d进制为%s", n,i,Arrays.toString(countList.toArray())));
		}
		
		int c = 0;
		for (int i : count) {
			c += i;
		}
		
		System.out.println("总和为:" + c);
		
	}
}
