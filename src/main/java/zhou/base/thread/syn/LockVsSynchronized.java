package zhou.base.thread.syn;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 显示锁是对象级别的锁，内部锁是类级别的锁。Lock锁是跟随对象的，synchronized锁是跟随类的。把Lock定义为
 * 多线程的私有属性是不起到资源互斥的作用，除非把Lock定义所有多线程的共享变量
 * 显示锁与内部锁的区别
 * 1.Lock支持更细粒度的锁控制
 * 2.Lock是无阻塞锁，synchronized是阻塞锁，当线程A持有锁时，线程B也期望获得锁，如使用显示锁，则B为等待状态，如果使用内部锁，则B为阻塞状态
 * 3.Lock可实现公平锁，synchronized只能是非公平锁。当线程释放锁时，jvm会从等待该锁的线程中随机选取一个线程拥有该锁（不按照先来后到顺序），而
 * 使用显示锁时会，根据等待时间来选择哪个线程拥有锁。
 * 4.Lock是代码级的，synchronized是jvm级的
 * Lock是通过编码实现的，而synchronized是用jvm解释执行的。
 * @author Zhou Yibing
 *
 */
public class LockVsSynchronized {

	public static void main(String[] args) {
		try {
			runTasks(TaskWithLock.class);
			runTasks(TaskWithSynchronized.class);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void runTasks(Class<? extends Runnable> cls) throws InstantiationException, IllegalAccessException, InterruptedException{
		ExecutorService executorService = Executors.newCachedThreadPool();
		System.out.println("*************begin execute "+cls.getSimpleName()+"'s task******");
		for(int i =0;i<3;i++){
			executorService.submit(cls.newInstance());
		}
		TimeUnit.SECONDS.sleep(10);
		System.out.println("************finish task******");
		executorService.shutdown();
	}
	
	static class Task{
		public void doSomething(){
			try{
				Thread.sleep(2000);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			StringBuffer sb = new StringBuffer();
			sb.append("Thread name:"+Thread.currentThread().getName());
			sb.append(",execute time:"+Calendar.getInstance().get(13)+"s");
			System.out.println(sb);
		}
	}
	
	static class TaskWithLock extends Task implements Runnable{

		//declare the lock ,the lock is thread's private variable
		//private final Lock lock = new ReentrantLock();
		
		//declate a lock that the lock is a static variable,all TaskWithLock object share this variable.
		private static final Lock lock = new ReentrantLock();
		
		@Override
		public void run() {
		    try{
		    	lock.lock();
		    	doSomething();
		    }finally{
		    	lock.unlock();
		    }
		}
	}
	
	static class TaskWithSynchronized extends Task implements Runnable{

		@Override
		public void run() {
			synchronized("a"){
				doSomething();
			}
		}
	}
	
	/**
	 * this class used a ReentrantReadWriteLock,the lock that allow multiple read operation but only one write opertion.
	 * when a write thread is executing,another of all threads will be blocked unless the wite thread is release the resource.
	 * 
	 * warning:if you want use the class,you must define this class's object as a shared object that all threads visited it as the same object.
	 * @author Zhou Yibing
	 *
	 */
	static class ReadWriteLock{
		
		private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		
		private final Lock r = lock.readLock();
		
		private final Lock w = lock.writeLock();
		
		public void read(){
			try{
				r.lock();
				Thread.sleep(1000);
				System.out.println("read...");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally{
				r.unlock();
			}
		}
		
		public void write(){
			try{
				w.lock();
				Thread.sleep(1000);
				System.out.println("write...");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally{
				w.unlock();
			}
		}
	}
}
