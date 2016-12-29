package lesson2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Created by Administrator on 2016/12/9 0009.
 */
public class Salary {
    private static byte[] name = new byte[5];
    private static Random rand = new Random(System.currentTimeMillis());
    private static final int dictSize = 26 * 25;
    private static void gen(String fileName, int number) {
        try {
            RandomAccessFile file = new RandomAccessFile(fileName, "rw");
            long fileSize = number * 7;
            file.seek(fileSize);
            FileChannel fch = file.getChannel();
            MappedByteBuffer buffer  = fch.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
            for (int i = 0; i < number; i++) {
                int salary = rand.nextInt(10) + 1;
                int bonus  = rand.nextInt(6);
              
                buffer.put(getRandomStr(5).getBytes("ASCII"));
                buffer.put((byte) salary);
                buffer.put((byte) bonus);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static String getRandomStr(int length) {
        char[] cResult = new char[length];  
        int i = 0;  
        while (i < length) {  
            i = i % length;  
            cResult[i]=(char) ('a'+Math.random()*26);
            i++;  
        }  
        return new String(cResult);  
    }
   static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= (1<<30)) ?(1<<30) : n + 1;
    }
    private static byte[] prefix = new byte[5];
    private static int getIndex() {
        return (prefix[0]<<8 | prefix[1])&(tableSizeFor(dictSize)-1);
    }
 

    private static ByteBuffer dict = ByteBuffer.allocateDirect(1024*10);
    
    private static void group(String fileName) throws UnsupportedEncodingException {
        
        try {
        	long gs = System.nanoTime();
            RandomAccessFile file = new RandomAccessFile(fileName, "r");
            FileChannel fch = file.getChannel();
            MappedByteBuffer buffer = fch.map(FileChannel.MapMode.READ_ONLY,0, file.length());
            while (buffer.position() < buffer.limit()) {
                buffer.get(prefix);
                byte salary = buffer.get();
                byte bonus = buffer.get();
                int index = getIndex()*10;
                int yearSalary = ((salary << 3) + (salary << 2) + salary + bonus);
              
             
                int oldTotal  = dict.getInt(index+2);
                int oldCount  = dict.getInt(index + 6);
                dict.put(index,prefix[0]);
                dict.put(index+1,prefix[1]);
                dict.putInt(index+2, oldTotal + yearSalary);
                dict.putInt(index + 6, oldCount + 1);
              
              
               
            }
            long ge = System.nanoTime();
            System.out.println("group cost time : "  + (ge - gs));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PriorityQueue<ByteBuffer> queue = new PriorityQueue<>(10,
                (o1, o2) -> Integer.compare(o2.getInt(2), o1.getInt(2)));

        for(int i = 0; i < dictSize; i++) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(10);
            int current = i*10;
            buffer.putChar(  dict.getChar(current));
            buffer.putInt(dict.getInt(current+2));
            buffer.putInt(dict.getInt(current+ 6));;
            buffer.position(0);
            queue.add(buffer);
        }
        StringBuilder bb;
        for(int i = 0; i < 10; i++) {
            ByteBuffer buffer  = queue.poll();
            byte[] prefix = new byte[2];
            buffer.get(prefix);
            int total = buffer.getInt();
            int count = buffer.getInt();
           bb  = new StringBuilder();
            bb.append(new String(prefix,"ASCII"));
            bb.append(",");
            bb.append(total);
            bb.append(",");
            bb.append(count);
            System.out.println(bb.toString());
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
 
        long d=System.currentTimeMillis();
        gen("d://test.txt",10000000);
        System.out.println("插入用时："+(System.currentTimeMillis()-d));
        long t0 = System.currentTimeMillis();
        group("d://test.txt");
        System.out.println("聚合用时:"+(System.currentTimeMillis() - t0));
    }
}
