package freedom.jdfs.nio;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import freedom.jdfs.codec.Codec;

public class NioProcessor {

	public final int id;
	public final String name;
	private Selector sel;
	private LinkedList<NioSession> newSessions = new LinkedList<NioSession>();

	public NioProcessor(int id)
	{
		this.id = id;
		this.name = "NioProcessor-" + id;
		try 
		{
			this.sel = Selector.open();
			startup();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	private void startup()
	{
		new Thread(new Processor()).start();
	}

	public void addNewSession(NioSession session) 
	{
		session.registerRead(sel);
		newSessions.add(session);
	}

	final class Processor implements Runnable
	{
		@Override
		public void run()
		{
			while (true)
			{
				try 
				{
					sel.select(1000);
					process(sel.selectedKeys());
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}

		private void process(Set<SelectionKey> selectedKeys)
		{
			for (Iterator<SelectionKey> it = selectedKeys.iterator();it.hasNext();) 
			{
				SelectionKey key = it.next();
				if (key.isReadable())
					read(key);
				else if(key.isWritable())
					write(key);
				it.remove();
			}
		}

		private void write(SelectionKey key) 
		{
			
		}

		private void read(SelectionKey key)
		{
			NioSession session = ((NioSession) key.attachment());
			SocketChannel channel = session.getChannel();
			try 
			{
				ByteBuffer newBuffer = ByteBuffer.allocate(channel.getOption(StandardSocketOptions.SO_RCVBUF));
				int len = channel.read(newBuffer);
				if (len > 0) 
				{
					ByteBuffer data = newBuffer;
					if(session.hasFragment())
					{
						data = mergeBuffer(session.pollFragment(),newBuffer);
					}
					int start = data.position();
					while(data.hasRemaining() && Codec.decode(session, data))
					{
						if(start == data.position())
							break;
						start = data.position();
					}
					
					if(data.hasRemaining())
					{
						session.storeFragment(data);
					}
				}

			}
			catch (IOException e) 
			{
				e.printStackTrace();
				try {
					channel.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} 
		}

		private ByteBuffer mergeBuffer(ByteBuffer fragment, ByteBuffer newBuffer)
		{
			int capacity = fragment.limit() + newBuffer.limit();
			ByteBuffer mergeBuffer = ByteBuffer.allocate(capacity);
			mergeBuffer.put(fragment);
			mergeBuffer.put(newBuffer);
			return mergeBuffer;
		}

	}
}
