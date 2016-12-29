package lesson2.question4;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lesson1.additional_2.ByteArrayHelper;
import lesson1.question4.RandomUtils;

import com.google.common.io.Files;

public class Application {

	public static void main(String[] args) throws IOException 
	{
		String file = "d:/salary.txt";
		int count = 10000000;
		write(count,file);
		read(file, count);
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
	
	/**全部用byte[]来处理
	 * 	name(2) + salary(8) + person(4) = 14
	 * 全部用基本数据类型数组来处理
	 * */
	private static final void read2(String file,int count) throws IOException
	{
		long rs = System.nanoTime();
		FileInputStream fis = new FileInputStream(file);
		FileChannel channel = fis.getChannel();
		long length = channel.size();
		//MappedByteBuffer byteBuffer = fis.getChannel().map(MapMode.READ_ONLY, 0, length);
		ByteBuffer byteBuffer = ByteBuffer.allocate((int) length);
		channel.read(byteBuffer);
		byteBuffer.flip();
		//byte[] bytes = byteBuffer.array();//java.nio.file.Files.readAllBytes(Paths.get(file));
		long re = System.nanoTime();
		System.out.println("read " + count + " line data cost :" + (re - rs));
		
		//分组
		long gs = System.nanoTime();
		//byte[] store = new byte[14 * count];
		ByteBuffer store = ByteBuffer.allocate(14 * 676);
		byte[] nameBytes = new byte[5];
		while(byteBuffer.hasRemaining())
		{
			byteBuffer.get(nameBytes);
			long salary = byteBuffer.getInt();
			int bonus  = byteBuffer.getInt();
			long allSalary = salary + bonus;
			byte name1 = (byte) (nameBytes[0] - 97);
			byte name2 = (byte) (nameBytes[1] - 97);
			int index = name1 * 26 * 14 + name2 * 14;
			store.put(index ++,name1);
			store.put(index ++,name2);
			store.putLong(index, store.getLong(index) + allSalary);
			index += 8;
			store.putInt(index, store.getInt(index) + 1);
			index += 4;
		}
		
		
		long ge = System.nanoTime();
		System.out.println(count + " line data group cost : " + (ge - gs) + " ns");
		long ss = System.nanoTime();
		byte[] array = store.array();
		sort(array, 0, 675);
		long se = System.nanoTime();
		System.out.println(26 * 26 +" group data sort cost : " + (se - ss) + " ns");
		
		for (int j = 0; j < 10; j++) 
		{
			byte[] name = new byte[]{(byte) (array[j*14+0] + 97),(byte) (array[j*14+1] + 97)};
			System.out.println(new String(name) +","+copyReadLong(array, j)+","+copyReadInt(array, j));
		}
	}
	
	private static final void sort(byte[] arr,int low,int high)
	{
		if(low < high)
		{
			int pvoit = partion(arr, low, high);
			sort(arr, low, pvoit - 1);
			sort(arr, pvoit + 1,high);
		}
	}
	
	private static final int partion(byte[] arr,int low,int high)
	{
		byte[] baseArray = Arrays.copyOfRange(arr, low * 14, (low +1) * 14);
		long base = copyReadLong(arr, low);
		while(low < high)
		{
			while(copyReadLong(arr, high) <= base && low < high)
				high--;
			if(low < high)
			{
				System.arraycopy(arr, high * 14, arr, low * 14, 14);
				low++;
			}
			while(copyReadLong(arr, low) >= base && low < high)
				low++;
			if(low < high)
			{
				System.arraycopy(arr, low * 14, arr, high * 14, 14);
				high--;
			}
		}
		System.arraycopy(baseArray, 0, arr, low * 14, 14);
		return low;
	}
	
	private static final long copyReadLong(byte[] arr,int from)
	{
		return ByteArrayHelper.writeLong(Arrays.copyOfRange(arr, from * 14 + 2, from * 14 + 10));
	}
	private static final long copyReadInt(byte[] arr,int from)
	{
		return ByteArrayHelper.writeInt(Arrays.copyOfRange(arr, from * 14 + 10, from * 14 + 14));
	}
	
	
	private static final void read(String file,int count) throws IOException
	{
		long rs = System.nanoTime();
		/*RandomAccessFile f = new RandomAccessFile(file, "r");
		FileChannel channel = f.getChannel();
		MappedByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, f.length());
		byte[] bytes = new byte[buffer.limit()];//
		buffer.get(bytes);*/
		byte[] bytes = java.nio.file.Files.readAllBytes(Paths.get(file));
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
			offset = i * 13;
			byte x = (byte) (bytes[offset++] - 97);
			byte y = (byte) (bytes[offset++] - 97);
			offset += 3;
			int salary = ByteArrayHelper.writeInt(Arrays.copyOfRange(bytes, offset, offset + 4));
			offset += 4;
			int bonus  = ByteArrayHelper.writeInt(Arrays.copyOfRange(bytes, offset, offset + 4));
			offset += 4;
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
	
	private static final void quicksort(int[][] counter,long[][] amounter,int low,int high)
	{
		for (int i = 0; i < amounter.length; i++)
		{
			for (int j = 0; j < amounter.length; j++) 
			{
				if(low < high)
				{ 
					int pviot = partion(counter,amounter,low,high);
					
				}
			}
		}
	}

	private static int partion(int[][] counter, long[][] amounter, int low,
			int high)
	{
		
		return 0;
	}

	private static void write(int count,String filePath) throws IOException
	{
		long cs = System.nanoTime();
		SalaryStore store = new SalaryStore(count);
		for (int i = 0; i < count; i++) 
		{
			store.add(new Salary(RandomUtils.randomString(5),RandomUtils.nextInt(999996) + 5,
					RandomUtils.nextInt(100001)));
		}
		long ce = System.nanoTime();
		System.out.println("create " + count + " line data cost : " + (ce - cs) + " ns");
		long s = System.nanoTime();
		Files.write(store.getBytes(),new File(filePath));
		long e = System.nanoTime();
		
		System.out.println("write  " + count + " line data to file cost : " + (e - s) + " ns");
	}
	
	private static final void groupByStream(int count)
	{
		long cs = System.nanoTime();
		List<Salary> store = new ArrayList<>(count);
		for (int i = 0; i < count; i++) 
		{
			store.add(new Salary(RandomUtils.randomString(2),RandomUtils.nextInt(999996) + 5,
					RandomUtils.nextInt(100001)));
		}
		long ce = System.nanoTime();
		System.out.println("create " + count +" line data cost : " + (ce - cs) + " ns");
		
		//Map<String,Long> counting  = store.stream().collect(Collectors.groupingBy(Salary::getName,Collectors.counting()));
		long gs = System.nanoTime();
		Map<String,Long> amounting = store.stream().collect(Collectors.groupingBy(Salary::getName,Collectors.summingLong(Salary::count)));
		long ge = System.nanoTime();
		System.out.println("use java 8 group " + count + " line data cost" + (ge - gs) + " ns");
		System.out.println("group size : " + amounting.size());
		long ccs = System.nanoTime();
		amounting.values().stream().sorted();
		long cce = System.nanoTime();
		System.out.println("sort cost : " + (cce -ccs) + " ns");
	}
}
