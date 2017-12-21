package sprinamqp.test2;

import java.io.UnsupportedEncodingException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class MQConsumer implements MessageListener {

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {
			String body = new String(message.getBody(), "UTF-8");
			System.out.println("自动应答的消费者0："+body);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
