package zhou.concurrent.semaphore;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class TestSemaphore {

	public static void main(String[] args) {

		ExecutorService exec = Executors.newCachedThreadPool();
	
		//只能5个线程同时访问
		final Semaphore semp = new Semaphore(5);
		for(int index=0;index<20;index++){
			final int no = index;
			Runnable run = new Runnable(){
				public void run() {
				try{
					// 获取许可
					semp.acquire();
				    System.out.println("Accessing: " + no);

                    Thread.sleep((long) (Math.random() * 10000));
                    System.out.println("-----------------"+semp.availablePermits());
                    // 访问完后，释放
					semp.release();
				  } catch (InterruptedException e) {
                      e.printStackTrace();
              }
			}
			};
			exec.execute(run);
		}
		exec.shutdown();
	}

	static class BoundedExecutor{
		private final Executor exec;
		private final Semaphore semaphore;
		
		private BoundedExecutor(Executor exec, Semaphore semaphore) {
			this.exec = exec;
			this.semaphore = semaphore;
		}
		
		public void submitTask(final Runnable command) throws InterruptedException{
			semaphore.acquire();
			exec.execute(new Runnable() {
				
				@Override
				public void run() {
					try{
					command.run();
					}finally{
						semaphore.release();
					}
				}
			});
		}
	}
}
