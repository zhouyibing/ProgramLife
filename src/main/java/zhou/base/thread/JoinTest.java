package zhou.base.thread;

public class JoinTest {
	
	public static void main(String[] args) throws InterruptedException {

		Thread thread = new ThreadObj();
		thread.start();
		//thread.run();
		thread.join(100000);
		System.out.println("main end!");
	}

	static class ThreadObj extends Thread{

		@Override
		public void run() {
			String ThreadName = Thread.currentThread().getName();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(int i=0;i<5;i++){
				System.out.println(ThreadName+":"+i);
				Thread.yield();
			}
			System.out.println("thread is end!");
		}
	}
}
