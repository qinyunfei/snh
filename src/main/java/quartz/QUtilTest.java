package quartz;

import java.util.HashMap;

import org.junit.Test;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 
 * @Description: 测试QuartzUtil工具类
 * @author: Qin YunFei
 * @date: 2017年11月6日 下午6:24:28
 * @version V1.0
 */
public class QUtilTest {

	public static void main(String[] args) throws SchedulerException {
		name2();
		QuartzUtil.showlist();

		
	}

	public static void name() {
		QuartzUtil.addJob("任务一","触发器一", HelloJob.class, "0/5 * * * * ?");

	}

	public static void name2() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", "5");
		QuartzUtil.addJob("任务二","触发器二",HelloJob.class, "0/5 * * * * ?", map);
		QuartzUtil.addTrigger("任务二", "触发器三", "0/5 * * * * ?");

	}
	
	public static void name3() {
		QuartzUtil.modifyTriggerTime("触发器二", "0/10 * * * * ?");

	}

}
