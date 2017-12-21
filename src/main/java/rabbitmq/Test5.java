package rabbitmq;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * 
 * @Description: 六大模式之 主题 路由的模糊匹配版
 * @author: Qin YunFei
 * @date: 2017年12月16日 下午3:33:59
 * @version V1.0
 */
public class Test5 {
	// 交换机名称
	private static final String EXCHANGE_NAME = "topic_logs";

	// 路由关键字
	private static final String[] routingKeys = new String[] { "quick.orange.rabbit", "lazy.orange.elephant",
			"quick.orange.fox", "lazy.brown.fox", "quick.brown.fox", "quick.orange.male.rabbit",
			"lazy.orange.male.rabbit" };

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

	// 生产者发送消息
	@Test
	public void name1() throws IOException, TimeoutException {
		// 创建到服务器的通道
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");

		// 发送消息到交换机
		for (String routingKey : routingKeys) {
			String message = "生产者1 消息级别:" + routingKey;
			channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
			System.out.println("生产者1 发送" + routingKey + "':'" + message);
		}

		channel.close();
		connection.close();
	}

	// 消费者1 消费到路由key *.orange.*
	@Test
	public void name2() throws Exception {
		// 创建到服务器的通道
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		String queueName = channel.queueDeclare().getQueue();
		// 路由关键字
		String[] routingKeys = new String[] { "*.orange.*" };
		// 绑定路由
		for (String routingKey : routingKeys) {
			channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
			System.out.println("消费者1 交换:" + EXCHANGE_NAME + ", 队列:" + queueName + ", 路由key:" + routingKey);
		}
		System.out.println("消费者1 等待接收消息");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println("消费者1 收到:  '" + envelope.getRoutingKey() + "':'" + message + "'");
			}
		};
		channel.basicConsume(queueName, true, consumer);
		System.in.read();
	}

	// 消费者2
	@Test
	public void name3() throws Exception {
		// 创建到服务器的通道
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		String queueName = channel.queueDeclare().getQueue();
		// 路由关键字
		String[] routingKeys = new String[] { "*.*.rabbit", "lazy.#" };
		// 绑定路由关键字
		for (String bindingKey : routingKeys) {
			channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
			System.out.println("消费者2 路由:" + EXCHANGE_NAME + ", 队列:" + queueName
					+ ", 路由key:" + bindingKey);
		}

		System.out.println("消费者2 等待接收消息");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws UnsupportedEncodingException {
				String message = new String(body, "UTF-8");
				System.out.println("消费者2 收到 '" + envelope.getRoutingKey() + "':'" + message + "'");
			}
		};
		channel.basicConsume(queueName, true, consumer);
		System.in.read();
	}

}
