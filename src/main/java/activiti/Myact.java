package activiti;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Before;
import org.junit.Test;

public class Myact {
	
	private ProcessEngine engine =null;
	
	@Before
	public void befo() {
		//创建流程引擎s
		engine = ProcessEngines.getDefaultProcessEngine();
	}
	
	//部署流程
	@Test
	public void name1() {
		/*
		 *一次部署会向3个表中添加数据
		 *会将bpmn的字节信息（理解为java源文件） 存储在 ACT_GE_BYTEARRAY表中
		 *会将本次部署信息（理解为部署日志） 存储在ACT_RE_DEPLOYMENT表中
		 *会将流程信息（理解为class文件） 存储在ACT_RE_PROCDEF表中
		 */
		//获取存储库服务组件 该组件负责流程的部署 已经部署产生的数据的查询
		RepositoryService repositoryService = engine.getRepositoryService();
		
		//获取部署构建器
		DeploymentBuilder createDeployment = repositoryService.createDeployment();
		/*
		 * 多次部署会根据部署文件的key 和部署信息来确定是部署新文件还是版本更替
		 */
		//部署指定bpmn文件  (就是将bpmn文件添加到数据库)获取部署后的部署信息对象
		Deployment deploy = createDeployment.addClasspathResource("请假流程.bpmn")
				.name("请假流程部署")  //为本次部署起个名字
				.category("oa类")	//本次部署的分类
				.tenantId("小秦部署")//自定义本次部署的额外信息
				.deploy();
		/**
		 * getId():本次部署的标识
		 * getName():为这次部署起的一个名字
		 * getDeploymentTime():部署的时间
		 * getCategory():分类
		 * getTenantId():自定义添加一些内容
		 */
		
		System.out.println(deploy);
	}
	
	/*
	 *查询流程并启动流程获取流程实例
	 */
	@Test
	public void name2() {
		//获取存储库服务组件 该组件负责流程的部署 已经部署产生的数据的查询
		RepositoryService repositoryService = engine.getRepositoryService();
		//创建流程定义(理解为class)查询 就是查询ACT_RE_PROCDEF表 可以获取该表的所有信息
		ProcessDefinitionQuery createProcessDefinitionQuery = repositoryService.createProcessDefinitionQuery();
		//获取全部流程定义
		List<ProcessDefinition> list = createProcessDefinitionQuery.list();
		
		for (ProcessDefinition processDefinition : list) {
			System.out.println(processDefinition.getId());
		}
		
		//还可以使用各种条件查询 就是根据表字段的值来查询 例如根据ACT_RE_PROCDEF表的Key来查询 最新版本的流程定义 singleResult是获取单一结果 条件设定完 查出来的就是一个 不过回返回list而已
		ProcessDefinition singleResult = createProcessDefinitionQuery.processDefinitionKey("请假流程v1.0").latestVersion().singleResult();
		System.out.println(">>>>>"+singleResult.getId());
		
		
		/*
		 * 一次流程启动创建流程实例时会向5张表中添加数据
		 * 会向运行时表ACT_RU_EXECUTION中添加流程实例信息  一个流程实例被创建后就会进入流程进行执行  （理解为目前的有那些对象正在运行）
		 * 会向运行时表ACT_RU_TASK 中添加流程运行状态信息 即该流程实例目前执行的任务点的详细信息
		 * 
		 * 会向历史表ACT_HI_PROCINST中添加本次流程启动的描述信息
		 * 会向历史表ACT_HI_TASKINST添加流程执行任务的开始信息
		 * 会向历史表ACT_HI_ACTINST中添加 流程实例 所有的流程状态信息
		 * 
		 * 
		 */
		
		//获取运行时服务组件 该组件负责启动一个流程即创建该流程的流程实例(理解为根据class new 一个对象)
		RuntimeService runtimeService = engine.getRuntimeService();
		//获取流程实例（理解为根据指定的class new 一个对象）
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(singleResult.getId());
		System.out.println(processInstance);
		
	}
	//获取流程实例的任务信息
	@Test
	public void name3() {
		//获取任务服务组件
		TaskService taskService = engine.getTaskService();
		//创建任务 可以查询ACT_RU_TASK表的一切信息
		TaskQuery taskQuery = taskService.createTaskQuery();
		List<Task> list = taskQuery.list();
		for (Task task : list) {
			System.out.println(task.getId()+"--->"+task.getName()+"--->"+task.getTaskDefinitionKey());
		}
		
		Task task = taskQuery.processInstanceId("5001").singleResult();
		//签收任务  就是任务分配给谁
		taskService.claim(task.getId(), "xiaoqin");
		//完成任务
		taskService.complete(task.getId());

	}
	/*
	 * 查询历史
	 */
	@Test
	public void name4() {
		//获取历史服务
		HistoryService historyService = engine.getHistoryService();
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId("5001").list();
		for (HistoricTaskInstance historicTaskInstance : list) {
			System.out.println("============");
			System.out.println("任务名称"+historicTaskInstance.getName());
			System.out.println("开始时间"+historicTaskInstance.getStartTime());
			System.out.println("签收时间"+historicTaskInstance.getClaimTime());
			System.out.println("结束时间"+historicTaskInstance.getEndTime());
			System.out.println("执行者"+historicTaskInstance.getAssignee());
		}
	
	}
	
	
	

}
