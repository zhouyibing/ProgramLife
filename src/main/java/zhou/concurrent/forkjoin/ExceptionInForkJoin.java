package zhou.concurrent.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
不能在ForkJoinTask类的compute()方法中抛出任务非运行时异常，因为这个方法的实现没有包含任何throws声明。
因此，需要包含必需的代码来处理相关的异常。另一方面，compute()方法可以抛出运行时异常（它可以是任何方法或者方法内的对象抛出的异常）。
ForkJoinTask类和ForkJoinPool类的行为与我们期待的可能不同。在控制台上，程序没有结束执行，不能看到任务异常信息。
如果异常不被抛出，那么它只是简单地将异常吞噬掉。然而，我们能够利用ForkJoinTask类的一些方法来获知任务是否有异常抛出，以及抛出哪一种类型的异常。


虽然运行这个程序时将抛出异常，但是程序不会停止。在Main主类中，调用原始任务ForkJoinTask类的isCompletedAbnormally()方法，
如果主任务或者它的子任务之一抛出了异常，这个方法将返回true。也可以使用getException()方法来获得抛出的Exception对象。
 ***
 */
public class ExceptionInForkJoin {

	public static void main(String[] args) {
		int array[]=new int[100];
		Task task=new Task(array,0,100);
		ForkJoinPool pool=new ForkJoinPool();
		pool.execute(task);
		pool.shutdown();
		try {
		pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
		e.printStackTrace();
		}
		if (task.isCompletedAbnormally()) {
			System.out.printf("Main: An exception has ocurred\n");
			System.out.printf("Main: %s\n",task.getException());
			}
			System.out.printf("Main: Result: %d",task.join());
	}

	static class Task extends RecursiveTask<Integer>{

		private int start,end;
		private int array[];
		
		private Task(int[] array,int start, int end) {
			this.start = start;
			this.end = end;
			this.array = array;
		}

		@Override
		protected Integer compute() {
			System.out.printf("Task: Start from %d to %d\n",start,end);
			if(end-start<10){
			 if ((3>start)&&(3<end)){
				throw new RuntimeException("This task throws an Exception: Task from "+start+" to "+end);
			 }
			 try {
				 TimeUnit.SECONDS.sleep(1);
				 } catch (InterruptedException e) {
				 e.printStackTrace();
				 }
		    }else{
		    	int mid=(end+start)/2;
		    	Task task1=new Task(array,start,mid);
		    	Task task2=new Task(array,mid,end);
		    	invokeAll(task1, task2);
		    }
			System.out.printf("Task: End form %d to %d\n",start,end);
			return 0;
		}
	}
}
