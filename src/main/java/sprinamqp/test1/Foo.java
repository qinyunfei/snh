package sprinamqp.test1;

//消息队列监听
public class Foo {

	public void listen(String foo) {
		System.out.println("消费者 消费消息"+foo);
	}
}
