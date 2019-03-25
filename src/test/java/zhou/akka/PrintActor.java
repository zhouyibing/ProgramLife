package zhou.akka;

import akka.actor.AbstractActor;

// #print-actor
public class PrintActor extends AbstractActor {
  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(
            Integer.class,
            i -> {
              System.out.println(Thread.currentThread().getName()+":PrintActor: " + i);
            })
            .matchAny(r->{
                System.out.println(r);
            })
        .build();
  }
}