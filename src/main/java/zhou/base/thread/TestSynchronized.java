package zhou.base.thread;

public class TestSynchronized {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
       final SynObject so = new SynObject();
       Thread a = new Thread(new Runnable() {
		@Override
		public void run() {
			so.a();
		}
	});
       a.start();
	}

	static class SynObject{
		
		public synchronized void a(){
			System.out.println("a..");
			b();
		}
		
		public synchronized void b(){
			System.out.println("b...");
		}
	}
}
