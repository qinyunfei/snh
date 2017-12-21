package sprinamqp.test2;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

public class MsgSendConfirmCallBack implements RabbitTemplate.ConfirmCallback {

	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		// TODO Auto-generated method stub
		System.out.println("ack :"+ack);
		if (ack) {
			System.out.println("消息成功到达交换机");
		} else {
			// 处理丢失的消息
			System.out.println("消息没有到达交换机, 失败原因" + cause);
		}
	}

}
