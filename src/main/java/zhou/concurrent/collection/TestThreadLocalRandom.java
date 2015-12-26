package zhou.concurrent.collection;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 它使用线程局部变量。每个线程希望以不同的生成器生成随机数，但它们是来自相同类的管理，这对程序员是透明的。
 * 在这种机制下，你将获得比使用共享的Random对象为所有线程生成随机数更好的性能。
 * 
 * 我们使用ThreadLocalRandom的current()方法。这是一个静态方法，它返回当前线程的ThreadLocalRandom对象，你可以使用这个对象生成随机数。
 * 如果调用这个方法的线程没有与任何（ThreadLocalRandom）对象关联，这个类将创建一个新的ThreadLocalRandom对象。
 * 在这种情况下，你使用这个方法初始化与任务相关的随机数生成器，所以，在这个方法下次调用时，它将创建ThreadLocalRandom对象。
 *
 ***
 */
public class TestThreadLocalRandom {

	public static void main(String[] args) {

		Thread threads[] = new Thread[3];
		for(int i=0;i<3;i++){
			TaskLocalRandom taskLocalRandom = new TaskLocalRandom();
			threads[i] = new Thread(taskLocalRandom);
			threads[i].start();
		}
	}

	static class TaskLocalRandom implements Runnable{

		public TaskLocalRandom(){
			ThreadLocalRandom.current();
		}
		
		@Override
		public void run() {
			String name = Thread.currentThread().getName();
			for(int i=0;i<10;i++){
				System.out.printf("%s:%d\n",name,ThreadLocalRandom.current().nextInt(10));
			}
		}
	}
}
