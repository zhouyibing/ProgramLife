package framework.zookeeper.curator;

import framework.zookeeper.ZookeeperOperator;
import framework.zookeeper.curator.model.FakeLimitedResource;
import framework.zookeeper.curator.model.SuperCuratorTest;
import framework.zookeeper.curator.model.Work;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zhou Yibing on 2015/11/2.
 * 5个客户端并发请求一个锁。测试zookeeper的分布式锁服务及Curator实现分布式锁的原理
 */
public class DistributeLockWithCurator  extends SuperCuratorTest {

    public DistributeLockWithCurator(ExecutorService executorService, CuratorFramework curatorFramework, CountDownLatch countDownLatch) {
        super(executorService, curatorFramework, countDownLatch);
    }

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(100);
        String connectStr = "192.168.80.138:2181,192.168.80.138:2182,192.168.80.138:2183";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(connectStr).retryPolicy(retryPolicy).build();
        ExecutorService executorService = Executors.newCachedThreadPool();
        DistributeLockWithCurator lockWithCurator = new DistributeLockWithCurator(executorService,client,latch);
        try {
            lockWithCurator.start(MyLock.class,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public class MyLock extends Work{

        public MyLock(CountDownLatch countDownLatch, String name, CuratorFramework curatorFramework) {
            super(countDownLatch, name, curatorFramework);
        }

        @Override
        public void doWork() throws Exception {
            InterProcessMutex interProcessMutex = new InterProcessMutex(this.getCuratorFramework(),"/distributeLock");//InterProcessMutex是可重入锁，InterProcessSemphoreMutex是不可重入锁
            try {
                if(interProcessMutex.acquire(5, TimeUnit.SECONDS)){
                    try {
                        log.info(this.getName()+" has acquire the lock.");
                        log.info(this.getName()+" has do its work.");
                        log.info(this.getName()+" has finish its work.");
                        getCountDownLatch().countDown();
                    }catch (Exception e){}
                    finally {
                        //release lock
                        interProcessMutex.release();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
