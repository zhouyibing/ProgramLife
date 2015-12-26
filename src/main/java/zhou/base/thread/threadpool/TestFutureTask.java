package zhou.base.thread.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
/**
 * FutureTask 类提供了一个名为 done() 的方法，允许在执行器中的任务执行结束之后，还可以执行一些代码。
 * 这个方法可以被用来执行一些后期处理操作，比如：产生报表，通过邮件发送结果或释放一些系统资源。当任务执行完成是受 FutureTask 类控制时，
 * 这个方法在内部被 FutureTask 类调用。在任务结果设置后以及任务的状态已改变为 isDone之后，无论任务是否被取消或者正常结束，
 * done()方法才被调用。默认情况下，done()方法的实现为空，即没有任何具体的代码实现。我们可以覆盖 FutureTask 类并实现done()方法来改变这种行为
 * 
 *当任务执行结束时， FutureTask类就会调用done()方法;以通过任务来关闭系统资源、输出日志信息、发送通知等
 ***
 */
public class TestFutureTask {

	public static void main(String[] args) {
		ExecutorService executor = (ExecutorService) Executors
				.newCachedThreadPool();
		ResultTask resultTasks[] = new ResultTask[5];
		for (int i = 0; i < 5; i++) {
			ExecutableTask executableTask = new ExecutableTask("Task " + i);
			resultTasks[i] = new ResultTask(executableTask);
			executor.submit(resultTasks[i]);
		}

		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		for (int i = 0; i < resultTasks.length-1; i++) {
			resultTasks[i].cancel(true);
		}

		for (int i = 0; i < resultTasks.length; i++) {
			try {
				if (!resultTasks[i].isCancelled()) {
					System.out.printf("%s\n", resultTasks[i].get());
				}

			} catch (InterruptedException  e) {
				e.printStackTrace();
			}catch (ExecutionException e1){
				e1.printStackTrace();
			}

		}
		executor.shutdown();

	}
}

class ExecutableTask implements Callable<String> {

	private String name;

	public String getName() {
		return name;
	}

	public ExecutableTask(String name) {
		this.name = name;
	}

	public String call() throws Exception {
		try {
			long duration = (long) (Math.random() * 10);
			System.out.printf("%s: Waiting %d seconds for results.\n",
					this.name, duration);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
		}
		return "Hello, world. I'm " + name;
	}
}

class ResultTask extends FutureTask<String> {
	private String name;

	public ResultTask(Callable<String> callable) {
		super(callable);
		this.name = ((ExecutableTask) callable).getName();
	}

	protected void done() {
		if (isCancelled()) {
			System.out.printf("%s: Has been canceled\n", name);
		} else {
			System.out.printf("%s: Has finished\n", name);
		}
	}
}