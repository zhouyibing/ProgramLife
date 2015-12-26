package zhou.concurrent.collection;

import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
/**
 * Client类使用put()方法将字符串插入到列表中。如果列表已满（列表生成时指定了固定的容量），调用这个方法的线程将被阻塞直到列表中有了可用的空间。
 * Main类使用take()方法从列表中取字符串。如果列表为空，调用这个方法的线程将被阻塞直到列表不为空（即有可用的元素）。
 * 这个例子中使用了LinkedBlockingDeque对象的两个方法，调用它们的线程可能会被阻塞，在阻塞时如果线程被中断，方法会抛出InterruptedException异常，
 * 所以必须捕获和处理这个异常。
 * 
 * takeFirst()和takeLast()：分别返回列表中第一个和最后一个元素，返回的元素会从列表中移除。如果列表为空，调用方法的线程将被阻塞直到列表中有可用的元素出现。
 * getFirst()和getLast()：分别返回列表中第一个和最后一个元素，返回的元素不会从列表中移除。如果列表为空，则抛出NoSuchElementExcpetinon异常。
 * peek()、peekFirst()和peekLast()：分别返回列表中第一个和最后一个元素，返回的元素不会从列表中移除。如果列表为空，返回null。
 * poll()、pollFirst()和pollLast()：分别返回列表中第一个和最后一个元素，返回的元素将会从列表中移除。如果列表为空，返回null。
 * add()、addFirst()和addLast(): 分别将元素添加到列表中第一位和最后一位。如果列表已满（列表生成时指定了固定的容量），这些方法将抛出IllegalStateException异常。
 *
 ***
 */
public class BlockSafeList {

	public static void main(String[] args) {
		LinkedBlockingDeque list = new LinkedBlockingDeque(3);
		Client client = new Client(list);
		Thread thread = new Thread(client);
		thread.start();

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++) {
				try {
					String request = (String) list.take();
					System.out.printf("Main: Request: %s at %s. Size:%d\n",
							request, new Date(), list.size());
					TimeUnit.MILLISECONDS.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.printf("Main: End of the program.\n");
	}

	static class Client implements Runnable {
		private LinkedBlockingDeque requestList;

		public Client(LinkedBlockingDeque requestList) {
			this.requestList = requestList;
		}

		public void run() {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 5; j++) {
					StringBuilder request = new StringBuilder();
					request.append(i);
					request.append(":");
					request.append(j);
					try {
						requestList.put(request.toString());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.printf("Client: %s at %s.\n", request,
							new Date());
				}
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.printf("Client: End.\n");
		}
	}

}
