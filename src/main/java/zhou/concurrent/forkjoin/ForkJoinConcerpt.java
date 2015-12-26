package zhou.concurrent.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * 
 ForkJoinPool类看作一个特殊的 Executor 执行器类型。这个框架基于以下两种操作。
分解（Fork）操作：当需要将一个任务拆分成更小的多个任务时，在框架中执行这些任务；
合并（Join）操作：当一个主任务等待其创建的多个子任务的完成执行。

Fork/Join框架和执行器框架（Executor Framework）主要的区别在于工作窃取算法（Work-Stealing Algorithm）。
与执行器框架不同，使用Join操作让一个主任务等待它所创建的子任务的完成，
执行这个任务的线程称之为工作者线程（Worker Thread）。工作者线程寻找其他仍未被执行的任务，
然后开始执行。通过这种方式，这些线程在运行时拥有所有的优点，进而提升应用程序的性能。

Fork/Join框架的核心是由下列两个类组成的。
ForkJoinPool：这个类实现了ExecutorService接口和工作窃取算法（Work-Stealing Algorithm）。
它管理工作者线程，并提供任务的状态信息，以及任务的执行信息。
ForkJoinTask：这个类是一个将在ForkJoinPool中执行的任务的基类。

RecursiveAction：用于任务没有返回结果的场景。
RecursiveTask：用于任务有返回结果的场景。
 * @author zhou
 *
 */
public class ForkJoinConcerpt {
	
	public static void main(String[] args) {
		List<Product> products=Product.generate(10000);
		Task task = new Task(products, 0, products.size(), 0.20);
		//它将执行默认的配置。创建一个线程数等于计算机CPU数目的线程池
		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(task);//execute()方法是异步调用的
		pool.execute(ForkJoinTask.adapt(new Runnable(){//通过ForkJoinTask.adapt将runnable/callable对象转化为ForkJoinTask
			@Override
			public void run() {
				 System.out.println("This runnable will adapt to ForkJoinTask");
			}
		}));
		//pool.invoke(task);//ForkJoinPool类的invoke()方法则是同步调用的
		pool.execute(new Runnable() {//运行runable时不采用工作窃取算法
			
			@Override
			public void run() {
				System.out.println("I'm runnable ...");
			}
		});
		do{
			System.out.printf("Main:Tread Count:%d\n",pool.getActiveThreadCount());
			System.out.printf("Main:Tread Steal:%d\n",pool.getStealCount());
			System.out.printf("Main:Tread Parallelism:%d\n",pool.getParallelism());
			try {
				TimeUnit.MILLISECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(!task.isDone());
		//关闭任务池
		pool.shutdown();
		//任务是否正常结束
		if(task.isCompletedNormally()){
			System.out.printf("Main: The process has completed normally.\n");
		}
		
		//打印处理结果
		for (int i=0; i<products.size(); i++){
			Product product=products.get(i);
			if (product.getPrice()!=12) {
			System.out.printf("Product %s: %f\n",product.
			getName(),product.getPrice());
			}
			}
		System.out.println("Main: End of the program.\n");
	}

	static class Product{
		private String name;
		private double price;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
		
		public static List<Product> generate(int size){
			List<Product> ret = new ArrayList<Product>();
			for(int i =0;i<size;i++){
				Product product=new Product();
				product.setName("Product "+i);
				product.setPrice(10);
				ret.add(product);
			}
			return ret;
		}
	}
	
	static class Task extends RecursiveAction{
		private static final long serialVersionUID = 1L;
		private List<Product> products;
		private int first;
		private int last;
		private double increment;//用来存储产品价格的增加额
		
		public Task (List<Product> products, int first, int last, double increment) {
				this.products=products;
				this.first=first;
				this.last=last;
				this.increment=increment;
				}

		@Override
		protected void compute() {
			if(last-first<10){
				updatePrices();
			}else{
				int mid = (last+first)/2;
				System.out.printf("Task:Pending tasks:%s\n",getQueuedTaskCount());
				Task t1 = new Task(products, first, mid+1, increment);
				Task t2 = new Task(products, mid+1, last, increment);
				//调用invokeAll()方法来执行一个主任务所创建的多个子任务  这是一个同步调用，这个任务将等待子任务完成，然后继续执行（也可能是结束）
				invokeAll(t1, t2);
			}
		}

		private void updatePrices() {
			for (int i=first; i<last; i++){
				Product product=products.get(i);
				product.setPrice(product.getPrice()*(1+increment));
				}
		}
	}
}
