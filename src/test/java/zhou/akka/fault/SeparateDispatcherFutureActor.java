package zhou.akka.fault;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.dispatch.Futures;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

// #separate-dispatcher
public class SeparateDispatcherFutureActor extends AbstractActor {
    Config config =
            ConfigFactory.parseString(
                            "my-blocking-dispatcher {\n"
                            + "  type = Dispatcher\n"
                            + "  executor = \"akka.actor.default-dispatcher\"\n"
                            + "  thread-pool-executor {\n"
                            + "    fixed-pool-size = 16\n"
                            + "  }\n"
                            + "  throughput = 1\n"
                            + "}\n");
  ExecutionContext ec = getContext().getSystem().dispatchers().lookup("my-blocking-dispatcher");

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(
            Integer.class,
            i -> {
              System.out.println(Thread.currentThread().getName()+":Calling blocking Future on separate dispatcher: " + i);
              Future<Integer> f =
                  Futures.future(
                      () -> {
                        Thread.sleep(5000);
                        System.out.println(Thread.currentThread().getName()+":Blocking future finished: " + i);
                        getSelf().tell("finished"+i, ActorRef.noSender());
                        return i;
                      },
                      ec);
            })
            .matchAny(r->{
                System.out.println(Thread.currentThread().getName()+":"+r);
            })
        .build();
  }
}