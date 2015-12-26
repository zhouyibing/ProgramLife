package framework.zookeeper.curator;

import com.google.common.collect.Lists;
import framework.zookeeper.curator.model.SuperCuratorTest;
import framework.zookeeper.curator.model.Work;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.framework.recipes.shared.SharedCountListener;
import org.apache.curator.framework.recipes.shared.SharedCountReader;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by Zhou Yibing on 2015/11/5.
 */
public class SharedCountTest implements SharedCountListener{


    public static void main(String[] args){

        int QTY=10;
       final String PATH="/curator/sharedCount";
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.80.138:2181,192.168.80.138:2182,192.168.80.138:2183", new ExponentialBackoffRetry(1000, 3));
        client.start();
        SharedCount baseCount = new SharedCount(client,PATH,0);
        final List<SharedCount> sharedCountList = Lists.newArrayList();
       final Random rand = new Random();
        SharedCountTest countTest = new SharedCountTest();
        try {
            baseCount.addListener(countTest);
            baseCount.start();
            ExecutorService executorService = Executors.newFixedThreadPool(QTY);
            for(int i=0;i< QTY;i++){
                final SharedCount count = new SharedCount(client,PATH,0);
                sharedCountList.add(count);
                executorService.submit(new Callable<Void>() {

                    @Override
                    public Void call() throws Exception{
                        count.start();
                        //Thread.sleep(rand.nextInt(10000));
                        int newValue = count.getCount()+rand.nextInt(10);
                        boolean setResult = count.trySetCount(count.getVersionedValue(), newValue);
                        System.out.println("["+Thread.currentThread().getName()+"] set a newVale["+newValue+"] to Node["+PATH+",version:"+count.getVersionedValue().getVersion()+"]:"+setResult);
                        if(!setResult)
                            count.setCount(newValue);//如果尝试设置计数器失败，则强制设置计数器（内部实现是不断循环重试设置）
                        return null;
                    }
                });
            }

            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.MINUTES);
            for(int i=0;i<QTY;i++){
                sharedCountList.get(i).close();
            }
            baseCount.close();
            CloseableUtils.closeQuietly(client);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void countHasChanged(SharedCountReader sharedCountReader, int i) throws Exception {
        System.out.println("Counter's value[currentVersion:"+sharedCountReader.getVersionedValue().getVersion()+",currentVale:"+sharedCountReader.getVersionedValue().getValue()+"] is change" );
    }

    @Override
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {

        System.out.println("State changed:"+connectionState.toString());
    }
}
