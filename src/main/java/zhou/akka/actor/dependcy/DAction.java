package zhou.akka.actor.dependcy;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.pattern.Patterns;

public class DAction extends UntypedActor{
	ActorRef actorA= getContext().actorOf(Props.create(new DInterface.DInterfaceCreator("DInterfaceA")));
	ActorRef actorB= getContext().actorOf(Props.create(new DInterface.DInterfaceCreator("DInterfaceB")));
	ActorRef actorC= getContext().actorOf(Props.create(new DInterface.DInterfaceCreator("DInterfaceC")));
		public DAction() {
			super();
		}

		@Override
		public void onReceive(Object msg) throws Exception {
			long startT = System.currentTimeMillis();
			System.out.println("Action:a request has coming.......................................");
			Future<Object> futureA = Patterns.ask(actorA, "message from action to A", 10000);
			Future<Object> futureB = Patterns.ask(actorB, "message from action to B", 10000);
			Future<Object> futureC = Patterns.ask(actorC, "message from action to C", 10000);
			//collect the results
			String resultA =  (String) Await.result(futureA, Duration.apply(10000, "seconds"));
			String resultB =  (String) Await.result(futureB, Duration.apply(10000, "seconds"));
			String resultC =  (String) Await.result(futureC, Duration.apply(10000, "seconds"));
			//handling the results
			System.out.println(resultA);
			System.out.println(resultB);
			System.out.println(resultC);
			//caculate the total cost times,then return it to the sender
			sender().tell(System.currentTimeMillis()-startT, self());
		}
    }