package sprinamqp.test2;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class MsgSendReturnCallback implements RabbitTemplate.ReturnCallback{
	
    @Autowired
    private RabbitTemplate errorTemplate;
    
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
    		System.out.println("消息没有进入队列 ");
        System.out.println("return--message:"+new String(message.getBody())+",replyCode:"+replyCode+",replyText:"+replyText+",exchange:"+exchange+",routingKey:"+routingKey); 
        
        //重新发布 需要注意如果是自动重发的话,消费端需要做幂等或去重处理.
//        RepublishMessageRecoverer recoverer = new RepublishMessageRecoverer(errorTemplate,"errorExchange", "errorRoutingKey");
//        Throwable cause = new Exception(new Exception("route_fail_and_republish"));
//        recoverer.recover(message,cause);
//        System.out.println("Returned Message："+replyText);
//        
    }

}