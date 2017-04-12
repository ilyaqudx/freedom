package freedom.jdfs.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import freedom.jdfs.LogKit;
import freedom.jdfs.handler.CommandRouter;
import freedom.jdfs.protocol.ProtoCommon;
import freedom.jdfs.storage.StorageClientInfo;
import freedom.jdfs.storage.StorageServer;
import freedom.jdfs.storage.StorageTask;

public class NioProcessor {

	Logger LOGGER = Logger.getLogger(NioProcessor.class); 
	public final int id;
	public final String name;
	private Selector sel;
	private ConcurrentLinkedQueue<NioSession> newSessions = new ConcurrentLinkedQueue<NioSession>();
	/**
	 * 完成操作的任务队列
	 * */
	private ConcurrentLinkedQueue<StorageTask>
		completeTaskQueue = new ConcurrentLinkedQueue<StorageTask>();
	
	public void complete(StorageTask storageTask)
	{
		if(null != storageTask){
			completeTaskQueue.add(storageTask);
			//立即唤醒NIO线程进行处理,否则必须要等到超时才会去处理
			sel.wakeup();//是否可用重新注册读事件是唤醒?会马上响应吗?
		}
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
			//我这犯低级错误.要从列表中删除才行.每次设置为STAGE为8还是要去读,并且TASK中的BUFFER长度为负数等错误
			NioSession session = null;
			while((session = newSessions.poll()) != null){
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
					//handle dio done back
					handleDioCallback();
					//process I/O event
					process(sel.selectedKeys());
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}

		private void handleDioCallback() 
		{
			if(!completeTaskQueue.isEmpty()){
				StorageTask storageTask = null;
				while((storageTask = completeTaskQueue.poll()) != null){
					storageTask.offset = 0;
					storageTask.length = 0;
					storageTask.data.clear();
					storageTask.clientInfo.fileContext.buffOffset = 0;
					storageTask.clientInfo.stage = StorageTask.FDFS_STORAGE_STAGE_NIO_RECV;
					storageTask.session.setIntestedRead(true);
					//1292line why 【Channel : 9 重新注册读事件】task.offset : 262144,task.length = 262144 ,buffer : java.nio.HeapByteBuffer[pos=262144 lim=262144 cap=262144]
					//LogKit.info("【Channel " +storageTask.session.id+ " 重新注册读事件】task.offset : " +storageTask.offset+ ",task.length = " +storageTask.length+ " ,buffer : " +storageTask.buffer,NioProcessor.class);
				}
			}
		}

		private void process(Set<SelectionKey> selectedKeys)
		{
			for (Iterator<SelectionKey> it = selectedKeys.iterator();it.hasNext();) 
			{
				SelectionKey key = it.next();
				it.remove();
				if (key.isReadable())
				{
					storage_recv_notify_read(key);
				}
				else if(key.isWritable()){
					write(key);
				}
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
			/*if ((clientInfo.stage & StorageTask.FDFS_STORAGE_STAGE_DIO_THREAD) > 0)
			{
				clientInfo.stage &= ~StorageTask.FDFS_STORAGE_STAGE_DIO_THREAD;
			}*/
			switch(clientInfo.stage)
			{
				case StorageTask.FDFS_STORAGE_STAGE_NIO_INIT:
					//init(session.task);
					break;
				case StorageTask.FDFS_STORAGE_STAGE_NIO_RECV:
					//TODO Notice storageTask.offset = 0;  之后 再看为什么要重设为0
					//为啥已经将STAGE设置为8还是可以进来
					int remain_bytes = (int)(clientInfo.totalLength - clientInfo.totalOffset);
					storageTask.length = remain_bytes > storageTask.size ? storageTask.size : remain_bytes;
					if(set_recv_event(storageTask)){
						client_sock_read(key,session,storageTask);
					}
					break;
				default:
					//LogKit.info(String.format("[Channel %d state is %d,has no function deal will to break]", session.id,clientInfo.stage), NioProcessor.class);
					break;
			}
		}
		
		/**
		 * read client data
		 * 
		 * 数据读完了,也删除了当前的读事件.为什么还有读事件不停的来.查看一下是否读取的数据为0,读完后,
		 * 自己的BYTEBUFFER没有可读的空间 pos = limit
		 * **/
		private void client_sock_read(SelectionKey key,NioSession session,StorageTask storageTask)
		{
			StorageClientInfo clientInfo = storageTask.clientInfo;
			if (clientInfo.canceled)
			{
				return;
			}
			
			if(clientInfo.stage != StorageTask.FDFS_STORAGE_STAGE_NIO_RECV)
				return;
			
			//TODO Notice
			/*fast_timer_modify(&pTask->thread_data->timer,
					&pTask->event.timer, g_current_time +
					g_fdfs_network_timeout);*/
			int recv_bytes = 0;
			while(true){
					//need recv bytes from socket
				//【Channel 9】buffer before recv : java.nio.HeapByteBuffer[pos=262144 lim=262144 cap=262144],recv_bytes : -195159
					recv_bytes = clientInfo.totalLength == 0 ? ProtoCommon.HEADER_LENGTH : storageTask.length - 
							storageTask.offset;
					try
					{
						if(recv_bytes < 0){
							System.out.println("我在这儿打个断点 : recv_bytes < 0");
						}
						
						//这儿做个测试,当RECV_BYTES为0时,还有读事件过来,判断 一下是否真的还有数据
						if(recv_bytes == 0){
							/*ByteBuffer test  = ByteBuffer.allocate(10);
							int testLen = session.getChannel().read(test);
							System.out.println(test);*/
							//判断一下,处理一下,直接进行磁盘处理
						}
						//only read recv_bytes
						ByteBuffer buffer = storageTask.data;
						//LogKit.info("【Channel " +session.id+ "】buffer before recv : " + buffer + ",recv_bytes : " + recv_bytes,NioProcessor.class);
						buffer.limit(storageTask.data.position() + recv_bytes);
						
						int len = session.getChannel().read(storageTask.data);
						if(len == 0){
							//LogKit.info("【Channel " +session.id+ "】读事件中读取到了数据为0的情况,缓冲区确实没有数据了.跳出循环,下一轮再来读取" + buffer,NioProcessor.class);
							//这儿读到0的情况是因为一直在死循环中,如果没有读取到完整的BUFFER的大小的数据则会一直读,但是这时候可能缓冲区确实没有数据,所以返回了0
							//的情况.此时应该跳出循环,让后面的CHANNEL继续执行.而不是一直让一个CHANNEL读完再处理.浪费CPU.而且如果一个CHANNEL因为长时间不来
							//数据,则会造成其他所有的处理都不能处理.这是代码写的有问题.fastdfs本身是这样处理的
							break;
						}
						if(clientInfo.totalLength == 0){
							//no enough header
							if(len < recv_bytes){
								storageTask.offset += len;
								return;
							}
							buffer.position(0);
							clientInfo.totalLength = buffer.getLong();
							if(clientInfo.totalLength <= 0){
								//log error TODO
								LogKit.error(String.format("【Channel %d total_length <= 0 , buffer : %s】", session.id,storageTask.data), this.getClass());
								return;
							}
							clientInfo.totalLength += ProtoCommon.HEADER_LENGTH;
							if(storageTask.size == 0){
								System.out.println("print task size is 0");
							}
							storageTask.length = (int)(clientInfo.totalLength > storageTask.size ? 
									storageTask.size : clientInfo.totalLength);
						}
						
						storageTask.offset += len;
						storageTask.data.position(storageTask.offset);
						//LogKit.info(String.format("【Channel %d】[task.offset : %d,task.length : %d]本次实际接收数据的长度 : %d ,本次期望接收数据长度 : %d ,累计接收数据长度 : %d,"
							//	+ "还剩余数据 : %d", session.id,storageTask.offset,storageTask.length,len,recv_bytes,storageTask.offset,storageTask.length - storageTask.offset),NioProcessor.class);
						if(storageTask.offset >= storageTask.length){//这个条件为什么有时候没有满足?是线程的问题还
							if(storageTask.length == 0){
								LogKit.error(String.format("【channel %d】【task length is 0.why?】",session.id), this.getClass());
							}
							session.setIntestedRead(false);//会不会在设置取消读事件的之前,就已经有数据到来并且放到在selections里面了,设置之后 到来的,不会响应.
							//LogKit.info(String.format("【Channel %d】[task.offset : %d,task.length : %d,buffer : %s]已经组成一个完整的TASK,可以进行磁盘操作同时将channel的opt的读取消 : opts : %d", session.id,storageTask.offset,storageTask.length,storageTask.buffer,session.getIntested()),NioProcessor.class);
							if (clientInfo.totalOffset + storageTask.length >= 
									clientInfo.totalLength)
							{
								/* current req recv done */
								//clientInfo.stage = StorageTask.FDFS_STORAGE_STAGE_NIO_SEND;
								storageTask.reqCount++;
							}

							if (clientInfo.totalOffset == 0)
							{
								clientInfo.totalOffset = storageTask.length;
								storage_deal_task(session,storageTask);
							}
							else
							{
								clientInfo.totalOffset += storageTask.length;

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

		public void storage_dio_queue_push(NioSession session,
				StorageTask storageTask) 
		{
			storageTask.clientInfo.stage = StorageTask.FDFS_STORAGE_STAGE_DIO_THREAD;
			StorageServer.context.storageDioService.addWriteTask(storageTask);
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
