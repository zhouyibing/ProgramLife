package framework.guava.concurrency;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class TestListenableFuture {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ListeningExecutorService executorService = MoreExecutors
				.listeningDecorator(Executors.newFixedThreadPool(10));
		ListenableFuture<String> listenableFuture = executorService
				.submit(new Callable<String>() {

					@Override
					public String call() throws Exception {
						return getRandomString(10);
					}
				});
		listenableFuture.addListener(new Runnable() {
			// 当task执行完成后，加入的listener中的处理逻辑将会执行.但是不能在此runnable中获得task执行的结果
			@Override
			public void run() {
				System.out.println("I will running after the task completed!");
			}
		}, executorService);
		System.out.println(listenableFuture.get());
		executorService.shutdown();
	}

	@Test
	public void testCallback() {
		ListeningExecutorService executorService = MoreExecutors
				.listeningDecorator(Executors.newFixedThreadPool(10));
		ListenableFuture<String> futureTask = executorService
				.submit(new Callable<String>() {
					@Override
					public String call() throws Exception {
						//throw new RuntimeException("dd");
						return "Task completed";
					}
				});
		FutureCallbackImpl callback = new FutureCallbackImpl();
		Futures.addCallback(futureTask, callback);
		System.out.println(callback.getCallbackResult());
		// Assuming success, would return "Task completed successfully"
		executorService.shutdown();
	}
	
/**
 * The FutureCallback interface specifies the onSuccess and onFailure methods.
The onSuccess method takes the result of the Future instance as an argument
so we have access to the result of our task.
 *
 ***
 */
	public class FutureCallbackImpl implements FutureCallback<String> {
		private StringBuilder builder = new StringBuilder();

		@Override
		public void onSuccess(String result) {
			builder.append(result).append(" successfully");
		}

		@Override
		public void onFailure(Throwable t) {
			builder.append("FutureCallbackImpl:"+t.toString());
		}

		public String getCallbackResult() {
			return builder.toString();
		}
	}

	// 随机生成字符串
	public static String getRandomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(62);
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}
}
