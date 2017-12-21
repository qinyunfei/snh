package rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.MessageProperties;

/**
 * 
 * @Description: 六大模式之一 工作队列
 * @author: Qin YunFei
 * @date: 2017年12月16日 上午9:38:18
 * @version V1.0
 */
public class Test2 {

	// 队列的名称
	public final static String QUEUE_NAME = "rabbitMQ.test3";

	// 定义rabbitmq的链接
	private Connection connection = null;

	@Before
	public void name() throws IOException, TimeoutException {
		// 定义连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		// 设置服务地址
		factory.setHost("service1");
		// 端口
		factory.setPort(5672);
		// 设置账号信息，用户名、密码、vhost
		factory.setVirtualHost("/atguigu");
		factory.setUsername("xiaohei");
		factory.setPassword("123456");
		// 获取连接
		connection = factory.newConnection();
	}

	// 生产者
	@Test
	public void name2() throws IOException, TimeoutException {
		// 创建一个通道
		Channel channel = connection.createChannel();
		// 创建一个队列
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);

		// 分发信息
		for (int i = 0; i < 50; i++) {
			String message = "Hello RabbitMQ" + i;
			channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
			System.out.println("NewTask send '" + message + "'");
		}

		channel.close();
		connection.close();
	}

	/**
	 * 
	 * 消费者1 处理能力高
	 * 
	 * @throws IOException
	 */
	@Test
	public void name3() throws IOException {
		// 创建一个通道
		Channel channel = connection.createChannel();

		int prefetchCount = 1;
		/**
		 * 公平调度（能者多劳）
		 * 1这告诉RabbitMQ一次不能给一个工作者大与一条的消息。或者换句话说，不要向消费者发送新消息，直到处理并确认前一个消息的ack。相反，它会将其分派给下一个还不忙的工作人员。
		 * 0告诉RabbitMQ一次可以给一个工作者多个消息 或者换句话说 RabbitMQ一次发多个消息给消费者处理  并不管上一个消息有没有ack
		 */
		channel.basicQos(prefetchCount);

		// 创建一个队列
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		//这里是异步的处理  juit不会等待子线程运行完结 就会结束主线程 所以我们 要避免主程序停止  
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] 消费者1收到了'" + message + "'");
				//模拟处理能力强
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//requeue 值为 true 表示该消息重新放回队列头，值为 false 表示放弃这条消息。
				//channel.basicReject(envelope.getDeliveryTag(), true);
				//multiple：是否批量
				//basicNack和basicReject的区别就是一个支持批量一个不支持批量
				channel.basicNack(envelope.getDeliveryTag(), false, true);
				
				//basicNack和basicAck不要一起使用
				//手动ack应答
				//deliveryTag:该消息的index 表示是那条消息
				//multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息。
				//消费者处理时由于程序本身bug或者宕机没有ack，那么rabbit会将该信息丢给另外的消费者进行处理。并且rabbit不会再给该消费者丢消息。
				//这里注释掉模拟下不应答 如果消费者抛出异常 这条消息将会被转发给其他消费者  如果不抛出异常且没有碟机则会一直阻塞在这里 很恐怖的
				//channel.basicAck(envelope.getDeliveryTag(),false);

				
			}
		};
		//消息确认 关闭ack自动应答
		boolean autoAck = false;
		channel.basicConsume(QUEUE_NAME, autoAck, consumer);
		
		//避免程序结束
		System.in.read(); 
	}

	/**
	 * 消费者2 处理能力弱
	 * 
	 * @param
	 * @throws IOException 
	 */
	@Test
	public void name4() throws IOException {
		// 创建一个通道
		Channel channel = connection.createChannel();

		int prefetchCount = 1;
		/**
		 * 公平调度（能者多劳）
		 * 这告诉RabbitMQ一次不能给一个工作者多个消息。或者换句话说，不要向工作人员发送新消息，直到处理并确认了前一个消息。相反，它会将其分派给下一个还不忙的工作人员。
		 */
		channel.basicQos(prefetchCount);

		// 创建一个队列
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		//这里是异步的处理  juit不会等待子线程运行完结 就会结束主线程 所以我们 要避免主程序停止  
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println("消费者2收到消息"+message);
				try {
					//模拟处理能力弱
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					//消息消费成功 手动应答服务器 通知可以删除该消息
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};

		//消息确认 关闭自动应答
		boolean autoAck = false;
		channel.basicConsume(QUEUE_NAME, autoAck, consumer);
		
		//避免程序结束
		System.in.read(); 
	}


}
