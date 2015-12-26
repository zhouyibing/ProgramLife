package framework.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

/**
 * Created by Zhou Yibing on 2015/11/5.
 */
public class DistributeQueueTest {

    public static void main(String[] args){
        testDistributeQueue();
    }

    /**
     * QueueSerializer�ṩ�˶Զ����еĶ�������л��ͷ����л���
     QueueConsumer�������ߣ� �����Խ��ն��е����ݡ� ��������е����ݵĴ����߼����Է���QueueConsumer.consumeMessage()�С�
     ����������Ƚ���Ϣ�Ӷ������Ƴ����ٽ������������ѡ� �������������裬����ԭ�ӵġ� ���Ե���Builder��lockPath()�����߼����� ����������������ʱ���������������������߲������Ѵ���Ϣ�� �������ʧ�ܻ��߽�����������Ϣ���Խ����������̡�������һ�����ܵ���ʧ�� ��û��ǵ�������ģʽʹ�ö��С�

     DistributeQueue�Ĵ�����̣�������ָ����path�ڵ��´���path/queue-���͵���ʱ�ڵ㲢����ֵ��Ȼ�����path���ӽڵ㣨ChildrenCache��,startʱ�������ύ�����̵߳��̳߳��У������߳�ͨ��ChildrenCacheȡ���ڵ���Ϣ��ɾ�������������̲߳�����Ϣ����Consumer��������consumeMessage��
     */
    public static void testDistributeQueue(){
        final String path = "/curator/distributeQueue";
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.80.138:2181,192.168.80.138:2182,192.168.80.138:2183",new ExponentialBackoffRetry(1000,3));
        QueueConsumer<String> consumer = createQueueConsumer();
        QueueBuilder<String> builder = QueueBuilder.builder(client,consumer,createQueueSerializer(),path);
        DistributedQueue<String> queue = builder.buildQueue();
        client.start();
        try {
            queue.start();
            for(int i=0;i<100;i++){
                queue.put("message-"+i);
                Thread.sleep(1000);
            }
            Thread.sleep(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            CloseableUtils.closeQuietly(client);
            CloseableUtils.closeQuietly(queue);
        }

    }

    private static QueueSerializer<String> createQueueSerializer() {
        return new QueueSerializer<String>(){

            @Override
            public byte[] serialize(String item) {
                return item.getBytes();
            }

            @Override
            public String deserialize(byte[] bytes) {
                return new String(bytes);
            }

        };
    }

    private static QueueConsumer<String> createQueueConsumer() {
        return new QueueConsumer<String>() {
            @Override
            public void consumeMessage(String s) throws Exception {
                System.out.println("consume one message: " + s);
            }

            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                System.out.println("connection new state: " + connectionState.name());
            }
        };
    }
}
