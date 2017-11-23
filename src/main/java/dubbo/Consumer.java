package dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Consumer {

	public static void main(String[] args) {
		//使用spring方式订阅服务
		//启动spring容器
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo/dubbo-consumer.xml");
		context.start();
		DemoService demoService = (DemoService) context.getBean("demoService");// 获取远程服务代理
		String sayHello = demoService.sayHello("sasasasasasas");// 执行远程方法
		System.out.println(sayHello);// 显示调用结果
		
	}

}
