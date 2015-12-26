package zhou.concurrent.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * 在ForkJoinPool类中执行ForkJoinTask对象时，在任务开始执行前可以取消它。
 * ForkJoinTask类提供了cancel()方法来达到取消任务的目的。在取消一个任务时必须要注意以下两点：
ForkJoinPool类不提供任何方法来取消线程池中正在运行或者等待运行的所有任务；
取消任务时，不能取消已经被执行的任务。
在本节，我们将实现一个取消ForkJoinTask对象的范例。
该范例将寻找数组中某个数字所处的位置。第一个任务是寻找可以被取消的剩余任务数。
由于Fork／Join框架没有提供取消功能，我们将创建一个辅助类来实现取消任务的操作。

ForkJoinTask类提供的cancel()方法允许取消一个仍没有被执行的任务，这是非常重要的一点。如果任务已经开始执行，
那么调用cancel()方法也无法取消。这个方法接收一个名为mayInterruptIfRunning的boolean值参数。顾名思义，
如果传递true值给这个方法，即使任务正在运行也将被取消。JavaAPI文档指出，ForkJoinTask类的默认实现，这个属性没有起作用。
如果任务还没有开始执行，那么这些任务将被取消。任务的取消对于已发送到线程池中的任务没有影响，它们将继续执行。
 **
 */
public class CancelTask {

	public static void main(String[] args) {
      int array[] = generateArray(1000);
      TaskManager manager = new TaskManager();
      ForkJoinPool pool = new ForkJoinPool();
      SearchNumberTask task = new SearchNumberTask(array,0,1000,4,manager);
      pool.execute(task);
      
      pool.shutdown();
      try {
    	  pool.awaitTermination(1, TimeUnit.DAYS);
    	  } catch (InterruptedException e) {
    	  e.printStackTrace();
    	  }    	   
      System.out.printf("Main: The program has finished\n");
	}
	public static int[] generateArray(int size) {
		int array[]=new int[size];
		Random random=new Random();
		for (int i=0; i<size; i++){
		array[i]=random.nextInt(10);
		}
		return array;
		}

	//利用这个类来存储在ForkJoinPool中执行的任务。由于ForkJoinPool和ForkJoinTask类的局限性，将利用TaskManager类来取消ForkJoinPool类中所有的任务。
	static class TaskManager{
		private List<ForkJoinTask<Integer>> tasks;
		
		public TaskManager(){
			tasks = new ArrayList<ForkJoinTask<Integer>>();
		}
		
		public void addTask(ForkJoinTask<Integer> task){
			tasks.add(task);
		}
		
		public void cancelTasks(ForkJoinTask<Integer> cancelTask){
			for(ForkJoinTask<Integer> task:tasks){
				if(task!=cancelTask){
					task.cancel(true);
					((SearchNumberTask)task).writeCancelMessage();
				}
			}
		}
	}
	
	static class SearchNumberTask extends RecursiveTask<Integer>{
		private int numbers[];
		private int start, end;
		private int number;
		private TaskManager manager;
		private final static int NOT_FOUND=-1;

		private SearchNumberTask(int[] numbers, int start, int end, int number,
				TaskManager manager) {
			super();
			this.numbers = numbers;
			this.start = start;
			this.end = end;
			this.number = number;
			this.manager = manager;
		}

		@Override
		protected Integer compute() {
			System.out.println("Task: "+start+":"+end);
			int ret;
			if (end-start>10) {
			ret=launchTasks();
			} else {
				ret=lookForNumber();
			}
			return ret;
		}

		private int lookForNumber() {
			for (int i=start; i<end; i++){
				if (numbers[i]==number) {
				System.out.printf("Task: Number %d found in position %d\n",number,i);
				manager.cancelTasks(this);
				return i;
			   }
			try {
				TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
				e.printStackTrace();
			}
			}
			return NOT_FOUND;
		}

		private int launchTasks() {
			int mid=(start+end)/2;
			SearchNumberTask task1=new SearchNumberTask(numbers,start,mid,number,manager);
			SearchNumberTask task2=new SearchNumberTask(numbers,mid,end,number,manager);
			//增加任务到TaskManager对象中。
			manager.addTask(task1);
			manager.addTask(task2);
			//调用fork()方法采用异步方式执行这两个任务。
			task1.fork();
			task2.fork();
			//等待任务结束，如果第一个任务返回的结果不为-1，则返回第一个任务的结果；否则返回第二个任务的结果。
			int returnValue;
			returnValue=task1.join();
			if (returnValue!=-1) {
			return returnValue;
			}
			returnValue=task2.join();
			return returnValue;

		}

		public void writeCancelMessage() {
			System.out.printf("Task: Cancelled task from %d to %d",start,end);
		}
	}
}
