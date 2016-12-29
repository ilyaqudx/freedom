package lesson2.question4.test2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Arrays;
import java.util.Random;

import lesson1.question4.RandomUtils;
import lesson2.question4.Group;

public class SalaryApp {
	

    public static void main(String[] args) throws IOException 
    {
    	int count = 10000000;
        long d=System.currentTimeMillis();
        createAndwrite("d://salary.txt",count);
        System.out.println("插入用时："+(System.currentTimeMillis()-d));
        
        long t0 = System.nanoTime();
        readAndGroup("d://salary.txt",count);
        long t1 = System.nanoTime();
        System.err.println("读取/分组/排序/输出 总耗时:"+(t1 - t0) + " ns");
    }
    
    static final Random random = new Random();
    
    private static final byte[] randomAsciiBytes(int len)
	{
		byte[] bytes = new byte[len];
		for (int i = 0; i < bytes.length; i++) 
		{
			// 0-25  代替  a-z
			bytes[i] = (byte)(random.nextInt(26)); 
		}
		return bytes;
	}
    
    private static void createAndwrite(String fileName, int count) 
    {
        try 
        {
            RandomAccessFile file = new RandomAccessFile(fileName, "rw");
            long fileSize = count * 13;
            file.seek(fileSize);
            FileChannel fch = file.getChannel();
            MappedByteBuffer buffer  = fch.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
            for (int i = 0; i < count; i++) 
            {
                int salary = RandomUtils.nextInt(999996) + 5;
                int bonus  = RandomUtils.nextInt(100001);
              
                buffer.put(randomAsciiBytes(5));//5个字节名称
                buffer.putInt(salary);//4个字节
                buffer.putInt(bonus);//4个字节
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void readAndGroup(String file,int count) throws IOException
    {
    	//读取文件
    	long rs = System.nanoTime();
		ByteBuffer buffer = read(file);
		long re = System.nanoTime();
		System.err.println("read " + count + " line data cost :" + (re - rs));
		
		//分组
		long gs = System.nanoTime();
		Group[] groupArray = group(count, buffer);
		long ge = System.nanoTime();
		System.err.println(count + " line data group cost : " + (ge - gs) + " ns");
		
		//排序
		long ss = System.nanoTime();
		Arrays.sort(groupArray);
		long se = System.nanoTime();
		System.out.println(26 * 26 +" group data sort cost : " + (se - ss) + " ns");
		
		//输出
		for (int j = 0; j < 10; j++) 
			System.out.println(groupArray[j]);
    }

	private static ByteBuffer read(String file)throws IOException 
	{
		RandomAccessFile f = new RandomAccessFile(file, "r");
		FileChannel channel = f.getChannel();
		return channel.map(MapMode.READ_ONLY, 0, f.length());
	}
    
    private static Group[] group(int count, ByteBuffer buffer) 
	{
    	//2维数组存储分组信息,外层下标和内层下标分别为name第一个字符和第二个字符
    	int  [][] counter  = new int  [26][26];//总人数
		long [][] salarys = new long [26][26];//总金额
		int i = 0;
		int offset = 0;
		while(i < count){
			offset = i * 13;
			byte x = buffer.get();
			byte y = buffer.get();
			buffer.position(offset + 5);
			//时间从430ms -> 130ms左右.原因就在于这两个转换.之前先要复制字节数组,然后再转成int进行累加操作.但是如果不需要转,本身就用byte可运算,直接操作数组会比ByteBuffer性能好
			int salary = buffer.getInt();  
			int bonus  = buffer.getInt();
			counter [x][y] ++;//人数累加
			salarys [x][y] +=  (salary * 13 + bonus);//总金额累加
			i++;
		}
		
		Group[] groupArray = new Group[676];
		for (int j = 0; j < 26; j++) {
			for (int j2 = 0; j2 < 26; j2++) 
			{
				Group g = new Group(new String(new byte[]{(byte) (j + 97),(byte) (j2 + 97)}), salarys[j][j2], counter[j][j2]);
				groupArray[j * 26 + j2] = g;
			}
		}
		return groupArray;
	}
}
