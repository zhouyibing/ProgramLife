package framework.zookeeper.curator.model;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class FakeLimitedResource {
    private final AtomicBoolean inUse = new AtomicBoolean(false);
    private String resource;

    public String getResource(){return this.resource;}

    public void setResource(String resouce) throws InterruptedException {
        // ��ʵ���������ǻ����������/ά��һ���������Դ
        //���������ʹ����������²���Ƿ������쳣IllegalStateException
        //�������������������sleep��һ��ʱ�䣬�������׳��쳣
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