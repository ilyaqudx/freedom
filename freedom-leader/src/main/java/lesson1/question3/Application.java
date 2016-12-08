package lesson1.question3;

public class Application {

	
	public static void main(String[] args) 
	{
		int rows = 10240;
		int cols = 10240;
		byte[][] array = new byte[rows][cols];
		
		long rowStart = System.nanoTime();
		long rowCount = rowFirst(array, rows, cols);
		long rowEnd = System.nanoTime();
		
		long colStart = System.nanoTime();
 		long colCount = colFirst(array, rows, cols);
		long colEnd = System.nanoTime();
		
		long rowCost = rowEnd - rowStart;
		long colCost = colEnd - colStart;
		System.out.println(String.format("行优先访问二维数组元素总数 : %d",rowCount));
		System.out.println(String.format("列优先访问二维数组元素总数 : %d",colCount));
		System.out.println(String.format("行优先访问时间全部元素耗时 : %d nano", rowCost));
		System.out.println(String.format("列优先访问时间全部元素耗时 : %d nano", colCost));
		System.out.println(String.format("行优先是列优先的%d倍", colCost / rowCost));
	
	}
	
	/**
	 * 行优先
	 * */
	public static final long rowFirst(byte[][] array,int rows,int cols)
	{
		long count = 0;
		for (int i = 0; i < rows; i++) 
		{
			for (int j = 0; j < cols; j++)
			{
				byte v = array[i][j];
				count++;
			}
		}
		return count;
	}
	
	/**
	 * 列优先
	 * */
	public static final long colFirst(byte[][] array,int rows,int cols)
	{
		long count = 0;
		for (int j= 0; j < cols; j++) 
		{
			for (int i = 0; i < rows; i++)
			{
				byte v = array[i][j];
				count++;
			}
		}
		return count;
	}
}
