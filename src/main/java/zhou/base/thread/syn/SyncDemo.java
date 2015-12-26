package zhou.base.thread.syn;

public class SyncDemo {

	public synchronized void syncMethod(){
		System.out.println(Thread.currentThread().getName()+":synchronized methode invoked!");
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void syncMethod2(){
		synchronized (this.getClass()) {
			System.out.println(Thread.currentThread().getName()+":synchronized methode invoked!");
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
	}
	
	public void commonMethod(){
		System.out.println(Thread.currentThread().getName()+":common methode invoked!");
	}
}
