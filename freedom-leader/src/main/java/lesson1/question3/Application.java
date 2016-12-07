package lesson1.question3;

public class Application {

	
	public static void main(String[] args) 
	{
		int rows = 10240;
		int cols = 10240;
		byte[][] array = new byte[rows][cols];
		
		long rowStart = System.nanoTime();
		rowFirst(array, rows, cols);
		long rowEnd = System.nanoTime();
		
		long colStart = System.nanoTime();
		colFirst(array, rows, cols);
		long colEnd = System.nanoTime();
		
		long rowCost = rowEnd - rowStart;
		long colCost = colEnd - colStart;
		System.out.println(String.format("行优先访问时间全部元素耗时 : %d nano", rowCost));
		System.out.println(String.format("列优先访问时间全部元素耗时 : %d nano", colCost));
		System.out.println(String.format("行优先是列优先的%d倍", colCost / rowCost));
	
	}
	
	/**
	 * 行优先
	 * */
	public static final void rowFirst(byte[][] array,int rows,int cols)
	{
		for (int i = 0; i < rows; i++) 
		{
			for (int j = 0; j < cols; j++)
			{
				byte v = array[i][j];
			}
		}
	}
	
	/**
	 * 列优先
	 * */
	public static final void colFirst(byte[][] array,int rows,int cols)
	{
		for (int i = 0; i < cols; i++) 
		{
			for (int j = 0; j < rows; j++)
			{
				byte v = array[j][i];
			}
		}
	}
}
