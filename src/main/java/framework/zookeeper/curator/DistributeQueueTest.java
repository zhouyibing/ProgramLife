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
     * QueueSerializer提供了对队列中的对象的序列化和反序列化。
     QueueConsumer是消费者， 它可以接收队列的数据。 处理队列中的数据的代码逻辑可以放在QueueConsumer.consumeMessage()中。
     正常情况下先将消息从队列中移除，再交给消费者消费。 但这是两个步骤，不是原子的。 可以调用Builder的lockPath()消费者加锁， 当消费者消费数据时持有锁，这样其它消费者不能消费此消息。 如果消费失败或者进程死掉，消息可以交给其它进程。这会带来一点性能的损失。 最好还是单消费者模式使用队列。

     DistributeQueue的处理过程，首先往指定的path节点下创建path/queue-类型的临时节点并设置值，然后监听path下子节点（ChildrenCache）,start时，启动提交消费线程到线程池中，消费线程通过ChildrenCache取出节点信息并删除，启动处理线程并将信息交给Consumer处理（调用consumeMessage）
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
