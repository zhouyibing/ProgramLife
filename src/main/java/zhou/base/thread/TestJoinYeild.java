package zhou.base.thread;

public class TestJoinYeild {

	public static void main(String[] args) throws InterruptedException {
		MyThread thread1 = new MyThread();
		thread1.start();
		
		MyThread thread2 = new MyThread();
		thread2.start();
		//thread2.join();
		//thread2.join(100);
		thread2.yield();
		//没有获得锁之前是不能调用wait,notify,notifyAll方法，会抛出IllegalMonitorStateException
		//thread2.wait();
		//thread2.notify();
		System.out.println("main end...");
	}

	static class MyThread extends Thread{

		@Override
		public void run() {
			for(int i=0;i<100;i++){
				System.out.println(Thread.currentThread().getName()+" "+i);
			}
		}
		
	}
}
