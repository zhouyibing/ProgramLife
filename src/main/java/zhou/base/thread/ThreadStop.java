package zhou.base.thread;

public class ThreadStop {
	public static void main(String[] args) {
		Thread t1 = new Thread(){

			@Override
			public void run() {
				while(true){
					System.out.println("线程运行中...");
				}
			}
			
		};
		
		t1.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t1.interrupt();
	}

}
