import java.util.Scanner;

public class Interview1 {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		String countString = scanner.nextLine();
		if(Integer.parseInt(countString) < 1)
			throw new IllegalArgumentException("n must be > 0");
		
		String items = scanner.nextLine();
		if(null == items | items.equals(""))
			throw new IllegalArgumentException("items is empty");
		
		int n = Integer.parseInt(countString);
		String[] cols = items.split(" ");
		if(cols.length != n)
			throw new IllegalArgumentException("items length must be =  " + n);
		
		//计算COLS的总数
		int count = 0;
		int[] intCols = new int[cols.length];
		for (int i = 0; i < cols.length; i++) 
		{
			intCols[i] = Integer.parseInt(cols[i]);
			count += intCols[i];
		}
		
		//检测count是否为n的倍数
		if(count % n != 0)
			throw new IllegalArgumentException("items total is not " + n +" times");
		int   avg = count / n;//求出每队的人数
		int[] arr = new int[cols.length];//存放每队现在还差平均数多少
		for (int i = 0; i < intCols.length; i++) 
		{
			arr[i] = intCols[i] - avg;
		}
		
		int step = 0;
		int moreIndex = -1;
		//开始计算需要的步数
		for (int i = 0; i < arr.length; i++)
		{
			int left = arr[i];
			if(left > 0 && moreIndex == -1)
				moreIndex = i;
			else if(left < 0){//如果队伍数量不足
				for (moreIndex = moreIndex == -1 ? (i == arr.length - 1 ? 0 : i + 1) : moreIndex; moreIndex < arr.length; )
				{
					int temp = arr[moreIndex];
					if(temp > 0){
						//有多余的数量
						int need = Math.min(-left, temp);//取最小的来计算步数
						step += Math.abs((moreIndex - i) * need);
						arr[i] = left += need;
						arr[moreIndex] = temp -= need;
						
						if(temp <= 0){
							//减去NEED后没有剩余,下一次则从下一队开始补
							moreIndex ++;
						}
						
						if(left == 0){
							//说明本队的数量已补足
							break;
						}
					}else
						moreIndex++;
					
				}
			}
		}
		
		System.out.println("need : " + step + " steps");
	
	}
}
