package zhou.akka.fsm;

import akka.actor.AbstractFSM;
import akka.actor.ActorRef;
import akka.japi.pf.UnitMatch;

import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Buncher extends AbstractFSM<Buncher.State,Buncher.Data>{
    {
        startWith(State.Idle, Uninitialized.Uninitialized);

        when(
                State.Idle,
                matchEvent(
                        SetTarget.class,
                        Uninitialized.class,
                        (setTarget, uninitialized) ->
                                stay().using(new Todo(setTarget.getRef(), new LinkedList<>()))));

        onTransition(
                matchState(
                        State.Active,
                        State.Idle,
                        () -> {
                            // reuse this matcher
                            final UnitMatch<Data> m =
                                    UnitMatch.create(
                                            matchData(
                                                    Todo.class,
                                                    todo ->
                                                            todo.getTarget().tell(new Batch(todo.getQueue()), getSelf())));
                            m.match(stateData());
                        })
                        .state(
                                State.Idle,
                                State.Active,
                                () -> {
                                    System.out.println("abcd");
                                }));

        when(
                State.Active,
                Duration.ofSeconds(1L),
                matchEvent(
                        Arrays.asList(Flush.class, StateTimeout()),
                        Todo.class,
                        (event, todo) -> goTo(State.Idle).using(todo.copy(new LinkedList<>()))));

        whenUnhandled(
                matchEvent(
                        Queue.class,
                        Todo.class,
                        (queue, todo) -> goTo(State.Active).using(todo.addElement(queue.getObj())))
                        .anyEvent(
                                (event, state) -> {
                                    log()
                                            .warning(
                                                    "received unhandled request {} in state {}/{}",
                                                    event,
                                                    stateName(),
                                                    state);
                                    return stay();
                                }));

        initialize();
    }

    static final class SetTarget{
        private final ActorRef ref;

        public SetTarget(ActorRef ref) {
            this.ref = ref;
        }
        public ActorRef getRef(){
            return ref;
        }

        public String toString(){
            return "SetTarget{\""+ref+"\"}";
        }
    }
    static final class Queue{
        private final Object obj;
        public Queue(Object obj){
            this.obj=obj;
        }
        public Object getObj(){
            return obj;
        }

        @Override
        public String toString() {
            return "Queue{" + "obj=" + obj + '}';
        }
    }
    static final class Batch{
        private final List<Object> list;
        public Batch(List<Object> list){
            this.list=list;
        }
        public List<Object> getList() {
            return list;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Batch batch = (Batch) o;

            return list.equals(batch.list);
        }

        @Override
        public int hashCode() {
            return list.hashCode();
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("Batch{list=");
            list.stream()
                    .forEachOrdered(
                            e -> {
                                builder.append(e);
                                builder.append(",");
                            });
            int len = builder.length();
            builder.replace(len, len, "}");
            return builder.toString();
        }
    }
    static enum Flush {
        Flush
    }
    enum State {
        Idle,
        Active
    }
    // state data
    interface Data {}
    public static enum Uninitialized implements Data {
        Uninitialized
    }
    final class Todo implements Data{
        private final ActorRef target;
        private final List<Object> queue;

        public Todo(ActorRef target, List<Object> queue) {
            this.target = target;
            this.queue = queue;
        }

        public ActorRef getTarget() {
            return target;
        }

        public List<Object> getQueue() {
            return queue;
        }

        @Override
        public String toString() {
            return "Todo{" + "target=" + target + ", queue=" + queue + '}';
        }

        public Todo addElement(Object element) {
            List<Object> nQueue = new LinkedList<>(queue);
            nQueue.add(element);
            return new Todo(this.target, nQueue);
        }

        public Todo copy(List<Object> queue) {
            return new Todo(this.target, queue);
        }

        public Todo copy(ActorRef target) {
            return new Todo(target, this.queue);
        }
    }
}


