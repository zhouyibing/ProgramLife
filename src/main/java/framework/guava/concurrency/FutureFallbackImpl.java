package framework.guava.concurrency;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.FutureFallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
/**
 * The FutureFallback interface is used as a backup or a default value for a Future
instance that has failed. FutureFallback is an interface with one method,
create(Throwable t).
By accepting a Throwable instance, we can decide whether we should attempt to
recover, return a default value, or propagate the exception.
 *
 ***
 */
public class FutureFallbackImpl implements FutureFallback<String> {
	@Override
	public ListenableFuture<String> create(Throwable t) throws Exception {
		if (t instanceof FileNotFoundException) {
			SettableFuture<String> settableFuture = SettableFuture.create();
			settableFuture.set("Not Found........");
			return settableFuture;
		}
		throw new Exception(t);
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException{
		ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
		ListenableFuture<String> future = executorService.submit(new Callable<String>() {

			@Override
			public String call() throws FileNotFoundException{
				FileInputStream inputStream = new FileInputStream("D:a.txt");
				return "";
			}
		});
		//bind the FutureFallback to ListenableFuture
		FutureFallback<String> fallback = new FutureFallbackImpl();
		Futures.withFallback(future, fallback, executorService);
		System.out.println(future.get());
		executorService.shutdown();
	}
}