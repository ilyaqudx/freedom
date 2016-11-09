package freedom.nio.filter;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import freedom.nio.IoSession;
import freedom.nio.NioSession;
import freedom.nio.WriteRequest;
import freedom.nio.codec.Codec;
import freedom.nio.filter.DefaultFilterChain.FilterEntry;
import freedom.nio.future.WriteFuture;

public class ProtocolCodecFilter implements Filter {

	private Codec codec;
	public ProtocolCodecFilter(Codec codec)
	{
		this.codec = codec;
	}
	@Override
	public void connected(FilterEntry nextFilter, IoSession session,
			Object msg) 
	{
		
	}
	@Override
	public void received(FilterEntry nextFilter, IoSession session,Object msg)
	{
		if(!(msg instanceof ByteBuffer))
		{
			nextFilter.fireRead(session, msg);
			return;
		}
		
		ByteBuffer buffer = (ByteBuffer) msg;
		if(session.hasFragment())
		{
			buffer = mergeBuffer(session, buffer);
		}
			
		int startPosition = 0;
		ProtocolDecoderOutput output = getDecoderOutput(session);
		while(codec.getDecoder().decode(session,buffer, output))
		{
			//startPosition == now position 说明根本一个字节数据都没有取,但是还要继续解码,会限入死loop
			if(!buffer.hasRemaining() || buffer.position() == startPosition)
				break;
		}
		//解码后的数据继续前行
		output.flush(nextFilter, session);
		
		if(buffer.hasRemaining())
		{
			//剩余数据进行存储
			session.storeFragment(buffer);
		}
	}
	
	/**
	 * 合并数据(将之前余下的数据和现在的数据进行合并)
	 * */
	private ByteBuffer mergeBuffer(IoSession session, ByteBuffer buffer)
	{
		ByteBuffer fragment = (ByteBuffer) session.getAttr(NioSession.FRAGMENT);
		int fragmentLen = fragment.limit();
		int bufferLen   = buffer.limit();
		byte[] array = new byte[fragmentLen + bufferLen];
		System.arraycopy(fragment.array(), 0, array, 0, fragmentLen);
		System.arraycopy(buffer.array(), 0, array, fragmentLen, bufferLen);
		buffer = ByteBuffer.wrap(array);
		session.clearFragment();
		return buffer;
	}
	@Override
	public void disconnected(FilterEntry nextFilter, IoSession session,
			Object msg) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void idle(FilterEntry nextFilter, IoSession session, Object msg)
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void write(FilterEntry nextFilter, IoSession session, WriteRequest request)
	{
		Object message = request.getMsg();
		if(message instanceof ByteBuffer)
			nextFilter.fireWrite(session, (WriteRequest) request);
		else
		{
			//?此处如何继续将WriteFuture传递下去?直接WriteRequest或单独写WriteFuture
			//答案是：过滤器接受的参数就是WriteRequest
			ProtocolEncoderOutput output = getEncoderOutput(session);
			//编码
			codec.getEncoder().encode(session, message,output);
			//将编码后的数据继续传递下去
			//output.flush(nextFilter, session);
			if(!output.getQueue().isEmpty())
			{
				ByteBuffer buffer = (ByteBuffer) output.getQueue().poll();
				if(buffer.hasRemaining())
				{
					WriteRequest encodedRequest = new WriteRequest(session, new WriteFuture(session),buffer );
					nextFilter.fireWrite(session, encodedRequest);
				}
			}
		}
	}
	
	public static final String DECODER_OUTPUT = "decoder_output";
	
	public static final String ENCODER_OUTPUT = "encoder_output";
	
	private ProtocolDecoderOutput getDecoderOutput(IoSession session)
	{
		return getAttrPutAbsent(session, DECODER_OUTPUT, ProtocolDecoderOutput.class);
	}
	
	private ProtocolEncoderOutput getEncoderOutput(IoSession session)
	{
		return getAttrPutAbsent(session, ENCODER_OUTPUT, ProtocolEncoderOutput.class);
	}
	
	@SuppressWarnings({ "unchecked" })
	public static final <T> T getAttrPutAbsent(IoSession session,String attr,Class<T> clazz)
	{
		T t = (T) session.getAttr(attr);
		if(null == t)
		{
			try 
			{
				t = clazz.newInstance();
				session.setAttr(attr, t);
			} 
			catch (InstantiationException e)
			{
				e.printStackTrace();
			} 
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		return t;
	}
	
	static abstract class AbstractProtocolCodecOutput
	{
		protected Queue<Object> queue;
		
		public AbstractProtocolCodecOutput(Queue<Object> queue)
		{
			this.queue = queue;
		}

		Queue<Object> getQueue()
		{
			return this.queue;
		}
		
		public void write(Object message)
		{
			this.queue.add(message);
		}
		
		public abstract void flush(FilterEntry nextFilter, IoSession session);
	}
	
	public static final class ProtocolDecoderOutput extends AbstractProtocolCodecOutput
	{
		public ProtocolDecoderOutput()
		{
			super(new LinkedList<Object>());
		}

		@Override
		public void flush(FilterEntry nextFilter, IoSession session) {
			while(!queue.isEmpty())
				nextFilter.fireRead(session, queue.poll());
			
		}
	}
	
	public static final class ProtocolEncoderOutput extends AbstractProtocolCodecOutput
	{
		public ProtocolEncoderOutput() 
		{
			super(new ArrayBlockingQueue<Object>(64));
		}

		@Override
		public void flush(FilterEntry nextFilter, IoSession session) {
			while(!queue.isEmpty())
				nextFilter.fireWrite(session, (WriteRequest)queue.poll());
			
		}
	}

	@Override
	public void sent(FilterEntry nextFilter, IoSession session, Object msg)
	{
		// TODO Auto-generated method stub
		nextFilter.fireSent(session, (WriteRequest)msg);
	}
	
}
