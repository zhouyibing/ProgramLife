package zhou.base.thread.threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 *当执行器接收一个任务并开始执行时，它先检查shutdown()方法是否已经被调用了。如果是，那么执行器就拒绝这个任务。
 *首先，执行器会寻找通过setRejectedExecutionHandler()方法设置的用于被拒绝的任务的处理程序，如果找到一个处理程序，
 *执行器就调用其rejectedExecution()方法；否则就抛出 RejectedExecutionExeption异常。这是一个运行时异常，因此并不需要catch语句来对其进行处理。
 ***
 */
public class RejectedTaskController implements RejectedExecutionHandler {

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

		System.out.printf(
				"RejectedTaskController: The task %s has been rejected\n",
				r.toString());

		System.out.printf("RejectedTaskController: %s\n", executor.toString());

		System.out.printf("RejectedTaskController: Terminating: %s\n",
				executor.isTerminating());

		System.out.printf("RejectedTaksController: Terminated: %s\n",
				executor.isTerminated());

	}
	
	public static void main(String[] args) {
		RejectedTaskController controller=new RejectedTaskController();
		ThreadPoolExecutor executor=(ThreadPoolExecutor) Executors.newCachedThreadPool();
		executor.setRejectedExecutionHandler(controller);
		System.out.printf("Main: Starting.\n");
		for (int i=0; i<3; i++) {
		Task task=new Task("Task"+i);
		executor.submit(task);
		}
		
		System.out.printf("Main: Shutting down the Executor.\n");
		executor.shutdown();

		System.out.printf("Main: Sending another Task.\n");
		Task task=new Task("RejectedTask");
		executor.submit(task);
		System.out.println("Main: End");
	}
	
	static class Task implements Runnable{

		private String name;
		public Task(String name){
			this.name=name;
		}

		@Override
		public void run() {
		 System.out.println("Task "+name+": Starting");
		try {
			long duration=(long)(Math.random()*10);
			System.out.printf("Task %s: ReportGenerator: Generating a report during %d seconds\n",name,duration);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
		  e.printStackTrace();
		}
		System.out.printf("Task %s: Ending\n",name);
		}
		
		public String toString() {
			return name;
		}

	}

}