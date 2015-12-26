package zhou.concurrent.collection;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 存储到DelayedQueue类中的元素必须实现Delayed接口。这个接口允许你处理延迟对象，所以你将实现存储在DelayedQueue对象的激活日期，
 * 这个激活时期将作为对象的剩余时间，直到激活日期到来。这个接口强制实现以下两种方法：
 * compareTo(Delayed o)：Delayed接口继承Comparable接口。如果执行这个方法的对象的延期小于作为参数传入的对象时，该方法返回一个小于0的值。
 * 如果执行这个方法的对象的延期大于作为参数传入的对象时，该方法返回一个大于0的值。如果这两个对象有相同的延期，该方法返回0。
 * getDelay(TimeUnit unit)：该方法返回与此对象相关的剩余延迟时间，以给定的时间单位表示。TimeUnit类是一个枚举类，
 * 有以下常量：DAYS、HOURS、 MICROSECONDS、MILLISECONDS、 MINUTES、 NANOSECONDS 和 SECONDS。
 * 
 *clear()：这个方法删除队列中的所有元素。
 *offer(E e)：E是代表用来参数化DelayQueue类的类。这个方法插入作为参数传入的元素到队列中。
 *peek()：这个方法检索，但不删除队列的第一个元素。
 *take()：这具方法检索并删除队列的第一个元素。如果队列中没有任何激活的元素，执行这个方法的线程将被阻塞，直到队列有一些激活的元素。
 ***
 */
public class TestDelayedQueue {

	public static void main(String[] args) throws InterruptedException {
		DelayQueue<Event> queue = new DelayQueue<Event>();
		Thread threads[] = new Thread[5];
		for (int i = 0; i < threads.length; i++) {
			Task task = new Task(i + 1, queue);
			threads[i] = new Thread(task);
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}

		do {
			int counter = 0;
			Event event;
			do {
				event = queue.poll();
				if (event != null)
					counter++;
			} while (event != null);
			System.out.printf("At %s you have read %d events\n", new Date(),
					counter);
			TimeUnit.MILLISECONDS.sleep(500);
		} while (queue.size() > 0);
	}
}

class Event implements Delayed {
	private Date startDate;

	public Event(Date startDate) {
		this.startDate = startDate;
	}

	public int compareTo(Delayed o) {
		long result = this.getDelay(TimeUnit.NANOSECONDS)
				- o.getDelay(TimeUnit.NANOSECONDS);
		if (result < 0) {
			return -1;
		} else if (result < 0) {
			return 1;
		}
		return 0;
	}

	public long getDelay(TimeUnit unit) {
		Date now = new Date();
		long diff = startDate.getTime() - now.getTime();
		return unit.convert(diff, TimeUnit.MILLISECONDS);
	}
}

class Task implements Runnable {
	private int id;
	private DelayQueue<Event> queue;

	public Task(int id, DelayQueue<Event> queue) {
		this.id = id;
		this.queue = queue;
	}

	public void run() {
		Date now = new Date();
		Date delay = new Date();
		delay.setTime(now.getTime() + (id * 1000));
		System.out.printf("Thread %s: %s\n", id, delay);
		for (int i = 0; i < 100; i++) {
			Event event = new Event(delay);
			queue.add(event);
		}
	}
}