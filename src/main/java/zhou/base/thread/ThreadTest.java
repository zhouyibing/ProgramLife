package zhou.base.thread;

/**
 * 子线程循环10次，接着主线程循环100，接着又回到子线程循环10次，接
 * 着再回到主线程又循环100，如此循环50次
 * @author Zhou Yibing
 *
 */
public class ThreadTest {
	public static Object lock=new Object();//同步锁标志，也可使用本类作为锁ThreadTest.class
	public static boolean subThread=true;//是否运行子线程
	
	public static void main(String[] args) {

		//子线程执行
		SubThread bus=new SubThread();
		new Thread(bus).start();
		
		//主线程执行
		   for(int m=1;m<=50;m++){
	    synchronized (lock) {
		   if(subThread){
			try {
				lock.wait();//如果是运行子线程，则释放锁
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		   }
		   for(int k=1;k<=100;k++)
			   System.out.println(Thread.currentThread().getName()+" run:"+m+","+k);
		   subThread=true;//运行完主线程后开始运行子线程
		   lock.notify();//通知锁等待池
		   }
		}
	}

	static class SubThread implements Runnable{
		
		@Override
		public void run() {
			 for(int i=1;i<=50;i++){
			  synchronized (lock) {
				 if(!subThread){
					try {
						lock.wait();//如果不是运行子线程，则释放锁
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				   }
				 for(int j=1;j<=10;j++)
				 System.out.println(Thread.currentThread().getName()+" run:"+i+","+j);
				 subThread=false;//运行完子线程后开始运行主线程
				 lock.notify();//通知锁等待池
			 }
		 }
		}
	}
}
