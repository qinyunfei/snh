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
 * @Description:六大模式之一 hello word 
 * @author: Qin YunFei
 * @date: 2017年12月16日 上午9:36:18
 * @version V1.0
 */
public class Test1 {

	private Connection connection = null;
	
	//队列的名称
    public final static String QUEUE_NAME="rabbitMQ.test";


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

	// 消息的发送者
	@Test
	public void name1() throws IOException, TimeoutException {
		// 创建一个链接服务器的通道
		Channel channel = connection.createChannel();
		
		
		
		/*
		 * 在服务器上创建一个队列 queueDeclare第一个参数表示队列名称、 第二个参数为是否持久化（true表示是，队列将在服务器重启时生存）、
		 * 第三个参数为是否是独占队列（创建者可以使用的私有队列，断开后自动删除）、 第四个参数为当所有消费者客户端连接断开时是否自动删除队列、
		 * 第五个参数为队列的其他参数
		 * 
		 * 注意RabbitMQ不允许你使用不同的参数重新定义一个已经存在的队列，并且会向任何尝试这样做的程序返回一个错误
		 */
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		String message = "Hello World!2";
		/**
		 * 发送消息到服务器知道的队列中队列中 basicPublish第一个参数为交换机名称、 第二个参数为队列映射的路由key、 第三个参数为消息的其他属性、
		 * 第四个参数为发送信息的主体
		 */
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		// 关闭通道
		channel.close();
		// 关闭链接
		connection.close();
	}

	// 消息的消费者
	@Test
	public void name2() throws IOException {
		// 创建通道
		Channel channel = connection.createChannel();
		 //声明要关注的队列 
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println("客户等待接收到的消息");
		// DefaultConsumer类实现了Consumer接口，通过传入一个通道，告诉服务器我们需要那个通道的消息，如果通道中有消息，就会执行回调函数handleDelivery
		// 定义队列的消费者
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] 收到了 '" + message + "'");
			}
		};
		// 自动回复队列应答 -- RabbitMQ中的消息确认机制 消费者发回确认（告知）告诉RabbitMQ已经收到，处理了一个特定的消息，并且RabbitMQ可以自由删除这个消息。
		//autoAck 为自动处理 当消费者收到消息就通知RabbitMQ可以删除这条消息  也不管该消息是否消费成功
		//所以我们一般关闭自动应当  改为手动应答当消息消费成功 就应答服务器
		boolean autoAck = true ;
		channel.basicConsume(QUEUE_NAME, autoAck, consumer);
		System.in.read();
	}

}
