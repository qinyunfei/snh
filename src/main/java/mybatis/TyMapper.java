package mybatis;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import domain.Emp;


public class TyMapper {

	public static void main(String[] args) throws IOException {
		// 读取mybatis配置文件获取SqlSessionFactory对象
		SqlSession sqlSession = MybatisHelper.getSqlSession();
		EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
		List<Emp> selectAll = mapper.selectAll();
		System.out.println(selectAll);
		sqlSession.close();
		
		

	}
}
