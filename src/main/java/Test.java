import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

public class Test {
	final static TransferQueue<String> queue = new LinkedTransferQueue<String>();

	public static void main(String[] args) {
		int consumerCount = -1;
		int threadNum = 5;
		try {
			for (int i = 0; i < threadNum; i++) {
				new Thread() {
					@Override
					public void run() {
						try {
							long start = System.currentTimeMillis();
							String result = queue.take();
							Thread.sleep(1000);
							System.out.println("result : " + result
									+ " use time : "
									+ (System.currentTimeMillis() - start));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}.start();
			}

			Thread.sleep(3000L);
			consumerCount = queue.getWaitingConsumerCount();
			System.out.println("waiting consumer count:" + consumerCount);
			for (int i = 0; i < consumerCount; i++) {
				queue.transfer("hello-0-" + i);
				System.out.println("waiting consumer count after put :"
						+ consumerCount);
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
