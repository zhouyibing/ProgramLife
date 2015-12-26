package framework.zookeeper.curator;

import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zhou Yibing on 2015/11/5.
 */
public class DistributeAtomicLongTest {

    public static void main(String[] args){
        int QTY=10;
        final String PATH="/curator/distributeAtomicLong";
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.80.138:2181,192.168.80.138:2182,192.168.80.138:2183", new ExponentialBackoffRetry(1000, 3));
        client.start();

        List<DistributedAtomicLong> longList = Lists.newArrayList();
        final Random rand = new Random();
        SharedCountTest countTest = new SharedCountTest();
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(QTY);
            for(int i=0;i< QTY;i++){
                final DistributedAtomicLong count = new DistributedAtomicLong(client,PATH,new RetryNTimes(10,10));
                longList.add(count);
                executorService.submit(new Callable<Void>() {

                    @Override
                    public Void call() throws Exception{
                        //Thread.sleep(rand.nextInt(1000));
                        AtomicValue<Long> value = count.increment();
                        //AtomicValue<Long> value = count.decrement();
                        //AtomicValue<Long> value = count.add((long)rand.nextInt(20));
                        System.out.println("succeed: " + value.succeeded());
                        if (value.succeeded())
                            System.out.println("Increment: from " + value.preValue() + " to " + value.postValue());
                        return null;
                    }
                });
            }

            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.MINUTES);
            CloseableUtils.closeQuietly(client);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
