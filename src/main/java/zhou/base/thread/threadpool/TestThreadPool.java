package zhou.base.thread.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestThreadPool {
     //ExecutorService.invokeAny()就是为此设计的，
	//他接收的参数是一个List，List中的每一个元素必须实现Callable接口。他的功能是依此启动新的线程执行List中的任务，并将第一个得到的结果作为返回值，然后立刻终结所有的线程。
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		//testFixedThreadPool();
		//testCachedThreadPool();
		testCompletionThreadPool();
		//testScheduledExecutorService();
	}
	
	public static void testFixedThreadPool() throws InterruptedException, ExecutionException{
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		List<Future<Long>> futures = new ArrayList<Future<Long>>();
		//如果有一个线程执行耗时长，会影响后面的future获取
		futures.add(executorService.submit(new CallThreadExecuteLongTime()));
		for(int i =0;i<14;i++){
			futures.add(executorService.submit(new CallThread()));
		}
		for(Future<Long> future:futures){
			System.out.println(future.get());
		}
		executorService.shutdownNow();
	}
	
	public static void testCachedThreadPool() throws InterruptedException{
		ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		List<Future<Long>> futures = new ArrayList<Future<Long>>();
		//如果有一个线程执行耗时长，会影响后面的future获取
		for(int i =0;i<20;i++){
			futures.add(executorService.submit(new CallThread()));
		}
		System.out.println("actived thread count before wait 60s:"+executorService.getPoolSize());
		Thread.sleep(65000);
		//等待超过60秒后还有多少线程
		System.out.println("actived thread count:"+executorService.getActiveCount());
		executorService.shutdownNow();
	}
	
	/**
	 * 当“完成服务”任务结束，这些任务中一个任务就执行结束了，“完成服务”中存储着Future对象，用来控制它在队列中的执行。
	 *调用poll()方法访问这个队列，查看是否有任务已经完成，如果有，则返回队列中的第一个元素（即一个任务执行完成后的Future对象）。
	 *当poll()方法返回Future对象后，它将从队列中删除这个Future对象。
	 *在这个示例中，我们在调用poll()方法时传递了两个参数，表示当队列里完成任务的结果为空时，想要等待任务执行结束的时间。
	 * poll()：无参数的poll()方法用于检查队列中是否有Future对象。如果队列为空，则立即返回null。否则，它将返回队列中的第一个元素，并移除这个元素。
     * take()：这个方法也没有参数，它检查队列中是否有Future对象。如果队列为空，它将阻塞线程直到队列中有可用的元素。如果队列中有元素，它将返回队列中的第一个元素，并移除这个元素。
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static void testCompletionThreadPool() throws InterruptedException, ExecutionException{
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		CompletionService<Long> completionService = new ExecutorCompletionService<Long>(executorService);
		completionService.submit(new CallThreadExecuteLongTime());
		for(int i =0;i<14;i++){
			completionService.submit(new CallThread());
		}
		while(true){
			Future<Long> future = completionService.take();
			System.out.println(future);
			if(null==future)
				break;
			System.out.println(future.get());
		}
		executorService.shutdownNow();
	}
	
	public static void testScheduledExecutorService(){
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
		//executorService.scheduleAtFixedRate(new RunThread(), 5, 5, TimeUnit.SECONDS);
		executorService.scheduleWithFixedDelay(new RunThread(), 5, 5, TimeUnit.SECONDS);
		//executorService.shutdownNow();
	}
	
	static class RunThread implements Runnable{

		@Override
		public void run() {
			System.out.println(Thread.currentThread()+" execute this...");
		}

	}
    
	static class CallThread implements Callable<Long>{

		@Override
		public Long call() throws Exception {
			long begin = System.currentTimeMillis();
			System.out.println(Thread.currentThread()+" execute this...");
			return System.currentTimeMillis()-begin;
		}
	}
	
	static class CallThreadExecuteLongTime implements Callable<Long>{

		@Override
		public Long call() throws Exception {
			long begin = System.currentTimeMillis();
			System.out.println(Thread.currentThread()+" execute this with long time...");
			Thread.sleep(30000);
			return System.currentTimeMillis()-begin;
		}
		
	}
}
