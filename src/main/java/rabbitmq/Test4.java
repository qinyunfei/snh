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
 * @Description: 六大模式之路由
 * @author: Qin YunFei
 * @date: 2017年12月16日 下午3:33:34
 * @version V1.0
 */
public class Test4 {

	// 交换机名称
	private static final String EXCHANGE_NAME = "direct_logs";

	// 路由关键字
	private static final String[] routingKeys = new String[] { "info", "warning", "error" };

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

	// 生产者 发送消息到交换机 并指定队列映射的路由key
	@Test
	public void name1() throws IOException, TimeoutException {
		// 获取到服务器的通道
		Channel channel = connection.createChannel();
		// 向服务器申请一个交换机
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		// 发送信息
		for (String routingKey : routingKeys) {
			String message = "生产者1 发送消息级别:" + routingKey;
			channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
			System.out.println("生产者1 发送" + routingKey + "':'" + message);
		}
		channel.close();
		connection.close();
	}

	// 消费者1消费路由key为info的消息消息
	@Test
	public void name2() throws IOException {
		// 获取到服务器的通道
		Channel channel = connection.createChannel();
		//向服务器申请一个队列
		String queueName = channel.queueDeclare().getQueue();
		//向服务器申请一个交换机
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		//根据路由关键字进行绑定
		channel.queueBind(queueName,EXCHANGE_NAME,"info");
        System.out.println("消费者1  等待消息");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("消费者1 收到了 '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
		//避免程序结束
		System.in.read();
	}

	// 消费者2消费路由key为warning和error的消息
	@Test
	public void name3() throws Exception {
		// 获取到服务器的通道
		Channel channel = connection.createChannel();
		//向服务器申请一个队列
		String queueName = channel.queueDeclare().getQueue();
		//向服务器申请一个交换机
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		//根据路由关键字进行绑定
		channel.queueBind(queueName,EXCHANGE_NAME,"warning");
		channel.queueBind(queueName,EXCHANGE_NAME,"error");
        System.out.println("消费者2  等待消息");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("消费者2 收到了 '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
		//避免程序结束
		System.in.read();
	}

}
