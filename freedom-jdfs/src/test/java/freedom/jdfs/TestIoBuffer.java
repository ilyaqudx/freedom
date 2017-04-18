package freedom.jdfs;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import freedom.jdfs.common.buffer.IoBuffer;

public class TestIoBuffer {

	
	public static void main(String[] args) throws CharacterCodingException 
	{
		IoBuffer buffer = IoBuffer.allocate(1024);
		buffer.putString("jpg\0\0\0", Charset.forName("UTF-8").newEncoder());
		
		
		buffer.flip();
		String string = buffer.getString(6, Charset.forName("UTF-8").newDecoder());
		System.out.println(string.equals("jpg"));
	}
}
