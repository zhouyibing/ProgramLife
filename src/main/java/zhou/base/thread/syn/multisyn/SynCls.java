package zhou.base.thread.syn.multisyn;

public class SynCls {

	public synchronized void a(){
		System.out.println(Thread.currentThread().getName()+" get the lock and sleep 20s(a)");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void b(){
		System.out.println(Thread.currentThread().getName()+" get the lock and sleep 20s(b)");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
