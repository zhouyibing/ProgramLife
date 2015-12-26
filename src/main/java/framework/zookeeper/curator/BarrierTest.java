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

    //���߳�ͨ�������ڵ��Ƿ�������ж��Ƿ��ֹͣ�ȴ���barrierͨ�����ƽڵ�Ĵ�����ʵ��դ��
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

    //Ϊÿ���ͻ����߳���ָ��path�´�����ʱ�ڵ㣨�������̵߳������趨��memberQty�Ľڵ㶼�����ú󣩣����г�Ա�������Ϊ�̴߳���һ��readyPath�ڵ㡣---enter�߼�
    //�ͻ�׼���뿪ʱ���Ƚ��Լ���ص���ʱ�ڵ��readyPath�ڵ�ɾ����Ȼ��ȴ������ͻ���ȫ���˳���--leave�߼�
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
