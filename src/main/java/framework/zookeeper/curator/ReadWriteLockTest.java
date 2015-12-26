package framework.zookeeper.curator;

import framework.zookeeper.curator.model.FakeLimitedResource;
import framework.zookeeper.curator.model.SuperCuratorTest;
import framework.zookeeper.curator.model.Work;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zhou Yibing on 2015/11/3.
 */
public class ReadWriteLockTest extends SuperCuratorTest {

    public ReadWriteLockTest(ExecutorService executorService, CuratorFramework curatorFramework, CountDownLatch countDownLatch) {
        super(executorService, curatorFramework, countDownLatch);
    }

    public static void main(String[] args){
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(100);
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.80.138:2181,192.168.80.138:2182,192.168.80.138:2183")
                .retryPolicy(new RetryNTimes(Integer.MAX_VALUE,1000)).connectionTimeoutMs(5000).build();
        ReadWriteLockTest readWriteWork = new ReadWriteLockTest(executorService,client,latch);
        try {
            readWriteWork.start(ReadWriteWork.class,new Object[]{"/curator/readWriteLock",new FakeLimitedResource()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public class ReadWriteWork extends Work {
        private InterProcessReadWriteLock readWriteLock;
        private InterProcessMutex readLock;
        private InterProcessMutex writeLock;
        private String lockPath;
        private FakeLimitedResource resource;

        public ReadWriteWork(CountDownLatch countDownLatch, String name, CuratorFramework curatorFramework,String lockPath,FakeLimitedResource resource) {
            super(countDownLatch, name, curatorFramework);
            this.readWriteLock = new InterProcessReadWriteLock(curatorFramework,lockPath);
            this.readLock = readWriteLock.readLock();
            this.writeLock = readWriteLock.writeLock();
            this.resource = resource;
        }

        public void doWork() throws Exception {
           //1.先检查是否可写，如果可写就修改对象，否则读取对象
            try {
                if (writeLock.acquire(3, TimeUnit.SECONDS)) {
                    log.info(this.getName() + " has get the wirteLock");
                    resource.setResource(this.getName());
                }
                if (readLock.acquire(3, TimeUnit.SECONDS)) {
                    log.info(this.getName() + " has get the readLock");
                    log.info(this.getName() + ":" + resource.getResource());
                }
            }finally {
                if(writeLock.isAcquiredInThisProcess()){
                    log.info(this.getName() + " has release the writeLock");
                    writeLock.release();
                }
                if(readLock.isAcquiredInThisProcess()){
                    log.info(this.getName() + " has release the readlock");
                    readLock.release();
                }
                this.getCountDownLatch().countDown();
            }
        }
    }
}
