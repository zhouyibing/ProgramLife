package zhou.akka.main;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import zhou.akka.actor.Action;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;

public class AkkaUndepencyDemo {

	public static void main(String[] args) throws Exception {
		final ActorSystem system = ActorSystem.create("sys");
		ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	    int clientNums = 100;
	    final ActorRef actorMain= system.actorOf(Props.create(Action.class));
	    List<java.util.concurrent.Future<Long>> futures = new ArrayList<java.util.concurrent.Future<Long>>();
		for(int i =0;i<clientNums;i++){
			futures.add(executorService.submit(new Callable<Long>() {
				
				public Long call() {
					Future<Object> response = Patterns.ask(actorMain, "main start", 1000000);
					try {
						long time = (Long) Await.result(response, Duration.apply(15000, "seconds"));
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
		system.shutdown();
		executorService.shutdown();
	}
}
