package zhou.concurrent.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * Fork/Join框架使用的线程叫工作者线程。Java包含继承Thread类的ForkJoinWorkerThread类和使用Fork/Join框架实现工作者线程。
 * 如果你想在ForkJoinPool类中使用MyWorkerThread线程，你必须实现自己的线程工厂。
 * 对于Fork/Join框架，这个工厂必须实现ForkJoinPool.ForkJoinWorkerThreadFactory类。
 * 为此，你已实现MyWorkerThreadFactory类。这个类只有一个用来创建一个新的MyWorkerThread对象的方法。
 *
 */
public class TestWorkThreadFactory {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		MyWorkerThreadFactory factory=new MyWorkerThreadFactory();
		ForkJoinPool pool=new ForkJoinPool(4, factory, null, false);//使用工作线程创建工厂
		int array[]=new int[100000];
		for (int i=0; i<array.length; i++){
		array[i]=1;
		}
		MyRecursiveTask task=new MyRecursiveTask(array,0,array.length);
		pool.execute(task);
		task.join();
		pool.shutdown();
		pool.awaitTermination(1, TimeUnit.DAYS);
		System.out.printf("Main: Result: %d\n",task.get());
		System.out.printf("Main: End of the program\n");
	}

	static class MyRecursiveTask extends RecursiveTask<Integer>{
		private static final long serialVersionUID = -8587811339051444040L;

		private int array[];
		private int start,end;
		
		private MyRecursiveTask(int[] array, int start, int end) {
			this.array = array;
			this.start = start;
			this.end = end;
		}

		@Override
		protected Integer compute() {
			Integer ret=1;
			if(end-start<1000){
			MyWorkThread thread  = (MyWorkThread)Thread.currentThread();
			try {
				thread.addTask();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			}else{
				int mid = (end+start)/2;
				MyRecursiveTask task1 = new MyRecursiveTask(array, start, mid+1);
				MyRecursiveTask task2 = new MyRecursiveTask(array, mid+1, end);
				invokeAll(task1,task2);
				ret = addResults(task1, task2);
			}
			return ret;
		}
		
		private Integer addResults(MyRecursiveTask task1, MyRecursiveTask task2) {
			int value;
			try {
			value = task1.get().intValue()+task2.get().intValue();
			} catch (InterruptedException e) {
			e.printStackTrace();
			value=0;
			} catch (ExecutionException e) {
			e.printStackTrace();
			value=0;
			}
			return value;
	 }
	}
}
