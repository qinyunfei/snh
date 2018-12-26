package kafka.test2;

import java.io.IOException;

import org.junit.Test;

import kafka.test.ConsumerGroup;
import kafka.test.ProducerThread;

/**
 * Author : RandySun (sunfeng152157@sina.com) Date : 2017-08-20 16:49 Comment :
 */
public class ConsumerThreadMain {

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
		ConsumerThread consumerThread = new ConsumerThread(groupId, topic);
		
		consumerThread.start(3);
		
		System.in.read();
	}
}