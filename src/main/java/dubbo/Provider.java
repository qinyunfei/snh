package dubbo;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

//Provider ：dubbo 4大角色之一 暴露服务的服务提供方
public class Provider {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		//使用spring方式发布服务
		//启动spring容器
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo/dubbo-provider.xml");
		//启动容器过程中就会发布服务
		context.start();
		//避免程序结束
		System.in.read(); // 按任意键退出
	}

}
