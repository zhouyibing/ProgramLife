package zhou.akka.example_pi;

import akka.actor.*;
import akka.routing.RoundRobinRouter;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by Zhou Yibing on 2015/10/23.
 */
public class Pi {

    public static void main(String[] args){
        Pi pi = new Pi();
        pi.calculate(4,10000,10000);
    }

    private void calculate(final int nrOfWorkers, final int nrOfElements,final int nrOrMessages) {
        ActorSystem system = ActorSystem.create("PiSystem");
        final  ActorRef listener = system.actorOf(Props.create(Listener.class),"listener");
        ActorRef master = system.actorOf(Props.create(Master.class,nrOfWorkers, nrOrMessages, nrOfElements, listener),"master");
        master.tell(new Calculate(),ActorRef.noSender());
    }

    public static class Master extends UntypedActor{

        private final int nrOfMessages;
        private final int nrOfElements;
        private double pi;
        private int nrOfResults;
        private final long start = System.currentTimeMillis();

        private final ActorRef listener;
        private final ActorRef workerRouter;

        public Master(final int nrOrWorkers,int nrOfMessages, int nrOfElements,ActorRef listener) {
            this.nrOfMessages = nrOfMessages;
            this.nrOfElements = nrOfElements;
            this.listener = listener;
            this.workerRouter = this.getContext().actorOf(Props.create(Worker.class).withRouter(new RoundRobinRouter(nrOrWorkers)),"workerRouter");
        }


        @Override
        public void onReceive(Object o) throws Exception {

            if(o instanceof Calculate){
                for(int start=0;start<nrOfMessages;start++){
                    workerRouter.tell(new Work(start,nrOfElements),getSelf());
                }
            }else if(o instanceof Result){
                Result result = (Result)o;
                pi+=result.getValue();
                nrOfResults+=1;
                if(nrOfResults == nrOfMessages){
                    Duration duration = Duration.create(System.currentTimeMillis()-start, TimeUnit.MICROSECONDS);
                    listener.tell(new PiApproximation(pi,duration),getSelf());
                    getContext().stop(getSelf());
                }
            }else{
                unhandled(o);
            }
        }
    }

    public static class Listener extends  UntypedActor{

        @Override
        public void onReceive(Object o) throws Exception {
            if(o instanceof PiApproximation){
                PiApproximation piApproximation = (PiApproximation)o;
                System.out.println(String.format("\n\tPi approximation: \t\t%s\n\tCalculation time: \t%s",piApproximation.getPi(),piApproximation.getDuration()));
                getContext().system().shutdown();
            }else{
                unhandled(o);
            }
        }
    }

    //worker to handle the calculate
    public static class Worker extends UntypedActor{

        @Override
        public void onReceive(Object message) throws Exception {
            if(message instanceof  Work){
                Work work = (Work)message;
                double result = calculatePiFor(work.getStart(),work.getNrOfElements());
                getSender().tell(new Result(result),getSelf());
            }else{
                unhandled(message);
            }
        }

        private double calculatePiFor(int start, int nrOfElements) {
            double acc = 0.0;
            for(int i=start*nrOfElements;i<((start+1)*nrOfElements-1);i++){
                acc+=4.0*(1-(i%2)*2)/(2*i+1);
            }
            return acc;
        }
    }


    /********* message  define *******/
    //Calculate �C sent to the Master actor to start the calculation
    static class Calculate{}

    //Work �C sent from the Master actor to the Worker actors containing the work assignment
    static class Work{
        private final int start;
        private final int nrOfElements;

        Work(int start, int nrOfElements) {
            this.start = start;
            this.nrOfElements = nrOfElements;
        }

        public int getStart(){return start;}
        public int getNrOfElements(){return nrOfElements;}
    }

    //Result �C sent from the Worker actors to the Master actor containing the result from the worker��s calculation
    static class Result{
        private final double value;

        Result(double value) {
            this.value = value;
        }

        public double getValue(){return value;}

        @Override
        public String toString() {
            return "Result{" +
                    "value=" + value +
                    '}';
        }
    }

    //PiApproximation �C sent from the Master actor to the Listener actor containing the the final pi result and how long time the calculation took
    static class PiApproximation{
        private final double pi;
        private final Duration duration;

        PiApproximation(double pi, Duration duration) {
            this.pi = pi;
            this.duration = duration;
        }

        public double getPi() {
            return pi;
        }

        public Duration getDuration() {
            return duration;
        }
    }
}
