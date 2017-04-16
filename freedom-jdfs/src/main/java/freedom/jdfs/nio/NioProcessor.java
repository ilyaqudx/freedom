package freedom.jdfs.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import freedom.jdfs.LogKit;
import freedom.jdfs.codec.ProtocolCodec;
import freedom.jdfs.exception.ProtocolParseException;
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
		this.id   = id;
		this.name = "NioProcessor-" + id;
		try 
		{
			this.sel  = Selector.open();
			//启动NIO处理器
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
		if(!newSessions.isEmpty())
		{
			//犯低级错误.要从列表中删除才行.每次设置为STAGE为8还是要去读,并且TASK中的BUFFER长度为负数等错误
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
			if(!completeTaskQueue.isEmpty())
			{
				StorageTask storageTask = null;
				while((storageTask = completeTaskQueue.poll()) != null)
				{
					if(storageTask.clientInfo.stage == StorageTask.FDFS_STORAGE_STAGE_NIO_RECV){
						storageTask.session.setIntestedRead(true);
					}
					else if(storageTask.clientInfo.stage == StorageTask.FDFS_STORAGE_STAGE_NIO_SEND)
					{	
						storageTask.session.setIntestedWrite(true);
					}
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
			NioSession session = ((NioSession) key.attachment());
			session.setIntestedWrite(false);
			storage_send_add_event(session.task);
		}

		private void storage_recv_notify_read(SelectionKey key)
		{
			NioSession session = ((NioSession) key.attachment());
			StorageTask storageTask      = session.task;
			StorageClientInfo clientInfo = session.task.clientInfo;
			storageTask.length = (int) Math.min(storageTask.size, clientInfo.remaining());
			client_sock_read(session, storageTask);
		}
		
		private int storage_send_add_event(StorageTask storageTask)
		{
			//storageTask.offset = 0;//TODO Notice 不能在这儿设置为0,暂时为了测试下载注释掉,上传这儿取消了肯定有问题.

			/* direct send   event is ?*/ 
			client_sock_write((short)1,storageTask);

			return ProtoCommon.SUCCESS;
		}

		private void client_sock_write(short event,StorageTask storageTask)
		{
			int bytes = 0;
		    StorageClientInfo clientInfo = storageTask.clientInfo;
			if (clientInfo.canceled)
			{
				return;
			}

			if ((event & ProtoCommon.IOEVENT_TIMEOUT) > 0)
			{
				LogKit.error("send timeout",NioProcessor.class);
				//TODO task_finish_clean_up(storageTask);
				return;
			}

			/*if ((event & ProtoCommon.IOEVENT_ERROR) > 0)
			{
				LogKit.error(String.format("client ip : %s,recv error event %d,close connection",storageTask.clientIp,event),NioProcessor.class);
				//TODO task_finish_clean_up(storageTask);
				return;
			}*/

			while (true)
			{
				/*fast_timer_modify(&pTask.thread_data.timer,
					&pTask.event.timer, g_current_time +
					g_fdfs_network_timeout);*/
				try {
					bytes = storageTask.session.getChannel().write(storageTask.data);
				} catch (IOException e) {
					e.printStackTrace();
					LogKit.error(String.format("client ip : %s,write failed,error info", storageTask.clientIp,e.getMessage()), this.getClass());
					//TODO task_finish_clean_up(pTask);
				}
				
				if (bytes < 0)
				{
					/*if (errno == EAGAIN || errno == EWOULDBLOCK)
					{
						set_send_event(pTask);
					}
					else if (errno == EINTR)
					{
						continue;
					}
					else
					{
						logError("file: "__FILE__", line: %d, " \
							"client ip: %s, recv failed, " \
							"errno: %d, error info: %s", \
							__LINE__, pTask.client_ip, \
							errno, STRERROR(errno));

						task_finish_clean_up(pTask);
					}

					return;*/
				}
				else if (bytes == 0)
				{
					LogKit.error("send failed, set write event.", NioProcessor.class);
					//TODO task_finish_clean_up(pTask);
					//这儿不能直接断开连接 .应该重新注册WRITE事件
					storageTask.session.setIntestedWrite(true);
					return;
				}

				storageTask.offset += bytes;
				if (storageTask.offset >= storageTask.length)
				{
					/*
					 * TODO 
					 * if (set_recv_event(storageTask) != 0)
					{
						return;
					}*/

					clientInfo.totalOffset += storageTask.length;
					if (clientInfo.totalOffset>=clientInfo.totalLength)
					{
						/*
						 * TODO 
						 * if (clientInfo.totalLength == ProtoCommon.HEADER_LENGTH
							&& ((TrackerHeader *)storageTask.data).status == EINVAL)
						{
							logDebug("file: "__FILE__", line: %d, "\
								"close conn: #%d, client ip: %s", \
								__LINE__, storageTask.event.fd,
								storageTask.client_ip);
							task_finish_clean_up(storageTask);
							return;
						}*/

						/*  reponse done, try to recv again */
						clientInfo.totalLength = 0;
						clientInfo.totalOffset = 0;
						storageTask.offset = 0;
						storageTask.length = 0;
						storageTask.data.clear();
						storageTask.session.setIntestedRead(true);
						clientInfo.stage = StorageTask.FDFS_STORAGE_STAGE_NIO_RECV;//目的应该是要接收客户端的断开信号
						
					}
					else  //continue to send file content
					{
						storageTask.clientInfo.fileContext.buffOffset = 0;
						storageTask.offset = 0;
						storageTask.length = (int) Math.min(storageTask.size,clientInfo.fileContext.end - clientInfo.fileContext.offset);
						/* continue read from file */
						StorageServer.context.storageDioService.storage_dio_queue_push(storageTask);
					}

					return;
				}
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
			/*fast_timer_modify(&pTask.thread_data.timer,
					&pTask.event.timer, g_current_time +
					g_fdfs_network_timeout);*/
			int recv_bytes = 0;
			boolean continueFlag = true;
			while(continueFlag){
					//need recv bytes from socket
					recv_bytes = clientInfo.totalLength == 0 ? ProtoCommon.HEADER_LENGTH : 
						storageTask.remaining();
					try
					{
						//only read recv_bytes
						ByteBuffer buffer = storageTask.data;
						buffer.limit(storageTask.data.position() + recv_bytes);
						
						int len = session.getChannel().read(storageTask.data);
						if(len == -1){
							throw new ClosedChannelException();
						}
						if(len == 0){
							break;
						}
						if(len == recv_bytes)
							continueFlag = ProtocolCodec.decode(session, storageTask, clientInfo);
					}
					catch (IOException e) 
					{
						try 
						{
							session.getChannel().close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						break;
					}
					catch(ProtocolParseException e)
					{
						LogKit.error(e.getMessage(),e.getCause(),NioProcessor.class);
					}
					catch (Exception e) 
					{
						try
						{
							session.getChannel().close();
						}
						catch (IOException e1)
						{
							e1.printStackTrace();
						}
						break;
					}
			}
		}

		public void storage_dio_queue_push(NioSession session,StorageTask storageTask) 
		{
			storageTask.clientInfo.stage = StorageTask.FDFS_STORAGE_STAGE_DIO_THREAD;
			StorageServer.context.storageDioService.addWriteTask(storageTask);
		}
	}
}
