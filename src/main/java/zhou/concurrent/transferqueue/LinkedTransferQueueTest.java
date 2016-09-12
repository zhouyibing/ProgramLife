package zhou.concurrent.transferqueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

/**
 * Created by yibingzhou on 2016/8/24.
 */
public class LinkedTransferQueueTest {

    public static void main(String[] args) throws InterruptedException {
        final TransferQueue<String> transferQueue = new LinkedTransferQueue();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(int i=0;i<10000;i++){
            final int finalI = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        transferQueue.transfer(finalI+"");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(finalI);
                }
            });
        }
        int total=0;
        while (total!=10000){
            System.out.println(transferQueue.take());
            total++;
        }
        System.out.println("total:"+total);
        executorService.shutdownNow();
    }
}
