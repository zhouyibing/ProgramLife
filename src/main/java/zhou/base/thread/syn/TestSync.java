package zhou.base.thread.syn;

public class TestSync {

	public static void main(String[] args) {
		syncInherit();
	}

	//同步方法的继承
	static void syncInherit(){
		final StaticSyncDemo demo3 = new StaticSyncDemo();
		Thread thread7 = new Thread(new Runnable(){
		@Override
		public void run() {
		demo3.syncMethod();
		}
		
		});
		Thread thread8 = new Thread(new Runnable(){
		@Override
		public void run() {
		demo3.syncMethod();
		}
		
		});
		thread7.start();
		thread8.start();
	}
	
	//访问同一对象的同步方法
	static void visitSameObjectSyncMethod(){
		final SyncDemo demo2 = new SyncDemo();
		Thread thread5 = new Thread(new Runnable(){
			@Override
			public void run() {
				demo2.syncMethod();
			}
			
		});
		Thread thread6 = new Thread(new Runnable(){
			@Override
			public void run() {
				demo2.syncMethod();
			}
			
		});
		thread5.start();
		thread6.start();
	}
	
	//访问非同一对象的同步方法
	static void visitNonSameObjectSyncMethod(){
		Thread thread3 = new Thread(new Runnable(){
			@Override
			public void run() {
				SyncDemo demo = new SyncDemo();
				demo.syncMethod();
			}
			
		});
		Thread thread4 = new Thread(new Runnable(){
			@Override
			public void run() {
				SyncDemo demo = new SyncDemo();
				demo.syncMethod();
			}
			
		});
		thread3.start();
		thread4.start();
	}
	
	//访问静态同步方法
	static void vistiStaticSyncMethod(){
		Thread thread1 = new Thread(new Thread1());
		Thread thread2 = new Thread(new Thread1());
		thread1.start();
		thread2.start();
	}
	
	static class Thread1 implements Runnable{
		
		@Override
		public void run() {
			StaticSyncDemo.staticMethod();
		}
	}
}
