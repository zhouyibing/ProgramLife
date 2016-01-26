package zhou.concurrent;

import java.lang.reflect.Field;

/**
 * Created by Zhou Yibing on 2016/1/21.
 */
public class UsafeTest {

    public static void main(String[] args){
       final test test = new test(1,2);
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"cas result:"+test.casA(1,2));
                System.out.println(Thread.currentThread().getName()+" a:"+test.getA());
            }
        });
        Thread b = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+"cas result:"+test.casA(1,3));
                System.out.println(Thread.currentThread().getName()+" a:"+test.getA());
            }
        });
        a.start();
        b.start();
    }

    static final class test{
        private int a;
        private int b;

        public test(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }

        private int casA(int expect,int n){
            if(UNSAFE.compareAndSwapInt(this, aOffset, expect, n))
                return n;
            return -1;
        }

        private int casB(int expect,int n){
            if(UNSAFE.compareAndSwapInt(this, bOffset,expect,n))
                return n;
            return -1;
        }

        private static final sun.misc.Unsafe UNSAFE;
        private static final long aOffset;
        private static final long bOffset;
        static{
            try {
                Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                UNSAFE = (sun.misc.Unsafe) f.get(null);
                //UNSAFE = sun.misc.Unsafe.getUnsafe();//直接这样获取unsafe，jvm是不允许的。认为是不安全的，跑出securityException
                Class clazz = test.class;
                aOffset = UNSAFE.objectFieldOffset(clazz.getDeclaredField("a"));
                bOffset = UNSAFE.objectFieldOffset(clazz.getDeclaredField("b"));

            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }
}
