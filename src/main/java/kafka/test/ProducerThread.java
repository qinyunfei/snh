package kafka.test;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class ProducerThread implements Runnable {

	// 消息生产者
	private final Producer<String, String> kafkaProducer;
	// 主题
	private final String topic;

	public ProducerThread(String topic) {
		// 获取Properties配置对象
		Properties properties = buildKafkaProperty();
		// 主题
		this.topic = topic;
		// 消息生产者
		this.kafkaProducer = new KafkaProducer<String, String>(properties);
	}

	// 创建Properties配置对象
	private static Properties buildKafkaProperty() {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", "service1:9092,service2:9092,service3:9092");
		
		
		properties.put("acks", "all");
		properties.put("retries", 0);
		properties.put("batch.size", 16384);
		properties.put("linger.ms", 1);
		properties.put("buffer.memory", 33554432);
		
		//序列化
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		return properties;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("开始发送消息到kafka");
		int i = 0;
		while (true) {
			//消息内容
			String sendMsg = "message :" + String.valueOf(++i);
			//发送消息到指定主题 指定回调函数可以获取消息被发送到那个分区已经偏移量是多少
			kafkaProducer.send(new ProducerRecord<String, String>(topic, sendMsg), new Callback() {

				@Override
				public void onCompletion(RecordMetadata recordMetadata, Exception e) {
					if (e != null) {
						e.printStackTrace();
					}
					System.out.println("消息"+sendMsg+"发送到: 分区:"+ recordMetadata.partition() + ",偏移量:"
							+ recordMetadata.offset());
				}
			});
			
			// thread sleep 3 seconds every time
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
