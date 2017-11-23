package dubbo;

//接口实现类 就是服务的实现
public class DemoServiceImpl implements DemoService {

	@Override
	public String sayHello(String name) {
		// TODO Auto-generated method stub
		 return "Hello " + name;
	}

}
