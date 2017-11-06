package quartz;



import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


//作业实现类 石英调度的目标任务
public class HelloJob implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		
		
		System.out.println("hello 石英调度");

		//throw new JobExecutionException("测试异常");
		
		
	}

}
