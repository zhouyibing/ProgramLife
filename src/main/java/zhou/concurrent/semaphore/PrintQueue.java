package zhou.concurrent.semaphore;

import java.util.concurrent.Semaphore;
/**
 *   1. 首先， 你要调用acquire()方法获得semaphore。
     2. 然后， 对共享资源做出必要的操作。
     3. 最后， 调用release()方法来释放semaphore。
    *
    *fairness的内容是指全java语言的所有类中，那些可以阻塞多个线程并等待同步资源释放的类（例如，semaphore)。默认情况下是非公平模式。
    *在这个模式中，当同步资源释放，就会从等待的线程中任意选择一个获得资源，但是这种选择没有任何标准。而公平模式可以改变这个行为并强制选择等待最久时间的线程。

	*随着其他类的出现，Semaphore类的构造函数容许第二个参数。这个参数必需是 Boolean 值。如果你给的是 false 值，
	*那么创建的semaphore就会在非公平模式下运行。如果你不使用这个参数，是跟给false值一样的结果。如果你给的是true值，
	*那么你创建的semaphore就会在公平模式下运行。
	
	*acquireUninterruptibly()：acquire()方法是当semaphore的内部计数器的值为0时，阻塞线程直到semaphore被释放。
	*在阻塞期间，线程可能会被中断，然后此方法抛出InterruptedException异常。而此版本的acquire方法会忽略线程的中断而且不会抛出任何异常。
	*tryAcquire()：此方法会尝试获取semaphore。如果成功，返回true。如果不成功，返回false值，并不会被阻塞和等待semaphore的释放。接下来是你的任务用返回的值执行正确的行动。
 ***
 */
public class PrintQueue {

	private final Semaphore semaphore;

	public PrintQueue() {
		semaphore = new Semaphore(1);
	}

	public void printJob(Object document) {

		try {
			semaphore.acquire();

			long duration = (long) (Math.random() * 10);
			System.out.printf(
					"%s: PrintQueue: Printing a Job during %d seconds\n",
					Thread.currentThread().getName(), duration);
			Thread.sleep(duration*1000);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphore.release();
		}
	}

	public static void main(String args[]) {

		PrintQueue printQueue = new PrintQueue();

		Thread thread[] = new Thread[10];
		for (int i = 0; i < 10; i++) {
			thread[i] = new Thread(new Job(printQueue), "Thread" + i);
		}

		// 18. 最后，开始这10个线程们。
		for (int i = 0; i < 10; i++) {
			thread[i].start();
		}
	}

	static class Job implements Runnable {

		private PrintQueue printQueue;

		public Job(PrintQueue printQueue) {
			this.printQueue = printQueue;
		}

		@Override
		public void run() {

			System.out.printf("%s: Going to print a job\n", Thread
					.currentThread().getName());

			printQueue.printJob(new Object());

			System.out.printf("%s: The document has been printed\n", Thread
					.currentThread().getName());
		}
	}

}