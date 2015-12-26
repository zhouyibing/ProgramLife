package framework.zookeeper.curator;

import framework.zookeeper.curator.model.FakeLimitedResource;
import framework.zookeeper.curator.model.SuperCuratorTest;
import framework.zookeeper.curator.model.Work;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;
import org.apache.curator.retry.RetryNTimes;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zhou Yibing on 2015/11/4.
 */
public class SemaphoreTest extends SuperCuratorTest{
    public SemaphoreTest(ExecutorService executorService, CuratorFramework curatorFramework, CountDownLatch countDownLatch) {
        super(executorService, curatorFramework, countDownLatch);
    }

    public static void main(String[] args){
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(100);
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.80.138:2181,192.168.80.138:2182,192.168.80.138:2183")
                .retryPolicy(new RetryNTimes(Integer.MAX_VALUE,1000)).connectionTimeoutMs(5000).build();
        ReadWriteLockTest readWriteWork = new ReadWriteLockTest(executorService,client,latch);
        try {
            readWriteWork.start(SemaphoreWork.class,new Object[]{"/curator/Semaphore",10});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class SemaphoreWork extends Work{

        private InterProcessSemaphoreV2 mutex;
        private String lockPath;
        private Integer maxLease;
        public SemaphoreWork(CountDownLatch countDownLatch, String name, CuratorFramework curatorFramework,String lockPath,Integer maxLease) {
            super(countDownLatch, name, curatorFramework);
            this.lockPath = lockPath;
            this.maxLease = maxLease;
            this.mutex = new InterProcessSemaphoreV2(curatorFramework,lockPath,maxLease);
        }

        @Override
        public void doWork() throws Exception {
            Lease lease = mutex.acquire();
            if(null==lease){
                log.info(this.getName()+" has not acquire another leases");
            }else{
                log.info(this.getName()+" has acquire another leases");
            }
            Collection<Lease> leases2 = mutex.acquire(5,10, TimeUnit.SECONDS);
            if(null==leases2||leases2.isEmpty()){
                log.info(this.getName()+" has not acquire 5 leases after 10s wait.");
            }else{
                log.info(this.getName()+" has acquire 5 leases again");
            }

            if(null!=lease)
              mutex.returnLease(lease);
            if(null!=leases2)
              mutex.returnAll(leases2);
        }
    }
}
