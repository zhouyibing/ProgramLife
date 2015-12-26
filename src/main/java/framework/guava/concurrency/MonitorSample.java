package framework.guava.concurrency;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.util.concurrent.Monitor;

/**
 * 1. Monitor.enter: The Monitor.enter method will attempt to enter a
monitor and will block indefinitely until the thread enters the monitor.
2. Monitor.enterIf: The Monitor.enterIf method takes Monitor.Guard as
an argument and will block to enter the monitor. Once it enters the monitor,
it will not wait for the condition to be satisfied, but it will return a boolean
indicating whether the Monitor block was entered.
3. Monitor.enterWhen: The Monitor.enterWhen method also takes Monitor.
Guard as an argument and blocks, waiting to enter the monitor. However, once
the lock is obtained, it will wait indefinitely for the condition to be satisfied.
4. Monitor.tryEnter: The Monitor.tryEnter method will attempt to access
the monitor but if it is already occupied by another thread, it will not wait
at all to obtain the lock but will return a boolean indicating whether the
Monitor block was entered.
5. Monitor.tryEnterIf: The Monitor.tryEnterIf method attempts to
immediately enter the monitor only if the lock is available and the condition is
satisfied; otherwise, it will not wait for the lock or the condition to be satisfied
but will return a boolean indicating whether the Monitor block was entered.
 *
 ***
 */
public class MonitorSample {
	private List<String> list = new ArrayList<String>();
	private static final int MAX_SIZE = 10;
	private static final int MIN_SIZE = 5;
	private Monitor monitor = new Monitor();
	private Monitor.Guard listBelowCapacity = new Monitor.Guard(monitor) {
		@Override
		public boolean isSatisfied() {
			return list.size() < MAX_SIZE;
		}
	};
	private Monitor.Guard listAboveCapacity = new Monitor.Guard(monitor) {
		
		@Override
		public boolean isSatisfied() {
			return list.size()>MIN_SIZE;
		}
	};

	public void addToList(String item) throws InterruptedException {
		monitor.enterWhen(listBelowCapacity);
		try {
			list.add(item);
		} finally {
			monitor.leave();
		}
	}
	
	public void removeList(int index) throws InterruptedException {
		if(monitor.enterIf(listAboveCapacity)){
			try {
				list.remove(index);
			} finally {
				monitor.leave();
			}
		}
	}
	
	public static void main(String[] args) {
		MonitorSample monitorSample = new MonitorSample();
		for(int i=0;i<20;i++){
			try {
				System.out.println("put a"+i+" into the list");
				monitorSample.addToList("a"+i);
				System.out.println("end.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testRemove(){
		MonitorSample monitorSample = new MonitorSample();
        for(int i=0;i<10;i++){
        	try {
				monitorSample.addToList("a"+i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
        
		for(int i=0;i<20;i++){
			try {
				monitorSample.removeList(i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(monitorSample.list.size());
	}
}