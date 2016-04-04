package framework.kafka;

import framework.quartz.util.UUID;
import kafka.api.*;
import kafka.api.FetchRequest;
import kafka.api.OffsetRequest;
import kafka.cluster.BrokerEndPoint;
import kafka.common.ErrorMapping;
import kafka.common.TopicAndPartition;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.TopicMetadataResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * Created by zhou on 4/4/16.
 *
 一个消息读取多次
 在一个处理过程中只消费Partition其中的一部分消息
 添加事务管理机制以保证消息被处理且仅被处理一次

 使用SimpleConsumer有哪些弊端呢？

 必须在程序中跟踪offset值
 必须找出指定Topic Partition中的lead broker
 必须处理broker的变动

 使用SimpleConsumer的步骤

 从所有活跃的broker中找出哪个是指定Topic Partition中的leader broker
 找出指定Topic Partition中的所有备份broker
 构造请求
 发送请求查询数据
 处理leader broker变更

 */
public class KafkaSImpleConsumer {

    //broker地址
    private List<ServerInfo> brokers = new ArrayList<>();
    //消费者对应的分区分副本地址
    private List<ServerInfo> replicaBrokers = new ArrayList<>();
    //消费者对应分区的leader
    private ServerInfo leader;
    //消费的主题
    private String topic;
    //分区id
    private int partition;

    private String clientId;

    public KafkaSImpleConsumer(String brokerList,String topic,int partition) {
        this.brokers = parseBroker(brokerList);
        this.clientId = getDefaultClientId();
        this.topic = topic;
        this.partition = partition;
    }

    public KafkaSImpleConsumer(List<ServerInfo> brokers,String topic,int partition) {
        this.brokers = brokers;
        this.clientId = getDefaultClientId();
        this.topic = topic;
        this.partition = partition;
    }

    public List<ServerInfo> parseBroker(String brokerList){
        String[] brokers = brokerList.split(",");
        String[] serverPort;
        ServerInfo serverInfo = null;
        List<ServerInfo> serverInfos = new ArrayList<>();
        for(String broker:brokers){
            serverPort = broker.split(":");
            if(serverPort.length!=2)
                throw new IllegalArgumentException("the broker must give as host:port");
            try {
                serverInfo = new ServerInfo(serverPort[0], Integer.parseInt(serverPort[1]));
            }catch (Exception e){
                throw new IllegalArgumentException("the broker's host or port is invalid,the port is a number");
            }
            if(null!=serverInfo) serverInfos.add(serverInfo);
        }
        return serverInfos;
    }

    private String getDefaultClientId() {
        return UUID.uuid();
    }

    public KafkaSImpleConsumer(String clientId, List<ServerInfo> brokers,String topic,int partition) {
        this.clientId = clientId;
        this.brokers = brokers;
        this.topic = topic;
        this.partition = partition;
    }

