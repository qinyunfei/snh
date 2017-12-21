package sprinamqp.test1;

import org.junit.Test;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @Description: 官方例子 spring-amqp 是对AMQP规范的一个抽象 尴尬的是目前只有rabbitmq提供了实现 实现包spring-rabbit
 * @author: Qin YunFei
 * @date: 2017年12月18日 上午10:28:06
 * @version V1.0
 */
public class Test1 {

	//spring java 版本
	@Test
	public void name2() throws InterruptedException {
		//带缓存的
		CachingConnectionFactory cf = new CachingConnectionFactory();
		cf.setHost("10.211.55.19");
		cf.setPort(5672);
		cf.setUsername("xiaohei");
		cf.setPassword("123456");
		cf.setVirtualHost("/atguigu");
		// 在代理上设置队列、交换、绑定 类似Channel 链接服务器的通道
		RabbitAdmin admin = new RabbitAdmin(cf);
		//创建一个队列
		Queue queue = new Queue("myQueue");
		//向服务器申请队列
		admin.declareQueue(queue);
		//创建Topic格式的交换机
		TopicExchange exchange = new TopicExchange("myExchange");
		//向服务器申请交换机
		admin.declareExchange(exchange);
		//绑定队列到交换机 路由关键字为foo.*
		admin.declareBinding(
			BindingBuilder.bind(queue).to(exchange).with("foo.*"));

		//设置侦听器和容器（消费者）
		SimpleMessageListenerContainer container =new SimpleMessageListenerContainer(cf);
		
		Object listener = new Object() {
			public void handleMessage(String foo) {
				System.out.println(foo);
			}
		};
		MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
		container.setMessageListener(adapter);
		container.setQueueNames("myQueue");
		container.start();
		

		// 使用RabbitTemplate对象发送消息
		RabbitTemplate template = new RabbitTemplate(cf);
		//向交换机myExchange 发送消息Hello, world! 队列路由key为foo.bar
		template.convertAndSend("myExchange", "foo.bar", "Hello, world!");
		Thread.sleep(1000);
		container.stop();
	}
	
	//spring 配置文件版本 和上面是对应的
	@Test
	public void name() throws InterruptedException {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring-rabbitMQ.xml");
		System.out.println(context);
		RabbitTemplate template = context.getBean(RabbitTemplate.class);
		System.out.println("生产者生产消息： Hello, world!");
		template.convertAndSend("Hello, world!");
		Thread.sleep(1000);
		context.destroy();

	}

}
