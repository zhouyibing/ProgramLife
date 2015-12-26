package zhou.base.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 实现runnable接口的方式，没有返回值，不能抛出异常
 * 异步计算考虑使用callable接口
 * 尽可能多地占用系统资源，提供快速运算
 * 可以监控线程执行的情况， 比如是否执行完毕、是否有返回值、是否有异常等
 * 可以为用户提供更好的支持
 * @author Zhou Yibing
 *
 */
public class AsynchronousCalculate {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<Integer> future = executorService.submit(new TaxCaculate(100));
		while(!future.isDone()){
			TimeUnit.MILLISECONDS.sleep(200);
			System.out.print("#");
		}
		System.out.println("计算税金完成："+future.get());
		executorService.shutdown();
	}

	static class TaxCaculate implements Callable<Integer>{

		private int seedMoney;
		
		public TaxCaculate(int seedMoney) {
			this.seedMoney = seedMoney;
		}

		@Override
		public Integer call() throws Exception {
			TimeUnit.MILLISECONDS.sleep(10000);
			return seedMoney/10;
		}
		
	}
}
