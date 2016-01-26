package zhou.concurrent.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by Zhou Yibing on 2016/1/20.
 */
public class SynchronousQueueTest {

    public static void main(String[] args){

         final SynchronousQueue synchronousQueue = new SynchronousQueue(true);
        final Object flag = new Object();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName()+":put an object into the queue");
                    synchronousQueue.put(flag);
                    System.out.println(Thread.currentThread().getName()+":the object has consumed.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread a2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                        System.out.println(Thread.currentThread().getName() + ":put an object into the queue");
                    synchronousQueue.put(flag);
                    System.out.println(Thread.currentThread().getName() + ":the object has consumed.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread b = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    for(int i=0;i<2;i++){
                        System.out.println(Thread.currentThread().getName() + ":take an object from the queue");
                        synchronousQueue.take();
                        System.out.println(Thread.currentThread().getName() + ":the object has consumed by me");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        a.start();
        a2.start();
       b.start();
    }

    @Test
    public  void testToArray(){
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");
        Object[] obj = list.toArray();
        String[] arr = new String[8];
        Object[] obj1 = list.toArray(arr);
        list.remove(0);

        System.out.println(list.get(0));
        System.out.println(obj[0]);
        System.out.println(obj1[0]);
        System.out.println(obj1[4]);
        System.out.println(arr[0]);
        System.out.println(arr[7]);

    }

    static class  a{
        private int a ;
    }
}
