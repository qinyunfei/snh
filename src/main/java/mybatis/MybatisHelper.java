package mybatis;

import java.io.IOException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
//通用mapper3配置
public class MybatisHelper {
	
	 private static SqlSessionFactory sqlSessionFactory;
	 
	 static {
		// 读取mybatis配置文件获取SqlSessionFactory对象
			try {
				 sqlSessionFactory = new SqlSessionFactoryBuilder()
						.build(Resources.getResourceAsStream("mybatis/mybatis-config.xml"));
				
				SqlSession session = sqlSessionFactory.openSession();
				
				MapperHelper mapperHelper = new MapperHelper();
//				// 特殊配置
//				Config config = new Config();
//				// 具体支持的参数看后面的文档
//				//config.setXXX(XXX);
//				// 设置配置
//				mapperHelper.setConfig(config);
				
				
				// 注册自己项目中使用的通用Mapper接口，这里没有默认值，必须手动注册
				mapperHelper.registerMapper(Mapper.class);
				// 配置完成后，执行下面的操作
				mapperHelper.processConfiguration(session.getConfiguration());
				session.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		 
		 
	 }
	
	
	
	   /**
     * 获取Session
     * @return
     */
    public static SqlSession getSqlSession(){
        return sqlSessionFactory.openSession();
}

}
