package zhou.akka.blocking;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import zhou.akka.PrintActor;
import zhou.akka.fault.SeparateDispatcherFutureActor;

public class BlockingDispatcherTest {
  public static void main(String args[]) {
    ActorSystem system = ActorSystem.create("BlockingDispatcherTest");

    try {
      // #blocking-main
      //ActorRef actor1 = system.actorOf(Props.create(BlockingFutureActor.class));
      ActorRef actor1 = system.actorOf(Props.create(SeparateDispatcherFutureActor.class));
      ActorRef actor2 = system.actorOf(Props.create(PrintActor.class));

      for (int i = 0; i < 100; i++) {
        actor1.tell(i, ActorRef.noSender());
        actor2.tell(i, ActorRef.noSender());
      }
      // #blocking-main
      Thread.sleep(5000 * 6);

    } catch (InterruptedException e) {
      // swallow the exception
    } finally {
      system.terminate();
    }
  }
}