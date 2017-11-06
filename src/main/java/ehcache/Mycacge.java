package ehcache;

import org.junit.Before;
import org.junit.Test;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;

public class Mycacge {

	// 缓存管理器
	private CacheManager cacheManager;

	@Before
	public void befo() {
		// 根据ehcache.xml获缓存管理器
		cacheManager = CacheManager.create();
	}

	// 获取所有的缓存名称
	@Test
	public void name() {
		String[] cacheNames = cacheManager.getCacheNames();
		for (String string : cacheNames) {
			System.out.println(string);
		}
	}

	// 根据缓存名称获取一个缓存
	@Test
	public void name2() {
		String[] cacheNames = cacheManager.getCacheNames();
		Cache cache = cacheManager.getCache(cacheNames[0]);
		System.out.println(cache);
	}

	// 向缓存中添加数据并重缓存中取出数据
	@Test
	public void name3() {
		// 获取一个指定缓存
		Cache cache = cacheManager.getCache("sampleCache1");
		// 创建一个指定的缓存元素
		Element element = new Element("key1", "values1");
		// 向缓存中放入数据
		cache.put(element);
		// 从缓存中获取数据
		Element element2 = cache.get("key1");
		System.out.println("缓存的key： " + element2.getObjectKey());
		System.out.println("缓存的value： " + element2.getObjectValue());
		System.out.println("缓存的版本： " + element2.getVersion());
		System.out.println("缓存的计数： " + element2.getHitCount());
		System.out.println("缓存的创建时间： " + element2.getCreationTime());
		System.out.println("缓存的最后访问时间： " + element2.getLastAccessTime());
		System.out.println("缓存的最后更新时间： " + element2.getLastUpdateTime());
	}

	// 手动创建缓存 以上缓存都是从配置文件中读取的缓存
	@Test
	public void name4() throws Exception {
		/*
		 * 创建一个缓存 缓存创建后交由缓存管理器进行初始化 否则会报错 可以查看缓存的状态信息查看当前缓存是否可用 
		 * name: 缓存名称
		 * maxElementsInMemory：内存中元素的最大数量，在它们被清除之前(0==无限制) 
		 * overflowToDisk：是否使用磁盘存储
		 * eternal：缓存中的元素是否是永恒的，即永远不会过期
		 * timeToLiveSeconds：从它的创建日期开始的过期时间
		 * timeToIdleSeconds：从其最近访问或修改的日期开始的过期时间
		 */
		Cache cache = new Cache("test", 1, true, false, 2, 3);

		System.out.println(cache.getStatus());

		// 将改缓存添加到缓存管理器
		cacheManager.addCache(cache);

		System.out.println(cache.getStatus());

		cache.put(new Element("jimmy", "菩提树下的杨过"));

		// 故意停1.5秒，以验证是否过期
		Thread.sleep(1500);

		Element eleJimmy = cache.get("jimmy");

		// 1.5s < 2s 不会过期
		if (eleJimmy != null) {
			System.out.println("jimmy \t= " + eleJimmy.getObjectValue());
		}

		// 再等上0.5s, 总时长：1.5 + 0.5 >= min(2,3),过期
		Thread.sleep(500);

		eleJimmy = cache.get("jimmy");

		if (eleJimmy != null) {
			System.out.println("jimmy \t= " + eleJimmy.getObjectValue());
		}

		// 取出一个不存在的缓存项
		System.out.println("fake \t= " + cache.get("fake"));

		cacheManager.shutdown();
	}

}
