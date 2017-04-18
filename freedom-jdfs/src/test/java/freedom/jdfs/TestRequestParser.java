package freedom.jdfs;

import java.nio.charset.Charset;

import freedom.jdfs.common.Header;
import freedom.jdfs.common.Packet;
import freedom.jdfs.common.buffer.IoBuffer;
import freedom.jdfs.protocol.RequestParser;

public class TestRequestParser {

	
	public static void main(String[] args) throws Exception {
		
		IoBuffer buffer = IoBuffer.allocate(1024);
		buffer.put((byte)1);
		buffer.putString("jpg\0\0\0", Charset.forName("UTF-8").newEncoder());
		buffer.putLong(100);
		buffer.flip();
		
		
		Packet request = RequestParser.parse(new Header(0, (byte)11, (byte)0), buffer);
		
		System.out.println(request);
		
		buffer.clear();
		buffer.putLong(0);
		buffer.putLong(0);
		buffer.putString("group1\0\0\0\0\0\0\0\0\0\0",  Charset.forName("UTF-8").newEncoder());
		buffer.putString("abcdefgh.jpg", Charset.forName("UTF-8").newEncoder());
		buffer.flip();
		Header header = new Header();
		header.setCmd((byte)14);
		header.setBodyLength(44);
		request = RequestParser.parse(header, buffer);
		
		System.out.println(request);
	}
}
