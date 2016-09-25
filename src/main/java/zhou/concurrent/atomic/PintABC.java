package zhou.concurrent.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Zhou Yibing on 2016/1/22.
 */
public class PintABC {
    @Test
    public void test2() throws InterruptedException{
        Thread a = new Thread(new PrintRunnable2("A",3,2));
        Thread b = new Thread(new PrintRunnable2("B",2,1));
        Thread c = new Thread(new PrintRunnable2("C",1,3));
        a.start();
        b.start();
        c.start();
        a.join();
        b.join();
        c.join();
    }

    static class PrintRunnable2 implements Runnable{
        private String str;
        private static AtomicInteger runFlag = new AtomicInteger(3);
        private int mySequence;
        private int next;

        private PrintRunnable2(String str,int mySequence,int next) {
            this.str = str;
            this.mySequence = mySequence;
            this.next=next;
        }
        @Override
        public void run() {
            while(true){
                if(runFlag .get()== mySequence)
                    print();
            }
        }

        public void print(){
            System.out.print(str);
            if(next>mySequence)
                System.out.println();

//cas������һ��ִ�����
            runFlag.compareAndSet(mySequence, next);
        }
    }
}
