package zhou.concurrent;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TestBlockingQueue {

	public static void main(String[] args) {

		BlockingQueue<Long> blockingQueue = new ArrayBlockingQueue<Long>(10);
		Deque<String> deque = new ArrayDeque<String>();
		deque.offer("a");
		deque.peek();
		for(int i=0;i<10;i++){
			Thread productThread = new ProductThread(blockingQueue);
			Thread consumeThread = new ConsumeThread(blockingQueue);
			productThread.start();
			consumeThread.start();
		}
		System.out.println(blockingQueue.size());
	}

	static class ProductThread extends Thread{

		private BlockingQueue<Long> queue;
		
		private ProductThread(BlockingQueue<Long> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			queue.offer(System.currentTimeMillis());
		}
	}
	
	static class ConsumeThread extends Thread{
        private BlockingQueue<Long> queue;
		
		private ConsumeThread(BlockingQueue<Long> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			try {
				queue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
