package freedom.study.concurrent.future;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CountDownLatchFuture implements RequestFuture {

	private Boolean ready = false;
	private String  value;
	private AtomicInteger concurrentCount = new AtomicInteger(0);
	private CountDownLatch latch = new CountDownLatch(1);
	
	
	@Override
	public boolean isDone()
	{
		return ready;
	}

	@Override
	public void set(String v)
	{
		//TODO 验证是否是FUTURE设置为了final的原因,导致了FUTURE中的布尔类型变量被50000并发访问,而没有出现安全问题
		
		//是运气好,还是其他原因.测试了几十次都是正确的.
		
		//但是如果把打印语句放在设置READY值前面,就会出现并发问题.多次设置了值.说明应该不是FUTURE设置为FINAL的原因.
		//先打印语句只是先执行了比较耗时的操作.
		if(ready)
			return;
		synchronized (ready) {
			if(ready)return;
			this.ready = true;
		}
		this.value = v;
		concurrentCount.incrementAndGet();
		System.out.println(String.format("%s set value = %s,concurrentCount : %d",Thread.currentThread().getName(),value,concurrentCount.get()));
		latch.countDown();
	}

	@Override
	public String get()
	{
		return get(15);
	}

	@Override
	public String get(int timeout)
	{
		if(!ready)
		{
			try {
				latch.await(timeout, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return value;
	}
}
