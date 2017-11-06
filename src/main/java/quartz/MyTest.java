package quartz;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.KeyMatcher;

public class MyTest {

	public static void main(String[] args) throws Exception {

	}

	public void name() throws Exception {
		// 创建作业
		JobDetail job = JobBuilder.newJob(HelloJob.class)
				.build();

		// 创建触发器
		Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
				.usingJobData(null)//这里可以放参数 可以看方法的重载
				.build();

		// 从调度工厂获取触发器
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		// 启动调度器
		scheduler.start();
		// 绑定作业和触发器
		scheduler.scheduleJob(job, trigger);
	}

	public static void name2() throws Exception {
		JobKey jobKey = new JobKey("作业一", "分组一");
		JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity(jobKey).build();

		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("触发器一", "分组一")
				.withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?")).build();
		
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		
		// 添加监听 监听jobKey 也可以监听jobKey的组
		scheduler.getListenerManager().addJobListener(new HelloJobListener(), KeyMatcher.keyEquals(jobKey));

		// scheduler.getListenerManager().addJobListener( new HelloJobListener(),GroupMatcher.jobGroupEquals("group1") );

		scheduler.start();
		scheduler.scheduleJob(job, trigger);
		
		name3();

	}

	//获取所有的
	public static void name3() throws SchedulerException {

		Scheduler scheduler = new StdSchedulerFactory().getScheduler();

		for (String groupName : scheduler.getJobGroupNames()) {
		System.out.println(groupName);

			for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

				String jobName = jobKey.getName();
				String jobGroup = jobKey.getGroup();

				// get job's trigger
				List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
				Date nextFireTime = triggers.get(0).getNextFireTime();
				System.out.println("[jobName] : " + jobName + " [groupName] : " + jobGroup + " 下一个执行时间 " + nextFireTime);

			}

		}

	}

}
