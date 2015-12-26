//15.现在，实现例子的主类通过创建一个类，名为Core并加入 main() 方法。
package zhou.base.thread.exchanger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;
/**
 * Exchanger 类允许在2个线程间定义同步点，当2个线程到达这个点，
 * 他们相互交换数据类型，使用第一个线程的数据类型变成第二个的，然后第二个线程的数据类型变成第一个的。
 * 第一个调用 exchange()方法会进入休眠直到其他线程的达到。
 *
 ***
 */
public class Core {
	public static void main(String[] args) {

		// 16. 创建2个buffers。分别给producer和consumer使用.
		List<String> buffer1 = new ArrayList<String>();
		List<String> buffer2 = new ArrayList<String>();

		// 17. 创建Exchanger对象，用来同步producer和consumer。
		Exchanger<List<String>> exchanger = new Exchanger<List<String>>();

		// 18. 创建Producer对象和Consumer对象。
		Producer producer = new Producer(buffer1, exchanger);
		Consumer consumer = new Consumer(buffer2, exchanger);

		// 19. 创建线程来执行producer和consumer并开始线程。
		Thread threadProducer = new Thread(producer);
		Thread threadConsumer = new Thread(consumer);
		threadProducer.start();
		threadConsumer.start();
	}
}
