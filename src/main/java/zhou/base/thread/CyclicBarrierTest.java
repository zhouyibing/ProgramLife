package zhou.base.thread;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
/**
 * CyclicBarrier关卡可以让所有线程全部处于等待状态，然后再满足条件的情况下继续执行，
 * 这就好比是一条起跑线，不管是如何到达起跑线的，只要到达这条起跑线就必须等待其他人员，待人员
 * 到齐后在各奔东西
 * @author Zhou Yibing
 *
 */
public class CyclicBarrierTest {

	public static void main(String[] args) {

		//设置汇集者数量及汇集后续工作
		CyclicBarrier barrier = new CyclicBarrier(2,new Runnable(){

			public void run() {
				System.out.println("隧道打通");
			}
		});
		
		new Thread(new worker(barrier)).start();
		new Thread(new worker(barrier)).start();
		new Thread(new worker(barrier)).start();
	}

	static class worker implements Runnable{

		private CyclicBarrier barrier;
		
		public worker(CyclicBarrier barrier) {
			this.barrier = barrier;
		}

		public void run() {
			try {
				TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000));
				System.out.println(Thread.currentThread().getName()+"-到达汇合点");
				//等待其他人到达汇合点
				barrier.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}catch ( BrokenBarrierException e1){
				e1.printStackTrace();
			}
		}
		
	}
}
