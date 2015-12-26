package framework.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zhou Yibing on 2015/11/5.
 */
public class BarrierTest {

    public static void main(String[] args){

        //test();
        testDoubleBarrier();
    }

    //各线程通过监听节点是否存在来判断是否该停止等待，barrier通过控制节点的存在来实现栅栏
    private static void test(){

        int QTY=10;
        String path="/curator/barrier";
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.80.138:2181,192.168.80.138:2182,192.168.80.138:2183",new ExponentialBackoffRetry(1000,3));
        client.start();
        ExecutorService executorService = Executors.newFixedThreadPool(QTY);
        final DistributedBarrier distributedBarrier = new DistributedBarrier(client,path);
        try {
            distributedBarrier.setBarrier();
            for(int i=0;i<QTY;i++) {
            final int index = i;
            executorService.submit(new Callable<Void>() {
                @Override
                public Void call() throws Exception {

                    Thread.sleep((long) (3 * Math.random()));
                    System.out.println("Client #" + index + " waits on Barrier");
                    distributedBarrier.waitOnBarrier();
                    System.out.println("Client #" + index + " begins");
                    return null;
                }
            });
        }
            Thread.sleep(10000);
            System.out.println("all Barrier instances should wait the condition");

            distributedBarrier.removeBarrier();
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //为每个客户端线程在指定path下创建临时节点（并阻塞线程到所有设定的memberQty的节点都创建好后），所有成员都进入后，为线程创建一个readyPath节点。---enter逻辑
    //客户准备离开时，先将自己相关的临时节点和readyPath节点删除，然后等待其他客户端全部退出。--leave逻辑
    private static void testDoubleBarrier(){
        int QTY=10;
        String path="/curator/doubleBarrier";
        try {
            CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.80.138:2181,192.168.80.138:2182,192.168.80.138:2183", new ExponentialBackoffRetry(1000, 3));
            client.start();

            ExecutorService service = Executors.newFixedThreadPool(QTY);
            for (int i = 0; i < QTY; ++i) {
                final DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, path, QTY);
                final int index = i;
                Callable<Void> task = new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {

                        Thread.sleep((long) (3 * Math.random()));
                        System.out.println("Client #" + index + " enters");
                        barrier.enter();
                        System.out.println("Client #" + index + " begins");
                        Thread.sleep((long) (3000 * Math.random()));
                        barrier.leave();
                        System.out.println("Client #" + index + " left");
                        return null;
                    }
                };
                service.submit(task);
            }


            service.shutdown();
            try {
                service.awaitTermination(10, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }catch(Exception e){e.printStackTrace();}
    }
}
