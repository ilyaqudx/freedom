package freedom.jdfs;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
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
		for (int i = 0; i < 10; i++) 
		{
			new UploadTask().start();
		}
	}
	
	static class UploadTask extends Thread{
		
		long successCount = 0;
		long totalTime = 0;
		
		@Override
		public void run() {
			try
			{
				execute();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		private void execute() throws IOException
		{
			final int count = 500;
			SocketChannel channel = SocketChannel.open();
			channel.setOption(StandardSocketOptions.SO_RCVBUF, 32 *1024);
			channel.connect(new InetSocketAddress("localhost", 23000));
			
			//FileChannel fileChannel = FileChannel.open(Paths.get("F:\\迅雷下载\\mysql-5.7.17-1.el7.x86_64.rpm-bundle.tar"));
			FileChannel fileChannel = FileChannel.open(Paths.get("3.png"));
			int fileLength = (int) fileChannel.size();
			ByteBuffer buffer = ByteBuffer.allocate(2 * 1024 * 1024);
			long ss = System.currentTimeMillis();
			for (int i = 0; i <count; i++) {
				process(channel, fileChannel, fileLength, buffer);
			}
			channel.close();
			long ee = System.currentTimeMillis();
			String s = String.format("total files : %d,success files : %d,total time : %d,avg time : %d", count,successCount,(ee-ss),(totalTime/count));
			System.out.println(s);
		}
		
		private void process(SocketChannel channel, FileChannel fileChannel,int fileLength, ByteBuffer buffer) throws IOException 
		{
			fileChannel.position(0);
			buffer.clear();
			buffer.putLong(1 + 8 + 6 + fileLength);
			buffer.put((byte) 11);
			buffer.put((byte) 0);
			
			//data
			buffer.put((byte) 0);//store_path_index
			buffer.putLong(fileLength);//文件长度
			buffer.put("png\0\0\0".getBytes());//扩展名  6字节,这儿多传了一个点.服务器保存后就在文件中多保存了一个字节,在第一个字节处.这个是服务器有问题.
			
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
			
			//System.out.println("上传文件成功 ,总共耗时 : " + (endTime - start) + "ms");
			ByteBuffer readBuffer = ByteBuffer.allocate(256 * 1024);
			try {
				
				int len = channel.read(readBuffer);
				//System.out.println("接收到服务器返回数据 : " + len + ",buffer = " + readBuffer);
				if(len > 0){
					readBuffer.flip();
					long pkgLen = readBuffer.getLong();
					byte cmd    = readBuffer.get();
					byte errno  = readBuffer.get();
					byte[] groupNameBuffer = new byte[16];
					readBuffer.get(groupNameBuffer);
					int endIndex = -1;
					for (int i = 0; i < groupNameBuffer.length; i++) {
						if(groupNameBuffer[i] == '\0'){
							endIndex = i;
							break;
						}
					}
					String groupName = new String(groupNameBuffer,0,endIndex);
					byte[] fileNameBuffer = new byte[(int) (pkgLen - 16)];
					readBuffer.get(fileNameBuffer);
					String filename = new String(fileNameBuffer);
					//System.out.println("cmd : " +cmd+ ",group : " + groupName + ",filename : " + filename);
					long endTime = System.currentTimeMillis();
					totalTime += (endTime - start);
					successCount++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
