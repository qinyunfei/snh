package kafka;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Before;
import org.junit.Test;

public class ProducerDemo {

	// 消息生产者
	public Producer<String, String> producer;

	@Before
	public void name() {

		Properties props = new Properties();
		// bootstrap.servers是Kafka集群的IP地址，如果Broker数量超过1个，则使用逗号分隔
		props.put("bootstrap.servers", "service1:9092,service2:9092,service3:9092");
		// 序列化类型
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		// 获取消息生产者
		producer = new KafkaProducer<>(props);

	}

	// 生产者生产消息
	@Test
	public void name1() {
		for (int i = 0; i < 10; i++) {
			String msg = "Message " + i;
			// 发布消息到主题
			producer.send(new ProducerRecord<String, String>("my-replicated-topic","key"+i,msg));
			System.out.println("发送:" + msg);
		}
		producer.close();
	}

	// 消费者消费消息
	@Test
	public void name2() {
		Properties properties = new Properties();
		// bootstrap.servers是Kafka集群的IP地址，如果Broker数量超过1个，则使用逗号分隔
		properties.put("bootstrap.servers", "service1:9092,service2:9092,service3:9092");
		//消费者分组
		properties.put("group.id", "group-1");
		properties.put("enable.auto.commit", "true");
		properties.put("auto.commit.interval.ms", "1000");
		properties.put("auto.offset.reset", "earliest");
		properties.put("session.timeout.ms", "30000");
		
		//和消息生产者保持一致
		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
		// 设置订阅的主题
		kafkaConsumer.subscribe(Arrays.asList("my-replicated-topic"));
		while (true) {
			//设置超时时间
			//Consumer调用poll方法来轮循Kafka集群的消息，其中的参数100是超时时间（Consumer等待直到Kafka集群中没有消息为止）： 
			ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
			for (ConsumerRecord<String, String> record : records) {
				System.out.println("偏移量 = "+record.offset()+"  key ="+record.key()+" value = "+record.value());
			}
		}

	}
}
