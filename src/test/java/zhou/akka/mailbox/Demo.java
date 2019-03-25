package zhou.akka.mailbox;

import akka.actor.AbstractActor;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.dispatch.ControlMessage;
import akka.dispatch.Envelope;
import akka.dispatch.PriorityGenerator;
import akka.dispatch.UnboundedStablePriorityMailbox;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.Config;

import java.util.Comparator;

public class Demo extends AbstractActor{
     LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    {
        for(Object msg:new Object[]{"lowpriority",
                "lowpriority",
                "highpriority",
                "pigdog",
                "pigdog2",
                "pigdog3",
                "highpriority",
                PoisonPill.getInstance()}){
            getSelf().tell(msg,getSelf());
        }
    }
    public static Props props(){
        return Props.create(Demo.class,Demo::new);
    }

    @Override
    public void postStop() throws Exception {
        log.info("postStop");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchAny(
                        message -> {
                            log.info(message.toString());
                        })
                .build();
    }
public static class Demo2 extends AbstractActor{
    LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    {
        for (Object msg :
                new Object[] {"foo", "bar", new MyControlMessage(), PoisonPill.getInstance()}) {
            getSelf().tell(msg, getSelf());
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchAny(
                        message -> {
                            log.info(message.toString());
                        })
                .build();
    }
}
public static class MyControlMessage implements ControlMessage {}
public static class MyPrioMailbox extends UnboundedStablePriorityMailbox{
        // needed for reflective instantiation
        public MyPrioMailbox(ActorSystem.Settings settings, Config config) {
            //// Create a new PriorityGenerator, lower prio means more important
            super(new PriorityGenerator() {
                @Override
                public int gen(Object message) {
                    if (message.equals("highpriority"))
                    return 0; // 'highpriority messages should be treated first if possible
                    else if (message.equals("lowpriority"))
                        return 2; // 'lowpriority messages should be treated last if possible
                    else if (message.equals(PoisonPill.getInstance()))
                        return 3; // PoisonPill when no other left
                    else return 1; // By default they go between high and low prio
                }
            });
        }
    }

    public static void main(String[] args){
        ActorSystem system = ActorSystem.create("system1");
        system.actorOf(Demo.props().withDispatcher("prio-dispatcher"));

        ActorSystem system2 = ActorSystem.create("system2");
        system2.actorOf(Props.create(Demo2.class,Demo2::new).withDispatcher("control-aware-dispatcher"));
    }
}
