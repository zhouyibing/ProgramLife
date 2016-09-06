package zhou.concurrent.transferqueue;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by Administrator on 2016/9/6.
 */
public class SynchronousQueueTest {
    static Queue queue;

    @BeforeClass
    public static void init(){
        queue = new SynchronousQueue();
    }

    @Test
    public void test(){
        queue.offer("a");
        System.out.println(queue.poll());
    }
}
