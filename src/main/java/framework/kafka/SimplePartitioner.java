package framework.kafka;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

//自定义分区
public  class SimplePartitioner implements Partitioner {
    public SimplePartitioner(VerifiableProperties props) {
    }

    @Override
        public int partition(Object o, int i) {
            String key = String.valueOf(o);
            System.out.print(key);
            int end = Integer.valueOf(key.charAt(key.length()-1));
            return end%i;
        }
    }