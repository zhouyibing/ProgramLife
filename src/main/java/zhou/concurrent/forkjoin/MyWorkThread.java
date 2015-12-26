package zhou.concurrent.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

public class MyWorkThread extends ForkJoinWorkerThread{

	private static ThreadLocal<Integer> taskCounter = new ThreadLocal<Integer>();
	protected MyWorkThread(ForkJoinPool pool) {
		super(pool);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		System.out.printf("MyWorkerThread %d:Initializing task counter.\n",getId());
		taskCounter.set(0);
	}
	
	/**
	 * 当一个线程正常结束或抛出一个Exception异常时，调用的ForkJoinWorkerThread提供的onTermination()方法。
	 * 这个方法接收一个Throwable对象作为参数。如果这个参数值为null时，表明这个工作者线程正常结束。但是，如果这个参数的值不为null，表明这个线程抛出一个异常。你必须包含必要的代码来处理这种情况。
	 */
	@Override
	protected void onTermination(Throwable exception) {
		System.out.printf("MyWorkerThread %d:%d\n",getId(),taskCounter.get());
		super.onTermination(exception);
		//taskCounter.remove();//移除变量防止内存移除
		//taskCounter = null;//help GC
	}
	
	public void addTask() throws InterruptedException{
		if(isInterrupted())
			throw new InterruptedException("The thread has interrupted,could not add task!");
		int counter = taskCounter.get().intValue();
		counter++;
		taskCounter.set(counter);
	}
}
