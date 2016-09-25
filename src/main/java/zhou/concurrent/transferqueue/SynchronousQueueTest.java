package zhou.concurrent.transferqueue;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Queue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/9/6.
 */
public class SynchronousQueueTest {
    static SynchronousQueue queue;

    @BeforeClass
    public static void init(){
        queue = new SynchronousQueue();
    }

    @Test
    public void test(){
        try {
            queue.offer("a",10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().isInterrupted());
    }
}
