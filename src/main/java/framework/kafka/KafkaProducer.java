package framework.kafka;

import kafka.cluster.Partition;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.Partitioner;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;
import scala.Int;

import java.util.Properties;

/**
 * KafkaProducer
 *
 * @author yibing zhou
 * @date 2016/3/23
 */
public class KafkaProducer extends Thread{
    private String topic;

    public KafkaProducer(String name, String topic) {
        super(name);
        this.topic = topic;
    }

    @Override
    public void run() {
        Producer<Integer, String> producer = createProducer();
        int i = 0;
        while (true){
            producer.send(new KeyedMessage(topic,"message:"+i++));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Producer createProducer() {
        Properties properties = new Properties();
        properties.put("zookeeper.connect","121.40.137.197:2181,121.40.83.98:2181,121.40.89.193:2181");
        properties.put("serializer.class", StringEncoder.class.getName());
        properties.put("partitioner.class","framework.kafka.SimplePartitioner");
        properties.put("metadata.broker.list","121.40.137.197:9092,121.40.83.98:9092,121.40.89.193:9092");
        properties.put("request.required.acks","1");
        ProducerConfig config = new ProducerConfig(properties);
        return new Producer<Integer, String>(config);
    }

    public static void main(String[] args){
        KafkaProducer producer = new KafkaProducer("producer","test2");
        producer.start();
    }
}
