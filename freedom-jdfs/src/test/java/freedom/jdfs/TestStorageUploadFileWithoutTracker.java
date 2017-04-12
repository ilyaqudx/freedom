package freedom.jdfs;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;

/**
 * 直接上传文件到Stroage,不通过Tracker查询
 * */
public class TestStorageUploadFileWithoutTracker {

	
	public static void main(String[] args) throws Exception 
	{
		for (int i = 0; i < 1; i++) {
			new Thread(new Runnable() {
				public void run() {
					try {
						concurrentTest();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			//Thread.sleep(1000);
		}
	}
	
	private static final void concurrentTest() throws IOException
	{
		
		SocketChannel channel = SocketChannel.open();
		boolean connected = channel.connect(new InetSocketAddress("localhost", 23000));
		
		FileChannel fileChannel = FileChannel.open(Paths.get("5.jpg"));
		int fileLength = (int) fileChannel.size();
		ByteBuffer buffer = ByteBuffer.allocate(2 * 1024 * 1024);
		buffer.putLong(1 + 8 + 6 + fileLength);
		buffer.put((byte) 1);
		buffer.put((byte) 11);
		
		//data
		buffer.put((byte) 0);//store_path_index
		buffer.putLong(fileLength);//文件长度
		buffer.put("jpg\0\0\0".getBytes());//扩展名  6字节,这儿多传了一个点.服务器保存后就在文件中多保存了一个字节,在第一个字节处.这个是服务器有问题.
		
		long start = System.currentTimeMillis();
		int offset = 0;
		int end    = 10 + 1 + 8 + 6 + fileLength;
		
		while(offset < end)
		{
			//先检查需要读多少数据
			fileChannel.read(buffer);
			buffer.flip();
			int len = channel.write(buffer);
			offset += len;
			//System.out.println(String.format("【本次写出数据  : %d , 累计写出数据  : %d】",len,offset));
			if(buffer.hasRemaining()){
				buffer.compact();
			}else
				buffer.clear();//重新读
		}
		long endTime = System.currentTimeMillis();
		System.out.println("上传文件成功 ,总共耗时 : " + (endTime - start) + "ms");
		try {
			synchronized (Thread.currentThread()) {
				Thread.currentThread().wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
