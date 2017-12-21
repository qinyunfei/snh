package rabbitmq;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
import com.rabbitmq.client.GetResponse;

/**
 * 
 * @Description: 死信队列 rabbitmq进阶
 * @author: Qin YunFei
 * @date: 2017年12月18日 下午7:36:57
 * @version V1.0
 */
public class Test6 {
	/**
	 * 队列中的消息可能会成为死信消息（dead lettered）。让消息成为死信消息的事件有：
	 * 
	 * 消息被取消确认（nack 或 reject），且设置为不重入队列（requeue = false） 消息TTL过期 队列达到长度限制
	 * 
	 * 死信消息会被死信交换机（Dead Letter Exchange, DLX）重新发布。
	 */

	// 死信交换机
	private static final String DLX_EXCHANGE_NAME = "exchangeDLX";
	// 死信队列
	private static final String DLX_QUEUE_NAME = "queueDLX";
	// 普通队列
	private static final String QUEUE_NAME = "test_queue";
	
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

	@Test
	public void name1() throws IOException, InterruptedException {
		Channel channel = connection.createChannel();
		Map<String,Object> args=new HashMap<>();
		args.put("x-message-ttl", 3000);//设置队列消息的ttl为3秒
		args.put("x-max-length", 5);//设置队列的最大长度为5
		args.put("x-dead-letter-exchange", DLX_EXCHANGE_NAME);//设置死信交换机 当死信消息找不到指定的交换机时，死信消息会被RabbitMQ安静的丢弃，而不是抛出异常。
		args.put("x-dead-letter-routing-key", "dlx-routing-key");//设置死信路由密钥 如果不设置可以使用队列名

		//创建普通队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, args);
		//创建死信队列
		channel.queueDeclare(DLX_QUEUE_NAME, false, false, false, null);
		//创建direct模式死信交换机
		channel.exchangeDeclare(DLX_EXCHANGE_NAME, "direct", false, true,null);
		//绑定死信队列到死信交换机 路由密匙为dlx-routing-key 
		channel.queueBind(DLX_QUEUE_NAME, DLX_EXCHANGE_NAME, "dlx-routing-key");
		
		//向普通队列中写入消息 队列达到长度限制进入死信
		for (int i = 0; i < 6; i++) {
			String msg="wcl"+i;
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
		}
		
		// 监视死信队列
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.printf("死信消费: %s, envelop: %s, properties: %s\n", message, envelope, properties);			}
		};
		//自动提交ack
		channel.basicConsume(DLX_QUEUE_NAME, true, consumer);
		Thread.sleep(100);
		//消息被取消确认（nack 或 reject），且设置为不重入队列（requeue = false）进入死信
		//autoAck 关闭自动ack
		GetResponse resp = channel.basicGet(QUEUE_NAME, false);
		byte[] body = resp.getBody();
		String message = new String(body, "UTF-8");
		System.out.println("即将被丢弃的消息"+message);
		//丢弃消息
		channel.basicNack(resp.getEnvelope().getDeliveryTag(), false, false);
	}

}
