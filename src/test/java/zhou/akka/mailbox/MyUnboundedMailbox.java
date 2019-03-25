package zhou.akka.mailbox;

import akka.actor.*;
import akka.dispatch.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.testkit.TestActor;
import com.typesafe.config.Config;
import scala.Option;
import zhou.akka.PrintActor;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MyUnboundedMailbox implements MailboxType,ProducesMessageQueue<MyUnboundedMailbox.MyMessageQueue>{
    public MyUnboundedMailbox(ActorSystem.Settings settings,Config config) {

    }

    @Override
    public MessageQueue create(Option<ActorRef> owner, Option<ActorSystem> system) {
        return new MyMessageQueue();
    }
    // Marker interface used for mailbox requirements mapping
    interface MyUnboundedMessageQueueSemantics{}
    public static class MyMessageQueue implements MessageQueue,MyUnboundedMessageQueueSemantics{
        private final Queue<Envelope> queue = new ConcurrentLinkedQueue<Envelope>();
        @Override
        public void enqueue(ActorRef receiver, Envelope handle) {
            System.out.println("enqueue:"+handle);
            queue.offer(handle);
        }

        @Override
        public Envelope dequeue() {
            System.out.println("dequeue...");
            return queue.poll();
        }

        @Override
        public int numberOfMessages() {
            return queue.size();
        }

        @Override
        public boolean hasMessages() {
            return queue.isEmpty();
        }

        @Override
        public void cleanUp(ActorRef owner, MessageQueue deadLetters) {
            System.out.println("cleanUp...");
            for(Envelope handle:queue)
                deadLetters.enqueue(owner,handle);
        }
    }
    public static class Test extends AbstractActor implements RequiresMessageQueue<MyUnboundedMessageQueueSemantics>{
        LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
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
    public static void main(String[] args){
        ActorSystem system = ActorSystem.create("system1");
        system.actorOf(
                Props.create(PrintActor.class, PrintActor::new)
                        .withDispatcher("custom-dispatcher"))
        .tell("hello",ActorRef.noSender());
        ActorSystem system2 = ActorSystem.create("system2");
        system2.actorOf(Props.create(Test.class, Test::new)).tell("hello",ActorRef.noSender());
    }
}