    public static void main(String[] args){
        KafkaSImpleConsumer kafkaSImpleConsumer =
                new KafkaSImpleConsumer("121.40.137.197:9092,121.40.83.98:9092,121.40.89.193:9092","test2",0);
        try {
            kafkaSImpleConsumer.run(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run(long maxReads) throws Exception {
        //1.先找到leader
        findLeader();
        if(leader == null)
            System.out.println("Can't find lead in speical topic and partition");
        //2.构造请求
        SimpleConsumer consumer = new SimpleConsumer(leader.getHost(),leader.getPort(),
        10000,50*1024,clientId);
        //2.１获得最近的offset
        //long readOffset = 3960;
        long readOffset = getLastOffset(consumer, kafka.api.OffsetRequest.EarliestTime());
        int numErrors = 0;
        while(maxReads>0) {
            if (consumer == null) {
                consumer = new SimpleConsumer(leader.getHost(), leader.getPort(),
                        10000, 50 * 1024, clientId);
            }
            //2.2设置请求参数
            FetchRequest req = new FetchRequestBuilder().clientId(clientId)
                    .addFetch(topic, partition, readOffset, 100).build();
            kafka.javaapi.FetchResponse fetchResponse = consumer.fetch(req);
            if (fetchResponse.hasError()) {
                numErrors++;
                short code = fetchResponse.errorCode(topic, partition);
                System.out.println("Error fetching data from the Broker:" + leader + " Reason: " + code);
                if (numErrors > 5)
                    break;
                if (code == ErrorMapping.OffsetOutOfRangeCode()) {
                    // We asked for an invalid offset. For simple case ask for
                    // the last element to reset
                    readOffset = getLastOffset(consumer, kafka.api.OffsetRequest.LatestTime());
                    continue;
                }
                consumer.close();
                consumer = null;
                //重新获取leader,可能出错是由于leader节点挂了
                findLeader();
                continue;
            }

            //2.3处理获得的数据
            numErrors = 0;
            long numRead = 0;
            for (MessageAndOffset messageAndOffset : fetchResponse.messageSet(topic, partition)) {
                long currentOffset = messageAndOffset.offset();
                if (currentOffset < readOffset) {
                    System.out.println("Found an old offset: " + currentOffset + " Expecting: " + readOffset);
                    continue;
                }

                readOffset = messageAndOffset.nextOffset();
                ByteBuffer payload = messageAndOffset.message().payload();

                byte[] bytes = new byte[payload.limit()];
                payload.get(bytes);
                System.out.println(String.valueOf(messageAndOffset.offset()) + ": " + new String(bytes, "UTF-8"));
                numRead++;
                maxReads--;
            }

            if (numRead == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        }
        if (consumer != null)
            consumer.close();
    }

    public long getLastOffset(SimpleConsumer consumer,long whichTime){
        System.out.println("get the last offset before "+new Date(whichTime));
        TopicAndPartition topicAndPartition = new TopicAndPartition(topic,partition);
        Map<TopicAndPartition,PartitionOffsetRequestInfo> requestInfoMap = new HashMap<>();
        //解释：从指定的topic的分区获得小于指定time的offset,maxOffsetNum=1
        requestInfoMap.put(topicAndPartition,new PartitionOffsetRequestInfo(whichTime,1));
        kafka.javaapi.OffsetRequest offsetRequest = new kafka.javaapi.OffsetRequest(requestInfoMap, kafka.api.OffsetRequest.CurrentVersion(),clientId);
        OffsetResponse offsetResponse = consumer.getOffsetsBefore(offsetRequest);//阻塞响应
        if(offsetResponse.hasError()){
            System.out.println("Error fetching data Offset Data the Broker. Reason: " + offsetResponse.errorCode(topic, partition));
            return 0;
        }

        long[] offsets = offsetResponse.offsets(topic,partition);
        return offsets[0];
    }

    public void findLeader(){
        SimpleConsumer consumer = null;
        PartitionMetadata partitionMetadata = null;
        loop:for(ServerInfo serverInfo:brokers) {
            try {
                consumer = new SimpleConsumer(serverInfo.getHost(), serverInfo.getPort(), 10000, 1024 * 50, clientId);
                TopicMetadataRequest topicMetadataRequest = new TopicMetadataRequest(Collections.singletonList(topic));
                TopicMetadataResponse topicMetadataResponse = consumer.send(topicMetadataRequest);
                List<TopicMetadata> metaData = topicMetadataResponse.topicsMetadata();
                for (TopicMetadata item : metaData) {
                    for (PartitionMetadata part : item.partitionsMetadata()) {
                        if (part.partitionId() == partition) {
                            partitionMetadata = part;
                            break loop;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error communicating with Broker [" + serverInfo + "] to find Leader for [" + topic + ", " + partition + "] Reason: " + e);
            } finally {
                if (null != consumer)
                    consumer.close();
            }
        }
            //找到分区的leader后更新副本集信息
            if(null!=partitionMetadata){
                BrokerEndPoint l = partitionMetadata.leader();
                leader = new ServerInfo(l.host(),l.port());
                replicaBrokers.clear();//清空副本信息
                ServerInfo replica;
                for(BrokerEndPoint broker:partitionMetadata.replicas()){
                    replica = new ServerInfo(broker.host(),broker.port());
                    replicaBrokers.add(replica);
                }
            }
    }

    class ServerInfo{
        private String host;
        private int port;

        public ServerInfo(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        @Override
        public String toString() {
            return "ServerInfo{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    '}';
        }
    }

}
