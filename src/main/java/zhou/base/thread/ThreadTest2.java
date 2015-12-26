package zhou.base.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 子线程循环10次，接着主线程循环100，接着又回到子线程循环10次，接
 * 着再回到主线程又循环100，如此循环50次
 * @author Zhou Yibing
 *
 */
public class ThreadTest2 {

	private static Lock lock=new ReentrantLock();
	private static Condition subThreadCondition=lock.newCondition();
	private static boolean shouldSubThread=false;
	
	public static void main(String[] args) {

		ExecutorService threadPool=Executors.newFixedThreadPool(3);
		threadPool.execute(new Runnable(){

			@Override
			public void run() {
				for(int i=1;i<=50;i++){
					lock.lock();
					try {
					if(!shouldSubThread)
					{
						subThreadCondition.await();
					}
					for(int j=1;j<=10;j++){
						System.out.println(Thread.currentThread().getName()+" run:"+i+","+j);
					}
					shouldSubThread=false;
					subThreadCondition.signal();
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						lock.unlock();
					}
				}
			}
		});
		
		threadPool.shutdown();
		for(int i=1;i<=50;i++){
			lock.lock();
			try {
			if(shouldSubThread)
			{
				subThreadCondition.await();
			}
			for(int j=1;j<=100;j++){
				System.out.println(Thread.currentThread().getName()+" run:"+i+","+j);
			}
			shouldSubThread=true;
			subThreadCondition.signal();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				lock.unlock();
			}
		}
	}

}
