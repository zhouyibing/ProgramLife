package zhou.base.thread.syn.multisyn;

public class TestSynCls {

	public static void main(String[] args) {

		final SynCls cls = new SynCls();
		Thread t1 = new Thread(new Runnable(){

			@Override
			public void run() {
				cls.a();
				cls.b();
			}
			
		});
		Thread t2 = new Thread(new Runnable(){

			@Override
			public void run() {
				cls.b();
				cls.a();
			}
			
		});
		t1.setName("thread-1");
		t2.setName("thread-2");
		t1.start();
		t2.start();
	}
}
