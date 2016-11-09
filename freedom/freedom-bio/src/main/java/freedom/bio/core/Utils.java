package freedom.bio.core;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class Utils {

	public static final PacketHead buildPackageHead(int dataLen)
	{
		PacketHead head = new PacketHead();
		head.setDataKindId((byte) 2);//加密类型必须为2.客户端会验证
		head.setCheckCode((byte) 1);
		head.setPacketSize((short)(dataLen + 8));
		head.setMainCmd((short) 1);
		head.setSubCmd((short) 100);
		return head;
	}
	
	public static final int getStringLen(Object entity,Class<?> clazz,Field field) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		Method method = clazz.getDeclaredMethod("getStringLen", String.class);
		Object value  = method.invoke(entity, field.getName());
		return value == null ? 0 : (Integer)value;
	}
	
	public static final <T> T parse(DataInputStream in , Class<T> clazz) throws InstantiationException, IllegalAccessException
	{
		T entity = clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields)
		{
			field.setAccessible(true);
			try 
			{
				Class<?> type = field.getType();
				if(type == byte.class)
					field.set(entity, in.readByte());
				else if(type == short.class)
					field.set(entity, readCppShort(in));
				else if(type == int.class)
					field.set(entity, readCppInt(in));
				else if(type == long.class)
					field.set(entity, readCppLong(in));
				else if(type == String.class)
				{
					field.set(entity, readCppString(in, getStringLen(entity, clazz, field)));
				}
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return entity;
	}
	
	public static final <T> byte[] getBytes(T entity)
	{
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		for (Field field : fields) 
		{
			field.setAccessible(true);
			try 
			{
				Class<?> type = field.getType();
				if(type == byte.class)
					dos.writeByte(field.getByte(entity));
				else if(type == short.class)
					dos.write(writeCppShort(field.getShort(entity)));
				else if(type == int.class)
					dos.write(writeCppInt(field.getInt(entity)));
				else if(type == long.class)
					dos.write(writeCppLong(field.getLong(entity)));
				else if(type == String.class)
				{
					String value = (String) field.get(entity);
					byte[] strBuffer = new byte[getStringLen(entity, clazz, field)];
					if(value != null && !"".equals(value))
					{
						byte[] valueStr = value.getBytes();
						if(valueStr.length < strBuffer.length)
						{
							System.arraycopy(valueStr, 0, strBuffer, 0, valueStr.length);
						}else
							System.arraycopy(valueStr, 0, strBuffer, 0, strBuffer.length);
					}
					dos.write(strBuffer);
				}
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return baos.toByteArray();
	}
	
	
	public static final byte[] getByteZero(int len)
	{
		byte[] buffer = new byte[len];
		for (int i = 0; i < len; i++) {
			buffer[i] = 0;
		}
		return buffer;
	}
	
	
	public static final byte readCppByte(InputStream in) throws IOException
	{
		return (byte) in.read();
	}
	public static final short readCppShort(InputStream in) throws IOException
	{
		int ch2 = in.read();
		int ch1 = in.read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (short)((ch1 << 8) + (ch2 << 0));
	}
	
	public static final int readCppInt(InputStream in) throws IOException
	{
		int ch4 = in.read();
        int ch3 = in.read();
        int ch2 = in.read();
        int ch1 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }
	
	public static final long readCppLong(InputStream in) throws IOException
	{
		byte[] readBuffer = new byte[8];
        in.read(readBuffer);
        return (((long)readBuffer[7] << 56) +
                ((long)(readBuffer[6] & 255) << 48) +
		((long)(readBuffer[5] & 255) << 40) +
                ((long)(readBuffer[4] & 255) << 32) +
                ((long)(readBuffer[3] & 255) << 24) +
                ((readBuffer[2] & 255) << 16) +
                ((readBuffer[1] & 255) <<  8) +
                ((readBuffer[0] & 255) <<  0));
    }
	
	public static final String readCppString(InputStream in,int len) throws IOException
	{
		byte[] data = new byte[len];
		in.read(data);
		int count = 0;
		for (byte b : data)
		{
			if(b == 0)
				break;
			count++;
		}
		return new String(data,0,count);
	}
	
	public static final byte writeCppByte(byte v)
	{
		return v;
	}
	public static final byte[] writeCppShort(short v)
	{
	     byte v1 = (byte) ((v >>> 0) & 0xFF);
	     byte v2 = (byte) ((v >>> 8) & 0xFF);
	     return new byte[]{v1,v2};
	}
	public static final byte[] writeCppInt(int v)
	{
		byte v1 = (byte) ((v >>> 0) & 0xFF);
		byte v2 = (byte) ((v >>> 8) & 0xFF);
		byte v3 = (byte) ((v >>> 16) & 0xFF);
		byte v4 = (byte) ((v >>> 24) & 0xFF);
		return new byte[]{v1,v2,v3,v4};
	}
	public static final byte[] writeCppLong(long v)
	{
		byte v1 = (byte) ((v >>> 0) & 0xFF);
		byte v2 = (byte) ((v >>> 8) & 0xFF);
		byte v3 = (byte) ((v >>> 16) & 0xFF);
		byte v4 = (byte) ((v >>> 24) & 0xFF);
		byte v5 = (byte) ((v >>> 32) & 0xFF);
		byte v6 = (byte) ((v >>> 40) & 0xFF);
		byte v7 = (byte) ((v >>> 48) & 0xFF);
		byte v8 = (byte) ((v >>> 56) & 0xFF);
		return new byte[]{v1,v2,v3,v4,v5,v6,v7,v8};
	}
}
