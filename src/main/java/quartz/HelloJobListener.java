package quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

//作业监听
public class HelloJobListener implements JobListener{
	
	public static final String LISTENER_NAME = "dummyJobListenerName";

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return LISTENER_NAME; 
	}
	//执行作业前，就运行这个。
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		// TODO Auto-generated method stub
		String jobName = context.getJobDetail().getKey().toString();
		System.out.println("Job : " + jobName + " 运行开始…");
	}

	//不晓得什么时候执行
	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		// TODO Auto-generated method stub
		System.out.println("飒飒飒飒飒飒");
	}

	//在执行完作业后运行这个（包括异常）
	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		// TODO Auto-generated method stub
		 
		String jobName = context.getJobDetail().getKey().toString();
		System.out.println("Job : " + jobName + " 运行完成...");
 
		if (!jobException.getMessage().equals("")) {
			System.out.println("发现异常: " + jobName
				+ " 异常为: " + jobException.getMessage());
		}
		
	}

}
