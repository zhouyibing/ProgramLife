package zhou.akka.actor.dependcy;

import java.util.Random;

import akka.actor.UntypedActor;
import akka.japi.Creator;

public class DInterface extends UntypedActor{

        private final String name;
	    
		public DInterface(String actorName) {
			this.name = actorName;
	    }

		@Override
		public void onReceive(Object msg) throws Exception {
			businessHandle(msg);
		}
		
		private void businessHandle(Object msg) throws InterruptedException{
			String actorDes = "("+Thread.currentThread().getName()+")"+this.name;
			System.out.println(actorDes+":received a msg---"+msg);
			Random r = new Random();
			long delay = r.nextInt(1000);
			System.out.println(actorDes+":the actor handing the business.");
			Thread.sleep(delay);
			getSender().tell(actorDes+":finished!", self());
		}
		
		 static class DInterfaceCreator implements Creator<DInterface>{

			private static final long serialVersionUID = 8530237625952588268L;

			private String name;
			
			public DInterfaceCreator(String name) {
				this.name = name;
			}

			@Override
			public DInterface create() throws Exception {
				return new DInterface(name);
			}
		}
}