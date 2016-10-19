package com.zhou.hadoop;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;

/**
 * Created by yibingzhou on 2016/10/12.
 */
public class Temperature {
    private static final Logger logger = LoggerFactory.getLogger(Temperature.class);
    /**
     * 四个泛型类型分别代表：
     * KeyIn        Mapper的输入数据的Key，这里是每行文字的起始位置（0,11,...）
     * ValueIn      Mapper的输入数据的Value，这里是每行文字
     * KeyOut       Mapper的输出数据的Key，这里是每行文字中的“年份”
     * ValueOut     Mapper的输出数据的Value，这里是每行文字中的“气温”
     */
    static class TempMapper implements Mapper<LongWritable, Text, IntWritable, IntWritable> {

        private String mapTaskId;
        private String inputFile;
        private int noRecords;
        static enum MapperCounters { MAPPED_RECORDS }
        @Override
        public void close() throws IOException {
            logger.info("reduceTask-{} final processed records:{}",mapTaskId,noRecords);
        }

        @Override
        public void configure(JobConf jobConf) {
            mapTaskId = jobConf.get(JobContext.TASK_ATTEMPT_ID);
            inputFile = jobConf.get(JobContext.MAP_INPUT_FILE);
        }

        @Override
        public void map(LongWritable key, Text value, OutputCollector<IntWritable, IntWritable> outputCollector, Reporter reporter) throws IOException {
            //logger.info("before Mapper:line num={},value={}",key,value);
            reporter.progress();//tell them mapper is  progressing
            String line = value.toString();
            int year = Integer.valueOf(line.substring(0,4));
            int temp = Integer.valueOf(line.substring(8));
            reporter.incrCounter(MapperCounters.MAPPED_RECORDS,1);
            ++noRecords;
            outputCollector.collect(new IntWritable(year),new IntWritable(temp));
            if ((noRecords%100) == 0) {
                reporter.setStatus(mapTaskId + " processed " + noRecords + " from input-file: " + inputFile);
            }
            //logger.info("after Mapper:key={},value={}",year,temp);
        }
    }

    /**
     * 四个泛型类型分别代表：
     * KeyIn        Reducer的输入数据的Key，这里是每行文字中的“年份”
     * ValueIn      Reducer的输入数据的Value，这里是每行文字中的“气温”
     * KeyOut       Reducer的输出数据的Key，这里是不重复的“年份”
     * ValueOut     Reducer的输出数据的Value，这里是这一年中的“最高气温”
     */
    static class TempReducer implements Reducer<IntWritable,IntWritable,IntWritable,IntWritable>{
        static enum ReducerCounters { REDUCED_RECORDS }
        private String reduceTaskId;
        private int noKeys = 0;

        @Override
        public void reduce(IntWritable key, Iterator<IntWritable> values, OutputCollector<IntWritable, IntWritable> output, Reporter reporter) throws IOException {
            int maxValue =Integer.MIN_VALUE;
            reporter.progress();
            StringBuffer sb = new StringBuffer();
            IntWritable value;
            int count=0;
            for(;values.hasNext();){
                value = values.next();
                maxValue = Math.max(maxValue,value.get());
                sb.append(value).append(",");
                count++;
            }
            //logger.info("before Reduce:value count={}",count);
            reporter.incrCounter(ReducerCounters.REDUCED_RECORDS,1);
            ++noKeys;
            output.collect(key,new IntWritable(maxValue));
            if ((noKeys%100) == 0) {
               reporter.setStatus(reduceTaskId + " processed " + noKeys);
            }
            //logger.info("after Reduce:key={},maxValue={}",key,maxValue);
        }

        @Override
        public void close() throws IOException {
            logger.info("reduceTask-{} final processed records:{}",reduceTaskId,noKeys);
        }

        @Override
        public void configure(JobConf job) {
            reduceTaskId = job.get(JobContext.TASK_ATTEMPT_ID);
        }
    }

    public static void main(String[] args) throws IOException {
        //System.setProperty("hadoop.home.dir","D:\\hadoop-2.7.2");
        //System.setProperty("HADOOP_USER_NAME","jianyugu");
        JobConf conf  = new JobConf();
        conf.set("fs.hdfs.impl",org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl",org.apache.hadoop.fs.LocalFileSystem.class.getName());
        FileInputFormat.addInputPath(conf,new Path("hdfs://10.2.56.199:9000/hdfs/input"));
        Path output = new Path("hdfs://10.2.56.199:9000/hdfs/output");
        FileOutputFormat.setOutputPath(conf,output);
        conf.setMapperClass(TempMapper.class);
        conf.setReducerClass(TempReducer.class);
        conf.setOutputKeyClass(IntWritable.class);
        conf.setOutputValueClass(IntWritable.class);
        //如果需要打成jar运行，需要下面这句
        conf.setJarByClass(Temperature.class);
        //Job job = new Job(conf);
        //运行前先将output目录删除
        FileSystem hdfs = FileSystem.get(URI.create("hdfs://10.2.56.199:9000/"),conf);
        hdfs.delete(output,true);
        JobClient.runJob(conf);
    }
}
