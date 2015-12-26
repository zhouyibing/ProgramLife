package zhou.base.thread.syn;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockClass {

	private static Lock lock = new ReentrantLock();
	private Condition emp = lock.newCondition();
	
	public void lockMethod(){
		lock.lock();
		try{
		System.out.println(Thread.currentThread().getName()+":(lockMethod)I have get the lock!");
		emp.await();
		System.out.println(Thread.currentThread().getName()+":I am wake up");
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}
	
	public void lockMethod2(){
		lock.lock();
		try{
		System.out.println(Thread.currentThread().getName()+":(lockMethod2)I have get the lock!");
		emp.signalAll();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}
}
