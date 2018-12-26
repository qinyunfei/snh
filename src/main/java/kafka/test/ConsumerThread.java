package kafka.test;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * Author  : RandySun (sunfeng152157@sina.com)
 * Date    : 2017-08-20  12:03
 * Comment :
 */
public class ConsumerThread implements Runnable {
	
	//KafkaConsumer是非线程安全的类，当使用多个线程操作同一个KafkaConsumer对象时就会引起ConcurrentModificationException这个错误
	//线程与KafkaConsumer对象的对应关系是1：1
    private KafkaConsumer<String,String> kafkaConsumer;
    
    private final String topic;

    public ConsumerThread(String groupId,String topic){
        Properties properties = buildKafkaProperty(groupId);
        //设置主题
        this.topic = topic;
        //获取消费者
        this.kafkaConsumer = new KafkaConsumer<String, String>(properties);
        //设置订阅主题
        this.kafkaConsumer.subscribe(Arrays.asList(this.topic));
    }

    //初始化Properties对象
    private static Properties buildKafkaProperty(String groupId){
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "service1:9092,service2:9092,service3:9092");
        //设置消费者分组
        properties.put("group.id", groupId);
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("session.timeout.ms", "30000");
        properties.put("auto.offset.reset", "earliest");
        
        //key-value 序列化
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        
        return properties;
    }

    @Override
    public void run() {
        while (true){
        		//轮循Kafka集群的消息，其中的参数100是超时时间（Consumer等待直到Kafka集群中没有消息为止）：
            ConsumerRecords<String,String> consumerRecords = kafkaConsumer.poll(100);
            for(ConsumerRecord<String,String> item : consumerRecords){
                System.out.println("消费者"+Thread.currentThread().getName()+"收到消息:"+item.value()+",分区:"+item.partition()+"偏移量:"+item.offset());
            }
        }
    }
}
