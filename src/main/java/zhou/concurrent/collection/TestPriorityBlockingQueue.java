package zhou.concurrent.collection;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * 你想要添加到PriorityBlockingQueue中的所有元素必须实现Comparable接口。
 * 这个接口有一个compareTo()方法，它接收同样类型的对象，你有两个比较的对象：一个是执行这个方法的对象，
 * 另一个是作为参数接收的对象。如果本地对象小于参数，则该方法返回小于0的数值。如果本地对象大于参数，则该方法返回大于0的数值。
 * 如果本地对象等于参数，则该方法返回等于0的数值。
 * PriorityBlockingQueue使用compareTo()方法决定插入元素的位置。（校注：默认情况下）较大的元素将被放在队列的尾部。
 * 阻塞数据结构（blocking data structure）是PriorityBlockingQueue的另一个重要特性。
 * 它有这样的方法，如果它们不能立即进行它们的操作，则阻塞这个线程直到它们的操作可以进行。
 * 
 *clear()：这个方法删除队列中的所有元素。
 *take()：这个方法返回并删除队列中的第一个元素。如果队列是空的，这个方法将阻塞线程直到队列有元素。
 *put(E e)：E是用来参数化PriorityBlockingQueue类的类。这个方法将作为参数传入的元素插入到队列中。
 *peek()：这个方法返回列队的第一个元素，但不删除它。
 ***
 */
public class TestPriorityBlockingQueue {

	public static void main(String[] args) {
		PriorityBlockingQueue<Event> queue = new PriorityBlockingQueue<Event>();
		Thread taskThreads[] = new Thread[5];
		for (int i = 0; i < taskThreads.length; i++) {
			Task task = new Task(i, queue);
			taskThreads[i] = new Thread(task);
		}
		for (int i = 0; i < taskThreads.length; i++) {
			taskThreads[i].start();
		}
		for (int i = 0; i < taskThreads.length; i++) {
			try {
				taskThreads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.printf("Main: Queue Size: %d\n", queue.size());
		for (int i = 0; i < taskThreads.length * 1000; i++) {
			Event event = queue.poll();
			System.out.printf("Thread %s: Priority %d\n", event.getThread(),
					event.getPriority());
		}

		System.out.printf("Main: Queue Size: %d\n", queue.size());
		System.out.printf("Main: End of the program\n");
	}

	static class Event implements Comparable<Event> {
		private int thread;
		private int priority;

		public Event(int thread, int priority) {
			this.thread = thread;
			this.priority = priority;
		}

		public int getThread() {
			return thread;
		}

		public int getPriority() {
			return priority;
		}

		public int compareTo(Event e) {
			if (this.priority > e.getPriority()) {
				return -1;
			} else if (this.priority < e.getPriority()) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	static class Task implements Runnable {
		private int id;
		private PriorityBlockingQueue<Event> queue;

		public Task(int id, PriorityBlockingQueue<Event> queue) {
			this.id = id;
			this.queue = queue;
		}

		public void run() {
			for (int i = 0; i < 1000; i++) {
				Event event = new Event(id, i);
				queue.add(event);
			}
		}
	}

}
