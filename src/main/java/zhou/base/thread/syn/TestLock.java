package zhou.base.thread.syn;

public class TestLock {
	static LockClass class1 = new LockClass();
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {

		Thread thread1 = new Thread(new ThreadObj());
		Thread thread2 = new Thread(new ThreadObj());
		thread1.start();
		thread2.start();
		Thread.sleep(10000);
		class1.lockMethod2();
	}

	static class ThreadObj implements Runnable{

		@Override
		public void run() {
			class1.lockMethod();
		}
		
	}
}
