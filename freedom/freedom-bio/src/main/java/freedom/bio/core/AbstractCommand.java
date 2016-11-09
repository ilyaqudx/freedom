package freedom.bio.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public abstract class AbstractCommand<I, O> implements Command<I, O> {

	public abstract O run(I in,O out)throws Exception;
	
	public abstract short getMainCmd();
	
	public abstract short getSubCmd();

	@SuppressWarnings("unchecked")
	@Override
	public PacketRes execute(IoSession session,byte[] data) throws Exception 
	{
		ParameterizedType type =  (ParameterizedType) this.getClass().getGenericSuperclass();
		Type[] types = type.getActualTypeArguments();
		I in = ByteArrayUtils.parse(IOBuffer.alloc(data), (Class<I>)types[0]);
		O out = ((Class<O>)types[1]).newInstance();
		out = run(in, out);
		PacketHead head = new PacketHead();
		head.setMainCmd(getMainCmd());
		head.setSubCmd(getSubCmd());
		return new PacketRes(head,out);
	}
}
