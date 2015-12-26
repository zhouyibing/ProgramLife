package zhou.base.thread.syn;

public class StaticSyncDemo extends SyncDemo{

	public static synchronized void staticMethod(){
		System.out.println(Thread.currentThread().getName()+":static synchronized method invoked!");
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
