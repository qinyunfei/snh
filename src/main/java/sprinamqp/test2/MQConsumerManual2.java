package sprinamqp.test2;

import java.io.UnsupportedEncodingException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.rabbitmq.client.Channel;

public class MQConsumerManual2 implements ChannelAwareMessageListener {

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		// TODO Auto-generated method stub

		try {
			Thread.sleep(1000);
			System.out.println("手动应答的消费者2--:"+new String(message.getBody())+" >>>>> " + message.getMessageProperties());
			// ack应答
			System.out.println("消费者2ack应答");

			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			e.printStackTrace();// TODO 业务处理
			// nack应答 丢弃消息
			System.out.println("消费者2nack应答 丢弃消息");
			channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
		}
	}

}
