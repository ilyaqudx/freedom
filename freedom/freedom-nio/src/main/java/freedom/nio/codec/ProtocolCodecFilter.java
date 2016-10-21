package freedom.nio.codec;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import freedom.nio.DefaultFilterChain.FilterEntry;
import freedom.nio.Filter;
import freedom.nio.IoSession;
import freedom.nio.NioSession;
import freedom.nio.WriteRequest;
import io.netty.buffer.ByteBuf;

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
		ProtocolDecoderOutput output = new ProtocolDecoderOutput();
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
			nextFilter.fireWrite(session, message);
		else
		{
			//?此处如何继续将WriteFuture传递下去?直接WriteRequest或单独写WriteFuture
			//答案是：过滤器接受的参数就是WriteRequest
			ProtocolEncoderOutput output = new ProtocolEncoderOutput();
			//编码
			codec.getEncoder().encode(session, msg,output);
			//将编码后的数据继续传递下去
			output.flush(nextFilter, session);
		}
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
	
	static final class ProtocolDecoderOutput extends AbstractProtocolCodecOutput
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
	
	static final class ProtocolEncoderOutput extends AbstractProtocolCodecOutput
	{
		public ProtocolEncoderOutput() 
		{
			super(new ArrayBlockingQueue<Object>(64));
		}

		@Override
		public void flush(FilterEntry nextFilter, IoSession session) {
			while(!queue.isEmpty())
				nextFilter.fireWrite(session, queue.poll());
			
		}
	}
	
}
