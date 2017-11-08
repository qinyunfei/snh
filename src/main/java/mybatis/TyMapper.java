package mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import domain.Emp;

public class TyMapper {

	// 测试通用mapper查询
	@Test
	public void name() {
		// 读取mybatis配置文件获取SqlSessionFactory对象
		SqlSession sqlSession = MybatisHelper.getSqlSession();
		EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
		List<Emp> selectAll = mapper.selectAll();
		System.out.println(selectAll);
		sqlSession.close();
	}

	// 测试通用mapper添加 并根据主键策略获取id
	@Test
	public void name2() {
		SqlSession sqlSession = MybatisHelper.getSqlSession();
		EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
		Emp emp = new Emp();
		emp.setEname("wcl");
		mapper.insertSelective(emp);
		System.out.println(emp);
		sqlSession.commit();
		sqlSession.close();

	}

}
