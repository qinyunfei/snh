package rabbitmq;

import java.io.IOException;
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
 * @Description: 六大模式之一 发布订阅模式
 * @author: Qin YunFei
 * @date: 2017年12月16日 上午11:26:48
 * @version V1.0
 */
public class Test3 {

	// 交换机名称
	private static final String EXCHANGE_NAME = "mylogs";

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

	// 消息生产者将消息发步到交换机
	@Test
	public void name1() throws IOException, TimeoutException {
		/**
		 * 创建到服务器的通道
		 */
		Channel channel = connection.createChannel();

		/*
		 * 向服务器声明一个交换机
		 */
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");// fanout表示分发，所有的消费者得到同样的队列信息
		// 分发信息 到交换机 有意思到是交换机只是复杂转发消息  并不提供消息的存储 换句话说就是你没队列在交换机上绑定则消息就丢失了
		for (int i = 0; i < 100; i++) {
			String message = "Hello World" + i;
			channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
			System.out.println("EmitLog Sent '" + message + "'");
		}
		channel.close();
		connection.close();
	}

	/**
	 * 
	 * 创建一个队列  和消费该队列
	 */
	@Test
	public void name2() throws IOException {
		/**
		 * 创建到服务器的通道
		 */
		Channel channel = connection.createChannel();

		/**
		 * 向服务器声明一个交换机
		 */
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

		/**
		 * 创建一个随机队列 当我们不给queueDeclare（）提供参数时，会创建一个非持久的，独占的自动删除队列 并自动生成队列名称
		 */
		String queueName = channel.queueDeclare().getQueue();
		System.out.println(queueName);

		/**
		 * 为队列绑定交换机
		 */
		channel.queueBind(queueName, EXCHANGE_NAME, "");

		System.out.println("消费者1等待消息");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println("消费者1 收到了"+queueName+"消息 " + message );
			}
		};
		
		//自动回复队列应答
        channel.basicConsume(queueName, true, consumer);
        ///
        System.in.read();
	}
	
	
	/**
	 * 
	 * 在创建一个队列  和消费该队列
	 */
	@Test
	public void name3() throws IOException {
		/**
		 * 创建到服务器的通道
		 */
		Channel channel = connection.createChannel();

		/**
		 * 向服务器声明一个交换机
		 */
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

		/**
		 * 创建一个随机队列 当我们不给queueDeclare（）提供参数时，会创建一个非持久的，独占的自动删除队列 并自动生成队列名称
		 */
		String queueName = channel.queueDeclare().getQueue();
		System.out.println(queueName);

		/**
		 * 为队列绑定交换机
		 */
		channel.queueBind(queueName, EXCHANGE_NAME, "");

		System.out.println("消费者2等待消息");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println("消费者2 收到了队列"+queueName+"的消息 " + message);
			}
		};
		
		//自动回复队列应答
        channel.basicConsume(queueName, true, consumer);
        System.in.read();
	}
	

}
