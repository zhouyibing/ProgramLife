package zhou.concurrent.collection;

import java.util.concurrent.ConcurrentLinkedDeque;
/**
 * 
 *getFirst()和getLast()：分别返回列表中第一个和最后一个元素，返回的元素不会从列表中移除。如果列表为空，这两个方法抛出NoSuchElementExcpetion异常。
 *peek()、peekFirst()和peekLast()：分别返回列表中第一个和最后一个元素，返回的元素不会从列表中移除。如果列表为空，这些方法返回null。
 *remove()、removeFirst()和removeLast()：分别返回列表中第一个和最后一个元素，返回的元素将会从列表中移除。如果列表为空，这些方法抛出NoSuchElementExcpetion异常。
 ***
 */
public class NonBlockSafeList {

	public static void main(String[] args) {

		ConcurrentLinkedDeque list = new ConcurrentLinkedDeque<>();
		Thread threads[] = new Thread[100];
		for (int i = 0; i < threads.length; i++) {
			AddTask task = new AddTask(list);
			threads[i] = new Thread(task);
			threads[i].start();
		}
		System.out.printf("Main: %d AddTask threads have been launched\n",
				threads.length);

		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("Main: Size of the List: %d\n", list.size());

		for (int i = 0; i < threads.length; i++) {
			PollTask task = new PollTask(list);
			threads[i] = new Thread(task);
			threads[i].start();
		}
		System.out.printf("Main: %d PollTask threads have been launched\n",
				threads.length);

		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("Main: Size of the List: %d\n", list.size());
	}

	static class AddTask implements Runnable {
		private ConcurrentLinkedDeque list;

		public AddTask(ConcurrentLinkedDeque list) {
			this.list = list;
		}

		@Override
		public void run() {
			String name = Thread.currentThread().getName();
			for (int i = 0; i < 10000; i++) {
				list.add(name + ": Element " + i);
			}
		}
	}

	static class PollTask implements Runnable {
		private ConcurrentLinkedDeque list;

		private PollTask(ConcurrentLinkedDeque list) {
			this.list = list;
		}

		@Override
		public void run() {
			for (int i = 0; i < 5000; i++) {
				list.pollFirst();
				list.pollLast();
			}
		}
	}
}
