package quartz;



import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerKey;


//作业实现类 石英调度的目标任务
public class HelloJob implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		System.out.println();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
		//获取作业器
		JobDetail jobDetail = context.getJobDetail();
		//获取触发器
		CronTrigger trigger = (CronTrigger) context.getTrigger();
		//获取作业器参数
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		//获取触发器参数
		JobDataMap triggerDataMap = trigger.getJobDataMap();
		//获取作业器key
		JobKey jobKey = jobDetail.getKey();
		//获取触发器key
		TriggerKey triggerKey = trigger.getKey();
		
		System.out.println("作业名称 : "+jobKey.getName()+"  作业分组 : "+jobKey.getGroup());
		
		System.out.println("触发器名称 : "+triggerKey.getName()+"  触发器分组 : "+triggerKey.getGroup());
		
		System.out.println("cron表达式 : "+trigger.getCronExpression());

		for (String string : jobDataMap.getKeys()) {
			System.out.println("作业器参数key : "+string+"  value : "+jobDataMap.get(string));
		}
		
		
		for (String string : triggerDataMap.getKeys()) {
			System.out.println("触发器参数key : "+string+"  value : "+triggerDataMap.get(string));
		}

//		System.out.println("本作业第一次开始时间： "+Tools.dateToStr(trigger.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
//		System.out.println("本次作业开始时间"+Tools.dateToStr(trigger.getPreviousFireTime(), "yyyy-MM-dd HH:mm:ss"));
//		System.out.println("下一次作业开始时间："+Tools.dateToStr(trigger.getNextFireTime(), "yyyy-MM-dd HH:mm:ss"));
//		System.out.println("获取指定时间后的作业时间："+Tools.dateToStr(trigger.getFireTimeAfter(new Date()), "yyyy-MM-dd HH:mm:ss"));
		
		//throw new JobExecutionException("测试异常");
		
		
	}

}
