package rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/*
 * 有了ack机制可以确保消息一定会被消费
 * 那么如何确保消息一定会被发送进队列？
 * rabbitmq有3种方式可以确保消息一定会发送到队列
 */
public class Test7 {
	// 队列名称
	private static final String QUEUE_NAME = "publisherConfirm";
	// 交换机名称
	private static final String EXCHANGE_NAME = "directConfirm";

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

	/*
	 * RabbitMQ 提供了事务机制可以确保发布方消息必达。但是吞吐量会降为越来的 1/250，这个性能损耗是无法接受的。 跳过
	 */
	@Test
	public void name1() throws Exception {
		Channel channel = connection.createChannel();
		// transaction 机制
		// 开启事务
		channel.txSelect();

		String msg = "msg  test !!!";
		for (int i = 0; i < 10000; i++) {
			msg = i + " : msg  test !!!";
			channel.basicPublish(EXCHANGE_NAME, QUEUE_NAME, null, msg.getBytes());
			System.out.println("发布 msg " + msg);
			if (i > 0 && i % 100 == 0) {
				// 批量提交
				channel.txCommit();
			}

		} // 若出现异常 进行 channel.txRollback()，对相应批次的msg进行重发或记录 这里为了简单没try cach
		channel.txCommit();

		channel.close();

	}

	/*
	 * 使用监听 异步方式效率高
	 */
	@Test
	public void name2() throws IOException, InterruptedException, TimeoutException {
		// 创建到服务器的信道
		Channel channel = connection.createChannel();
		// 创建队列
		channel.queueDeclare(QUEUE_NAME, false, false, true, null);

		channel.exchangeDeclare(EXCHANGE_NAME, "direct");

		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "wcl");
		// 方法将当前信道设置为确认模式 类似与ack的手动模式
		channel.confirmSelect();
		// 异步机制 效率高
		channel.addConfirmListener(new ConfirmListener() {

			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				// 消息发送到交换机失败 失败后可以根据deliveryTag进行重新发送
				System.out.printf("nack: %s %s\n", deliveryTag, multiple);
			}

			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				// 消息发送到交换机成功
				System.out.printf("ack: %s %s\n", deliveryTag, multiple);
			}
		});
		// 发送消息
		for (int i = 0; i < 200; i++) {
			String message = "NO. " + i;
			System.out.println("开始发送消息" + message);
			channel.basicPublish(EXCHANGE_NAME, "wcl", null, message.getBytes());
			Thread.sleep(1000);
		}
		channel.close();
	}

	/*
	 * 使用waitForConfirms 同步方式效率底与异步高于事务（事务你就忘记吧）
	 */
	@Test
	public void name3() throws Exception {
		// 创建到服务器的信道
		Channel channel = connection.createChannel();
		// 创建队列
		channel.queueDeclare(QUEUE_NAME, false, false, true, null);

		channel.exchangeDeclare(EXCHANGE_NAME, "direct");

		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "wcl");
		// 方法将当前信道设置为确认模式 类似与ack的手动模式
		channel.confirmSelect();

		String msg = "msg  test !!!";
		for (int i = 0; i < 10000; i++) {
			msg = i + " : msg  test !!!";
			channel.basicPublish(EXCHANGE_NAME, "wcl", null, msg.getBytes());
			System.out.println("发布 msg " + msg);
			// 此处的 if 是为了实现批量confirm 能比较好的提高性能
			if (i > 0 && i % 100 == 0) {
				if (channel.waitForConfirms()) {
					System.out.println("成功发布 msg " + (i - 100) + " to " + i);
				} else {
					System.out.println("发布失败 msg " + (i - 100) + " to " + i);
					i -= 100;// 此处-100是为了重发，也可以先记录下，之后再进行重发
				}
			}
		}
		channel.close();
	}

}
