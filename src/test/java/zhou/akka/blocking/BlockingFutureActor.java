package zhou.akka.blocking;

import akka.actor.AbstractActor;
import akka.dispatch.Futures;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

// #blocking-in-future
class BlockingFutureActor extends AbstractActor {
  ExecutionContext ec = getContext().getDispatcher();

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(
            Integer.class,
            i -> {
              System.out.println(Thread.currentThread().getName()+":Calling blocking Future: " + i);
              Future<Integer> f =
                  Futures.future(
                      () -> {
                        Thread.sleep(5000);
                        System.out.println(Thread.currentThread().getName()+":Blocking future finished: " + i);
                        return i;
                      },
                      ec);
            })
        .build();
  }
}