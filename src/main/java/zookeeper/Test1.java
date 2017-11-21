package zookeeper;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

public class Test1 {

	private ZooKeeper zooKeeper;

	@Before
	public void before() throws IOException {
		// 获取ZooKeeper（就是服务器）
		//zooKeeper = new ZooKeeper("10.211.55.19:2181", 5000, new Watcher() {
		zooKeeper = new ZooKeeper("10.211.55.19:2181,10.211.55.24:2181,10.211.55.25:2181", 5000, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				// TODO Auto-generated method stub
				
			}});
	}
	// 创建节点
	@Test
	public void name() throws IOException, KeeperException, InterruptedException {
		// acl 访问控制权限列表
		// CreateMode节点类型
		String create = zooKeeper.create("/wcl", "666".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println(create);

	}
	
	// 获取节点
	@Test
	public void name2() throws IOException, KeeperException, InterruptedException {
		
		
		byte[] data =zooKeeper.getData("/wcl", new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				// TODO Auto-generated method stub
				System.out.println("数据有变化");
			}}, null);
		System.out.println(new String(data));
	}
	
	//修改节点
	@Test
	public void name3() throws IOException, KeeperException, InterruptedException {
		//-1忽略版本匹配
		Stat stat = zooKeeper.setData("/wcl", "777".getBytes(), -1);
		System.out.println(stat);
	}
	
	// 删除节点
	@Test
	public void name4() throws IOException, KeeperException, InterruptedException {
		//-1忽略版本匹配
		zooKeeper.delete("/wcl", -1);
	}

	//列出所有节点
	@Test
	public void name5() throws IOException, KeeperException, InterruptedException {
		//获取子节点 可以递归出所有节点 懒得写啦
		List<String> children = zooKeeper.getChildren("/", null); 
		System.out.println(children);
	}

}
