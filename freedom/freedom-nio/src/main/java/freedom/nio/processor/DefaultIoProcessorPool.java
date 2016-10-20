package freedom.nio.processor;

import java.util.concurrent.Executors;

import freedom.nio.IoSession;

public class DefaultIoProcessorPool implements IoProcessorPool<IoProcessor> {

	public static final int PROCESSER_COUNT = Runtime.getRuntime().availableProcessors() + 1;
	
	public static final String PROCESSOR = "processor";
	
	private IoProcessor[] pool = new IoProcessor[PROCESSER_COUNT];
	
	public DefaultIoProcessorPool()
	{
		//初始化processor pool
		for (int i = 0; i < pool.length; i++) 
		{
			pool[i] = new NioProcessor(Executors.newCachedThreadPool());
		}
	}
	
	public IoProcessor getProcessor(IoSession session) throws Exception
	{
		//problem 7
		IoProcessor processor = (IoProcessor) session.getAttr(PROCESSOR);
		if(null == processor)
		{
			processor = pool[(int) (Math.abs(session.getId()) % PROCESSER_COUNT)];
			if(processor == null)
				throw new Exception("add processor failed");
			session.setAttr(PROCESSOR,processor);
		}
		return processor;
	}
}
