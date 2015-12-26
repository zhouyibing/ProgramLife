package framework.zookeeper.curator.model;

import org.apache.curator.framework.CuratorFramework;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zhou Yibing on 2015/11/3.
 */
public abstract class Work implements Runnable{
    private CountDownLatch countDownLatch;
    private String name;
    private CuratorFramework curatorFramework;

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CuratorFramework getCuratorFramework() {
        return curatorFramework;
    }

    public void setCuratorFramework(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    public Work(CountDownLatch countDownLatch, String name, CuratorFramework curatorFramework) {
        this.countDownLatch = countDownLatch;
        this.name = name;
        this.curatorFramework = curatorFramework;
    }

    public abstract void doWork() throws Exception;

    @Override
    public void run() {
        try {
            doWork();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
