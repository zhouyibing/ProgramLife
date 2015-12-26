package framework.btrace;

import java.util.Random;

/**
 * Created by Zhou Yibing on 2015/12/4.
 */
public class BtraceTest {

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        Counter counter = new Counter();
       for(int i=0;i<20;i++){
            counter.add(random.nextInt(10));
            Thread.sleep(1000);
        }
    }
}
