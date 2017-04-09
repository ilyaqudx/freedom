package freedom.jdfs;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import freedom.jdfs.storage.Globle;

/**
 * 直接上传文件到Stroage,不通过Tracker查询
 * */
public class TestStorageUploadFileWithoutTracker {

	
	public static void main(String[] args) throws Exception 
	{
		for (int i = 0; i <256; i++) {
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
		}
	}
	
	private static final void concurrentTest() throws IOException
	{
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress("localhost", 23000));
		
		byte[] data = Files.readAllBytes(Paths.get("D:/迅雷下载/filedir/azy-tool-0.0.1.jar"));
		
		byte[] dataLenBuff = Globle.long2buff(data.length);
		
		OutputStream out = socket.getOutputStream();//135,556
		//header
		out.write(Globle.long2buff(1 + 8 + 6 + data.length));
		out.write(1);
		out.write(11);
		
		//data
		out.write(0);//store_path_index
		out.write(dataLenBuff);//文件长度
		out.write("sql\0\0\0".getBytes());//扩展名  6字节
		out.write(data);//文件数据
		out.flush();
		
		socket.getInputStream().read();
	}
}
