package framework.generic.mybatis;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.util.Assert;

import framework.generic.executor.ExecutorFactoryBean;

public class MapperFactoryBean<T> extends ExecutorFactoryBean {

	private boolean addToConfig = true;

	private SqlSession sqlSession;

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSession = new SqlSessionTemplate(sqlSessionFactory);
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSession = sqlSessionTemplate;
	}

	public void setAddToConfig(boolean addToConfig) {
		this.addToConfig = addToConfig;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		Assert.notNull(this.sqlSession, "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");
		Configuration configuration = this.sqlSession.getConfiguration();
		if ((this.addToConfig) && (!configuration.hasMapper(getMapperInterface())))
			configuration.addMapper(getMapperInterface());
	}

}
