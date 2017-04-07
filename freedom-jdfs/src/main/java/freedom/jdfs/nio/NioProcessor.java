package freedom.jdfs.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.apache.log4j.Logger;

import freedom.jdfs.handler.CommandRouter;
import freedom.jdfs.protocol.ProtoCommon;
import freedom.jdfs.storage.StorageClientInfo;
import freedom.jdfs.storage.StorageTask;

public class NioProcessor {

	Logger LOGGER = Logger.getLogger(NioProcessor.class); 
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
		newSessions.add(session);
	}
	
	private void handleNewSession()
	{
		if(!newSessions.isEmpty()){
			for (NioSession session : newSessions) {
				session.registerRead(sel);
			}
		}
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
					//handle acceptor new session
					handleNewSession();
					//process I/O event
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
				{
					storage_recv_notify_read(key);
					//此处读事件,不能按照常规直接就删除
				}
				else if(key.isWritable())
					write(key);
				it.remove();
			}
		}

		private void write(SelectionKey key) 
		{
			
		}

		private void storage_recv_notify_read(SelectionKey key)
		{
			NioSession session = ((NioSession) key.attachment());
			StorageTask storageTask      = session.task;
			StorageClientInfo clientInfo = session.task.clientInfo;
			if ((storageTask.stage & StorageTask.FDFS_STORAGE_STAGE_DIO_THREAD) > 0)
			{
				storageTask.stage &= ~StorageTask.FDFS_STORAGE_STAGE_DIO_THREAD;
			}
			switch(storageTask.stage)
			{
				case StorageTask.FDFS_STORAGE_STAGE_NIO_INIT:
					//init(session.task);
					break;
				case StorageTask.FDFS_STORAGE_STAGE_NIO_RECV:
					storageTask.offset = 0;
					int remain_bytes = (int)(clientInfo.total_length - clientInfo.total_offset);
					storageTask.length = remain_bytes > storageTask.size ? storageTask.size : remain_bytes;
					if(set_recv_event(storageTask)){
						client_sock_read(session,storageTask);
					}
					break;
			}
		}
		
		/**
		 * read client data
		 * **/
		private void client_sock_read(NioSession session,StorageTask storageTask)
		{
			StorageClientInfo clientInfo = storageTask.clientInfo;
			if (clientInfo.canceled)
			{
				return;
			}
			
			//TODO
			/*fast_timer_modify(&pTask->thread_data->timer,
					&pTask->event.timer, g_current_time +
					g_fdfs_network_timeout);*/
			int recv_bytes = 0;
			while(true){
					//need recv bytes from socket
					recv_bytes = clientInfo.total_length == 0 ? ProtoCommon.HEADER_LENGTH : storageTask.length - 
							storageTask.offset;
					try
					{
						//only read recv_bytes
						ByteBuffer buffer = storageTask.buffer;
						buffer.limit(buffer.position() + recv_bytes);
						int len = session.getChannel().read(storageTask.buffer);
						if(clientInfo.total_length == 0){
							//no enough header
							if(len < recv_bytes){
								storageTask.offset += len;
								return;
							}
							buffer.position(0);
							clientInfo.total_length = buffer.getLong();
							if(clientInfo.total_length < 0){
								//log error TODO
								return;
							}
							clientInfo.total_length += ProtoCommon.HEADER_LENGTH;
							storageTask.length = (int)(clientInfo.total_length > storageTask.size ? 
									storageTask.size : clientInfo.total_length);
						}
						
						storageTask.offset += len;
						if(storageTask.offset >= storageTask.length){//read done this turn
							if (clientInfo.total_offset + storageTask.length >= 
									clientInfo.total_length)
							{
								/* current req recv done */
								clientInfo.stage = StorageTask.FDFS_STORAGE_STAGE_NIO_SEND;
								storageTask.req_count++;
							}

							if (clientInfo.total_offset == 0)
							{
								clientInfo.total_offset = storageTask.length;
								storage_deal_task(session,storageTask);
							}
							else
							{
								clientInfo.total_offset += storageTask.length;

								/* continue write to file */
								storage_dio_queue_push(session,storageTask);
							}

							return;
						}
						
					}
					catch (IOException e) 
					{
						e.printStackTrace();
					}
			}
		}

		private void storage_dio_queue_push(NioSession session,
				StorageTask storageTask) 
		{
			
		}

		private void storage_deal_task(NioSession session,StorageTask storageTask) 
		{
			CommandRouter.route(session, storageTask);
		}

		private void storage_upload_file(NioSession session,
				StorageTask storageTask, boolean append) 
		{
			
		}

		//TODO
		private boolean set_recv_event(StorageTask storageTask)
		{
			return true;
		}
	}
}
