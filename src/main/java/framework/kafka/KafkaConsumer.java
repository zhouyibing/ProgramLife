package framework.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.apache.tools.ant.Executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * KafkaConsumer
 *
 * @author yibing zhou
 * @date 2016/3/23
 */
public class KafkaConsumer extends Thread{
    private String topic;

    public KafkaConsumer(String topic){
        super();
        this.topic = topic;
    }


    @Override
    public void run() {
        ConsumerConnector consumer = createConsumer();
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, 5); // 一次从主题中获取一个数据
        Map<String, List<KafkaStream<byte[], byte[]>>>  messageStreams = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = messageStreams.get(topic);// 获取每次接收到的这个数据

        ScheduledThreadPoolExecutor executorService = new ScheduledThreadPoolExecutor(10);
        for(final KafkaStream stream:streams) {
            System.out.println(stream);
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
                    String topic;
                    String msg;
                    long offset;
                    int partition;
                    MessageAndMetadata<byte[],byte[]> messageAndMetadata;
                    while (iterator.hasNext()) {
                        messageAndMetadata = iterator.next();
                        msg = new String(messageAndMetadata.message());
                        topic = messageAndMetadata.topic();
                        offset = messageAndMetadata.offset();
                        partition = messageAndMetadata.partition();
                        System.out.println(Thread.currentThread().getName()+"接收到来自partition-"+partition+"的第"+offset+"消息:["+topic+"]"+msg
                                +",productArity:"+messageAndMetadata.productArity()+" productPrefix:"+ messageAndMetadata.productPrefix());
                    }
                }
            });
        }
        while(!executorService.isTerminated()){
            System.out.print("activeCount:"+executorService.getActiveCount()+" taskCount:"+executorService.getTaskCount()+
            " completedTaskCount:"+executorService.getCompletedTaskCount()+" coreSize:"+executorService.getCorePoolSize()+
                    " poolSize:"+executorService.getPoolSize()+" maxPoolSize:"+executorService.getMaximumPoolSize());
            System.out.println();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private ConsumerConnector createConsumer() {
        Properties properties = new Properties();
        properties.put("zookeeper.connect", "121.40.137.197:2181,121.40.83.98:2181,121.40.89.193:2181");//声明zk
        properties.put("group.id", "group1");// 必须要使用别的组名称， 如果生产者和消费者都在同一组，则不能访问同一组内的topic数据
        properties.put("zookeeper.sync.time.ms","1000");
        properties.put("zookeeper.session.timeout.ms","40000");
        properties.put("auto.commit.interval.ms","1000");
        return Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));
    }


    public static void main(String[] args) {
        new KafkaConsumer("test2").start();// 使用kafka集群中创建好的主题 test

    }
}
