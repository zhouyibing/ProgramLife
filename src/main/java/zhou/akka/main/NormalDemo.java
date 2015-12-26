package zhou.akka.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class NormalDemo {

	public static void main(String[] args) throws Exception {
		ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		int clientNums = 100; 
		List<Future<Long>> futures = new ArrayList<Future<Long>>();
		final NAction action = new NAction();
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
	}
	
	static class NAction{
		public Long execute(){
			System.out.println("Action:a request has coming.......................................");
			long startT = System.currentTimeMillis();
			NInterface pa = new NInterface("Interface A");
			NInterface pb = new NInterface("Interface B");
			NInterface pc = new NInterface("Interface C");
			pa.sendMsg("message from action to A");
			pb.sendMsg("message from action to B");
			pc.sendMsg("message from action to C");
			pa.call();
			pb.call();
			pc.call();
			return System.currentTimeMillis()-startT;
		}
	}
	
	static class NInterface{
		private String name;
		private Object msg;
		
		public NInterface(String name) {
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
