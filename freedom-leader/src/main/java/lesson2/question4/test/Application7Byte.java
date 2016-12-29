package lesson2.question4.test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Arrays;

import lesson1.question4.RandomUtils;

public class Application7Byte {

	public static void main(String[] args) throws IOException 
	{
		String file = "d:/salary.txt";
		int count = 10000000;
		//write(count,file);
		long s = System.nanoTime();
		read(file, count);
		long e = System.nanoTime();
		System.out.println("all read : " + (e - s));
		//groupByStream(count);
		//add(count);
		//read2(file, count);
	}
	
	private static final void add(int count)
	{
		long as = System.nanoTime();
		int a = 0;
		for (int i = 0; i < count; i++) {
			a += 97;
			a -= 97;
		}
		long ae = System.nanoTime();
		System.out.println("add " + count + " time cost : " + (ae - as) + " ns");
	}
	
	private static final void read(String file,int count) throws IOException
	{
		long rs = System.nanoTime();
		RandomAccessFile f = new RandomAccessFile(file, "r");
		FileChannel channel = f.getChannel();
		MappedByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, f.length());
		byte[] bytes = new byte[buffer.limit()];//java.nio.file.Files.readAllBytes(Paths.get(file));
		buffer.get(bytes);
		long re = System.nanoTime();
		System.out.println("read " + count + " line data cost :" + (re - rs));
		
		//分组
		long gs = System.nanoTime();
		int[][] counter  = new int[26][26];
		long [][] amounter = new long [26][26];
		int i = 0;
		int offset = 0;
		while(i < count)
		{
			/*offset = i * 7;
			byte x = (byte) (buffer.get() - 97);
			byte y = (byte) (buffer.get() - 97);
			buffer.position(offset + 5);
			int salary = buffer.get();
			int bonus  = buffer.get();
			counter [x][y] ++;
			amounter[x][y] +=  salary * 13 + bonus;
			i++;*/
			offset = i * 7;
			byte x = (byte) (bytes[offset++] - 97);
			byte y = (byte) (bytes[offset++] - 97);
			offset += 3;
			int salary = bytes[offset++];
			int bonus  = bytes[offset++];//ByteArrayHelper.writeInt(Arrays.copyOfRange(bytes, offset, offset + 4));
			counter [x][y] ++;
			amounter[x][y] +=  salary * 13 + bonus;
			i++;
		}
		
		Group[] groupArray = new Group[676];
		for (int j = 0; j < 26; j++) 
		{
			for (int j2 = 0; j2 < 26; j2++) 
			{
				Group g = new Group(new String(new byte[]{(byte) (j + 97),(byte) (j2 + 97)}), amounter[j][j2], counter[j][j2]);
				groupArray[j * 26 + j2] = g;
			}
		}
		long ge = System.nanoTime();
		System.out.println(count + " line data group cost : " + (ge - gs) + " ns");
		long ss = System.nanoTime();
		Arrays.sort(groupArray);
		long se = System.nanoTime();
		System.out.println(26 * 26 +" group data sort cost : " + (se - ss) + " ns");
		
		for (int j = 0; j < 10; j++) 
		{
			System.out.println(groupArray[j]);
		}
	}
	
	private static void write(int count,String filePath) throws IOException
	{
		long cs = System.nanoTime();
		SalaryStore store = new SalaryStore(count);
		for (int i = 0; i < count; i++) 
		{
			store.add(new Salary(RandomUtils.randomString(5),(byte)RandomUtils.nextInt(123) + 5,
					(byte)RandomUtils.nextInt(128)));
		}
		long ce = System.nanoTime();
		System.out.println("create " + count + " line data cost : " + (ce - cs) + " ns");
		long s = System.nanoTime();
		//Files.write(store.getBytes(),new File(filePath));
		write(filePath, store.getBytes());
		long e = System.nanoTime();
		
		System.out.println("write  " + count + " line data to file cost : " + (e - s) + " ns");
	}
	
	private static final void write(String fileName,byte[] arr) throws IOException
	{
		RandomAccessFile file = new RandomAccessFile(fileName, "rw");
        long fileSize = arr.length;
        file.seek(fileSize);
        FileChannel fch = file.getChannel();
        MappedByteBuffer buffer  = fch.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
        buffer.put(arr);
        buffer.force();
        fch.close();
	}
}
