package freedom.bio.core;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;

public class ByteArrayUtils {

	
	public static final <T> T parse(IOBuffer buffer,Class<T> clazz) throws InstantiationException, IllegalAccessException
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
					field.set(entity, buffer.readCppByte());
				else if(type == short.class)
					field.set(entity, buffer.readCppShort());
				else if(type == int.class)
					field.set(entity, buffer.readCppInt());
				else if(type == long.class)
					field.set(entity, buffer.readCppLong());
				else if(type == String.class)
				{
					field.set(entity, buffer.readCppString(Utils.getStringLen(entity, clazz, field)));
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
					dos.write(Utils.writeCppShort(field.getShort(entity)));
				else if(type == int.class)
					dos.write(Utils.writeCppInt(field.getInt(entity)));
				else if(type == long.class)
					dos.write(Utils.writeCppLong(field.getLong(entity)));
				else if(type == String.class)
				{
					String value = (String) field.get(entity);
					byte[] strBuffer = new byte[Utils.getStringLen(entity, clazz, field)];
					if(value != null && !"".equals(value))
					{
						byte[] valueStr = value.getBytes();
						if(valueStr.length < strBuffer.length)
							System.arraycopy(valueStr, 0, strBuffer, 0, valueStr.length);
						else
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
}
