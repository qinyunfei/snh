package kafka.test;

import java.io.IOException;

import org.junit.Test;

/**
 * Author : RandySun (sunfeng152157@sina.com) Date : 2017-08-20 14:18 Comment :
 */
public class ConsumerGroupMain {
	// 消费者分组
	public String groupId = "group01";
	// 主题
	public String topic = "test2";

	// 启动生产者
	@Test
	public void name1() throws IOException {
		Thread producerThread = new Thread(new ProducerThread(topic));
		producerThread.start();

		System.in.read();
	}

	// 启动消费者
	@Test
	public void name2() throws IOException {
		// 消费者数量
		int consumerNumber = 3;
		ConsumerGroup consumerGroup = new ConsumerGroup(groupId, topic, consumerNumber);
		consumerGroup.start();
		
		System.in.read();

	}
}
