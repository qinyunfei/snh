package sprinamqp.test2;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Emp;

/**
 * 
 * @Description: 这个例子演示消息发送的确认机制 和消息消费的ack机制
 * @author: Qin YunFei
 * @date: 2017年12月18日 上午10:28:06
 * @version V1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-rabbitMQ2.xml" })
public class Test2 {

	// 交换机名称
	private static final String EXCHANGE_NAME = "test-mq-exchange";

	// 路由关键字
	private static final String ROUTINGKEYS = "test_queue_key";

	@Autowired
	private RabbitTemplate template;

	
	//消息消费者的ack机制演示
	@Test
	public void name() throws InterruptedException, IOException {

		for (int i = 0; i < 50; i++) {
			Emp emp = new Emp();
			emp.setEmpno(i);
			System.out.println("发送消息 " + i);
			// 发送消息
			template.convertAndSend(EXCHANGE_NAME, ROUTINGKEYS, emp);
		}

		System.in.read();

	}

	//消息生产者的确认机制演示
	@Test
	public void test1() throws InterruptedException, IOException {
		String message = "no.1";
		// exchange,routingKeys 都正确,confirm被回调, ack=true
		template.convertAndSend(EXCHANGE_NAME, ROUTINGKEYS, message);
		System.in.read();
	}

	@Test
	public void test2() throws InterruptedException, IOException {
		String message = "no.2";
		// exchange 错误,routingKeys 正确,confirm被回调, ack=false
		template.convertAndSend(EXCHANGE_NAME + "no", ROUTINGKEYS, message);
		Thread.sleep(1000);
		System.in.read();
	}

	@Test
	public void test3() throws InterruptedException, IOException {
		String message = "no.3";
		// exchange 正确,routingKeys 错误 ,confirm被回调, ack=true; return被回调
		// replyText:NO_ROUTE
		template.convertAndSend(EXCHANGE_NAME, ROUTINGKEYS+"no", message);
		System.in.read();
	}

	@Test
	public void test4() throws InterruptedException, IOException {
		String message = "no.4";
		// exchange 错误,routingKeys 错误,confirm被回调, ack=false
		template.convertAndSend(EXCHANGE_NAME + "no", ROUTINGKEYS+"no", message);
		System.in.read();
	}
}
