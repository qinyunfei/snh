package kafka.test2;


import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Author  : RandySun (sunfeng152157@sina.com)
 * Date    : 2017-08-20  16:29
 * Comment :
 */
public class ConsumerThreadHandler implements Runnable {
	
    private ConsumerRecord consumerRecord;

    public ConsumerThreadHandler(ConsumerRecord consumerRecord){
        this.consumerRecord = consumerRecord;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"  消费消息:"+consumerRecord.value()+",分区:"+consumerRecord.partition()+"偏移量"+consumerRecord.offset());
    }
}
