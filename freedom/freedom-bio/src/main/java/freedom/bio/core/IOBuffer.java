package freedom.bio.core;

import java.io.EOFException;
import java.io.IOException;

public class IOBuffer {

	private int pos;
	private byte[] buffer;
	
	public int pos()
	{
		return pos;
	}
	
	public boolean hasRemaining()
	{
		return buffer.length > pos;
	}
	
	public int remaining()
	{
		return buffer.length - pos;
	}
	
	public void reset()
	{
		this.pos = 0;
	}
	
	public static final IOBuffer alloc(byte[] data)
	{
		IOBuffer buffer = new IOBuffer();
		buffer.buffer = data;
		return buffer;
	}
	
	public final byte readCppByte() throws IOException
	{
		return buffer[pos++];
	}
	public  final short readCppShort() throws IOException
	{
		int ch2 = buffer[pos++];
		int ch1 = buffer[pos++];
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (short)((ch1 << 8) + (ch2 << 0));
	}
	
	public  final int readCppInt() throws IOException
	{
		int ch4 = buffer[pos++];
        int ch3 = buffer[pos++];
        int ch2 = buffer[pos++];
        int ch1 = buffer[pos++];
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }
	
	public  final long readCppLong() throws IOException
	{
		byte[] readBuffer = new byte[8];
        System.arraycopy(buffer, pos, readBuffer, 0, 8);
        pos += 8;
        return (((long)readBuffer[7] << 56) +
                ((long)(readBuffer[6] & 255) << 48) +
		((long)(readBuffer[5] & 255) << 40) +
                ((long)(readBuffer[4] & 255) << 32) +
                ((long)(readBuffer[3] & 255) << 24) +
                ((readBuffer[2] & 255) << 16) +
                ((readBuffer[1] & 255) <<  8) +
                ((readBuffer[0] & 255) <<  0));
    }
	
	public final byte[] readCppBytes(int len)
	{
		byte[] data = new byte[len];
		System.arraycopy(buffer, pos, data, 0, len);
		pos += len;
		return data;
	}
	
	public  final String readCppString(int len) throws IOException
	{
		byte[] data = readCppBytes(len);
		int count = 0;
		for (byte b : data)
		{
			if(b == 0)
				break;
			count++;
		}
		return new String(data,0,count);
	}
}
