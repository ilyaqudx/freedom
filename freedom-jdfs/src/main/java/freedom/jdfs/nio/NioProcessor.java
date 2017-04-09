package freedom.jdfs.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

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
	/**
	 * 完成操作的任务队列
	 * */
	private ConcurrentLinkedQueue<StorageTask>
		completeTaskQueue = new ConcurrentLinkedQueue<StorageTask>();
	
	public void complete(StorageTask storageTask)
	{
		if(null != storageTask)
			completeTaskQueue.add(storageTask);
	}
	
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
		 * 
		 * 数据读完了,也删除了当前的读事件.为什么还有读事件不停的来.查看一下是否读取的数据为0,读完后,
		 * 自己的BYTEBUFFER没有可读的空间 pos = limit
		 * **/
		private void client_sock_read(NioSession session,StorageTask storageTask)
		{
			StorageClientInfo clientInfo = storageTask.clientInfo;
			if (clientInfo.canceled)
			{
				return;
			}
			
			//TODO Notice
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
						//这儿做个测试,当RECV_BYTES为0时,还有读事件过来,判断 一下是否真的还有数据
						if(recv_bytes == 0){
							/*ByteBuffer test  = ByteBuffer.allocate(10);
							int testLen = session.getChannel().read(test);
							System.out.println(test);*/
						}
						//only read recv_bytes
						ByteBuffer buffer = storageTask.buffer;
						//System.out.println("buffer before recv : " + buffer);
						buffer.limit(storageTask.buffer.position() + recv_bytes);
						
						int len = session.getChannel().read(storageTask.buffer);
						if(len == 0){
							System.out.println("读事件中读取到了数据为0的情况,缓冲区确实没有数据了.跳出循环,下一轮再来读取" + buffer);
							//这儿读到0的情况是因为一起在死循环中,如果没有读取到完整的BUFFER的大小的数据则会一直读,但是这时候可能缓冲区确实没有数据,所以返回了0
							//的情况.此时应该跳出循环,让后面的CHANNEL继续执行.而不是一直让一个CHANNEL读完再处理.浪费CPU.而且如果一个CHANNEL因为长时间不来
							//数据,则会造成其他所有的处理都不能处理.这是代码写的有问题
							break;
						}
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
						storageTask.buffer.position(storageTask.offset);
						System.out.println(String.format("【Channel %d】本次实际接收数据的长度 : %d ,本次期望接收数据长度 : %d ,累计接收数据长度 : %d,"
								+ "还剩余数据 : %d", session.id,len,recv_bytes,buffer.position(),storageTask.length - storageTask.offset));
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
						try {
							session.getChannel().close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						break;
					}
					catch (Exception e) 
					{
						e.printStackTrace();
						try {
							session.getChannel().close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						break;
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
