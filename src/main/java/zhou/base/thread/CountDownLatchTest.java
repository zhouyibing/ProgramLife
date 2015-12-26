package zhou.base.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 100-meter race statistics the runer's average score
 * countDownLatch's effect is control a counter.when thread finished it will invoke
 * countDownLatch show itself is end of run.
 * it is specially effective is multiple sub thread calculate,such as the main 
 * thread statistics the sub thread's run result.
 * @author Zhou Yibing
 *
 */
public class CountDownLatchTest {

	/**
	 * 1.10个线程都开始运行，执行到begin.await后线程阻塞，等待begin的计数变0
	 * 2.主线程调用begin的countDown方法，使begin的计数为0
	 * 3.10个线程继续运行
	 * 4.主线程继续运行下一个语句，end的计数不为0,主线程等待。
	 * 5.每个线程运行结束时把end的计数器减1，标志着本线程运行完毕。
	 * 6.10个线程全部结束，end计数器为0
	 * 7.主线程继续执行，打印出成绩平均值
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		//runner's number
		int num = 10;
		//only starting running once
		CountDownLatch begin = new CountDownLatch(1);
		//have many runners,so has the same end countDownLatch
		CountDownLatch end = new CountDownLatch(num);
		
		ExecutorService executorService = Executors.newFixedThreadPool(num);
		List<Future<Integer>> futureList = new ArrayList<Future<Integer>>();
		for(int i = 0;i<num;i++){
			futureList.add(executorService.submit(new runner(begin,end)));
		}
		//shot the starting gun,the runners begin to run
		begin.countDown();
		//wait for the runners run at the endline
		end.await();
		int count=0;
		for(int i = 0;i<futureList.size();i++){
			count+=futureList.get(i).get();
		}
		System.out.println("the total score is:"+count);
		System.out.println("the average score of all is:"+count/num);
		executorService.shutdown();
	}

	static class runner implements Callable<Integer>{

		//begin signal
		private CountDownLatch begin;
		//end signal
		private CountDownLatch end;
		
		public runner(CountDownLatch begin, CountDownLatch end) {
			this.begin = begin;
			this.end = end;
		}

		@Override
		public Integer call() throws Exception {
			//the runner's score
			int score = new Random().nextInt(100);
			//wait for the starting gun
			begin.await();
			//running
			TimeUnit.MILLISECONDS.sleep(score);
			//end of running
			end.countDown();
			//print the runner's score
			System.out.println(Thread.currentThread().getName()+"'s score is "+score);
			return score;
		}
	}
}
