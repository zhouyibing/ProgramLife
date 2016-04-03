package framework.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.tools.ant.Executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        topicCountMap.put(topic, 10); // 一次从主题中获取一个数据
        Map<String, List<KafkaStream<byte[], byte[]>>>  messageStreams = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = messageStreams.get(topic);// 获取每次接收到的这个数据
        for(final KafkaStream stream:streams) {
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
                    while (iterator.hasNext()) {
                        String message = new String(iterator.next().message());
                        System.out.println("接收到: " + message);
                    }
                }
            });
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
