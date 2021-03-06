package framework.guava.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;

/**
 * The AsyncFunction interface is closely related to the Function interface we covered
in Chapter 3, Functional Programming with Guava. Both accept an input object. The
difference is that the AsyncFunction interface returns ListenableFuture as an
output object. We call the ListenableFuture.get method when we retrieve the
transformation result of the AsyncFunction interface. The AsyncFunction interface
is used when we want to perform our transformation asynchronously without
having a blocking call (although calling the Future.get method could block if the
task has not been completed). But the AsyncFunction interface is not required to
perform its transformation asynchronously; it's only required to return a Future
instance.
 ***
 */
public class AsyncFuntionSample implements AsyncFunction<Long, String> {
	private ConcurrentMap<Long, String> map = Maps.newConcurrentMap();
	private ListeningExecutorService listeningExecutorService;
	

	private AsyncFuntionSample(ListeningExecutorService listeningExecutorService) {
		this.listeningExecutorService = listeningExecutorService;
	}

	@Override
	public ListenableFuture<String> apply(final Long input) throws Exception {
		if (map.containsKey(input)) {
			//The SettableFuture class is a ListenableFuture interface that we can use to set the value to be returned, or we can set ListenableFuture to Fail with a given exception.
			SettableFuture<String> listenableFuture = SettableFuture.create();
			listenableFuture.set(map.get(input));
			return listenableFuture;
		} else {
			return listeningExecutorService.submit(new Callable<String>() {
				@Override
				public String call() throws Exception {
					map.putIfAbsent(input, "a");
					return "a";
				}
			});
		}
	}
	
	public static void main(String[] args) throws Exception {
		ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
		AsyncFuntionSample asyncFuntionSample = new AsyncFuntionSample(executorService);
		System.out.println(asyncFuntionSample.apply(1L).get());
		System.out.println(asyncFuntionSample.apply(1L).get());
		executorService.shutdown();
	}
}