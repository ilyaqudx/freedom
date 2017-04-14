package freedom.jdfs;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 直接上传文件到Stroage,不通过Tracker查询
 * */
public class TestStorageDownloadFileWithoutTracker {

	
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
		channel.configureBlocking(true);
		channel.connect(new InetSocketAddress("localhost", 23000));
		
		ByteBuffer buffer = ByteBuffer.allocate(86);
		buffer.putLong(8 + 8 + 16 + 44);
		buffer.put((byte) 14);
		buffer.put((byte) 0);
		
		//data
		buffer.putLong(0);//offset
		buffer.putLong(0);//downloadBytes
		buffer.put("group1\0\0\0\0\0\0\0\0\0\0".getBytes());
		buffer.put("M00/00/00/AAAAAAAAAACAYGDHAAAAAGssY98=hU.tar".getBytes());
		buffer.flip();
		channel.write(buffer);
		
		ByteBuffer readBuffer = ByteBuffer.allocate(256 * 1024);
		try {
			
			readBuffer.limit(10);
			while(true){
				int len = channel.read(readBuffer);
				if(len < 10){
					Thread.sleep(50);
					continue;
				}
				break;
			}
			readBuffer.flip();
			long pkgBodyLen = readBuffer.getLong();
			byte cmd   = readBuffer.get();
			byte errno = readBuffer.get();
			RandomAccessFile file = new RandomAccessFile("d:/download.tar", "rw");
			file.setLength(pkgBodyLen);
			System.out.println("文件总长度 : " + pkgBodyLen);
			long start = System.currentTimeMillis();
			if(pkgBodyLen > 0){
				readBuffer.clear();
				int offset = 0;
				int written = 0;
				while(offset < pkgBodyLen){
					offset += channel.read(readBuffer);
					//System.out.println("offset  = " + offset + " , [written] : " + written);
					if(offset >= pkgBodyLen || !readBuffer.hasRemaining()){
						readBuffer.flip();
						file.seek(written);
						written += file.getChannel().write(readBuffer);
						readBuffer.clear();
					}
				}
				//System.out.println("offset  = " + offset + " , [written] : " + written);
				file.close();
				long end = System.currentTimeMillis();
				System.out.println("file download success : d:/download.tar , cost time : " +(end - start));
			}else
				throw new Exception("文件长度错误 : " + pkgBodyLen);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			channel.close();
		}
	}
}
