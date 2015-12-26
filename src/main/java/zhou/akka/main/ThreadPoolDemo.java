package zhou.akka.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolDemo {

	public static void main(String[] args) throws Exception {
		ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		int clientNums = 100; 
		List<Future<Long>> futures = new ArrayList<Future<Long>>();
		final PAction action = new PAction();
			for(int i =0;i<clientNums;i++){
				futures.add(executorService.submit(new Callable<Long>() {
					
					public Long call() {
						try {
							Long time = action.execute();
							System.out.println("the request send by "+Thread.currentThread().getName()+" has responsed cost "+time+" ms");
						    return time;
						} catch (Exception e) {
							e.printStackTrace();
						}
						return 0L;
					}
				})
				);
			}
			
			long totalCost = 0;
			for(java.util.concurrent.Future<Long> f:futures){
				totalCost+=f.get();
			}
			System.out.println("the average of cost time every request is "+totalCost/clientNums+" ms");
			executorService.shutdown();
			action.shutdown();
	}
	
	static class PAction{
		ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		public Long execute(){
			System.out.println("Action:a request has coming.......................................");
			long startT = System.currentTimeMillis();
			PInterface pa = new PInterface("Interface A");
			PInterface pb = new PInterface("Interface B");
			PInterface pc = new PInterface("Interface C");
			pa.sendMsg("message from action to A");
			pb.sendMsg("message from action to B");
			pc.sendMsg("message from action to C");
			List<Future<Object>> futures = new ArrayList<Future<Object>>();
			futures.add(executorService.submit(pa));
			futures.add(executorService.submit(pb));
			futures.add(executorService.submit(pc));
			
			for(Future<Object> future:futures){
				try {
					future.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
			return System.currentTimeMillis()-startT;
		}
		
		public void shutdown(){
			executorService.shutdown();
		}
	}
	
	static class PInterface implements Callable<Object>{
		private String name;
		private Object msg;
		
		public PInterface(String name) {
			super();
			this.name = name;
		}
		
		public void sendMsg(Object msg){
			this.msg = msg;
		}
		
		public Object call(){
			String actorDes = "("+Thread.currentThread().getName()+")"+this.name;
			System.out.println(actorDes+":received a msg---"+msg);
			Random r = new Random();
			long delay = r.nextInt(1000);
			System.out.println(actorDes+":the interface handing the business.");
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return actorDes+":finished!";
		}
	}
}
