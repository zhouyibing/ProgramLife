package zhou.concurrent.volatiletest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zhou Yibing on 2016/1/26.
 * ����volatile��ʵ��һ��lock,��̫�ɿ�
 */
public class VolatileLock {
    private static final Lock lock = new Lock();
   public static void main(String[] args){
       ExecutorService executorService = Executors.newFixedThreadPool(10);
       for(int i=0;i<10;i++) {
           executorService.submit(new Runnable() {
               @Override
               public void run() {
                   try {
                       lock.lock();
                       System.out.println(Thread.currentThread().getName() + " is working...");
                       Thread.sleep(1000);
                   } catch (InterruptedException e){
                       e.printStackTrace();
                   }finally {
                       lock.unlock();
                   }
               }
           });
       }
       try {
           executorService.shutdown();
           executorService.awaitTermination(1, TimeUnit.MINUTES);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
   }


    public static class Lock{
        volatile  boolean flag=false;
        public void lock(){
            System.out.println(Thread.currentThread().getName() + " wait to get the lock.");
            while (flag){
            }
            if(!flag) {
                System.out.println(Thread.currentThread().getName() + " has get the lock.");
                flag = true;
            }
        }

        public void unlock(){
            System.out.println(Thread.currentThread().getName()+" has release the lock.");
            flag = false;
        }
    }
}
