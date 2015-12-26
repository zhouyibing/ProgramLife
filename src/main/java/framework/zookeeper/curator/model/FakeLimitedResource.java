package framework.zookeeper.curator.model;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class FakeLimitedResource {
    private final AtomicBoolean inUse = new AtomicBoolean(false);
    private String resource;

    public String getResource(){return this.resource;}

    public void setResource(String resouce) throws InterruptedException {
        // 真实环境中我们会在这里访问/维护一个共享的资源
        //这个例子在使用锁的情况下不会非法并发异常IllegalStateException
        //但是在无锁的情况由于sleep了一段时间，很容易抛出异常
        if (!inUse.compareAndSet(false, true)) {
            throw new IllegalStateException("Needs to be used by one client at a time");
        }
        try {
            this.resource=resouce;
            Random r = new Random();
            long delay = r.nextInt(1000);
            Thread.sleep((long) (delay));
        } finally {
            inUse.set(false);
        }
    }
}