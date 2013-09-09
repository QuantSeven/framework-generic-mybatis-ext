package framework.generic.mybatis;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import framework.generic.dao.Interceptor;
import framework.generic.dao.Model;
import framework.generic.executor.Executor;

public class MybatisGenericDao<T extends Model<PK>, PK extends Serializable> extends SqlSessionDaoSupport implements Executor, ApplicationContextAware {
	private static final Log a = LogFactory.getLog(MybatisGenericDao.class);
	private static String b = "user";
	private static List<String> c;
	private ApplicationContext d;
	private Properties g;
	private boolean h = true;
	private boolean i = true;
	private String j;
	private List<Interceptor> k;
	
	private String sqlSessionFactoryBeanName;

	public String getSqlSessionFactoryBeanName() {
		return sqlSessionFactoryBeanName;
	}

	public void setSqlSessionFactoryBeanName(String sqlSessionFactoryBeanName) {
		this.sqlSessionFactoryBeanName = sqlSessionFactoryBeanName;
	}

	@Override
	@javax.annotation.Resource(name="sqlSessionFactory")
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
	}

	public boolean supports(Method paramMethod) {
		return true;
	}

	public Object execute(Class<?> paramClass, Method paramMethod, Object[] paramArrayOfObject) {
		return null;
	}

	public void setProperties(Resource paramResource) {
		try {
			Properties localProperties;
			(localProperties = new Properties()).load(paramResource.getInputStream());
			this.g = localProperties;
			return;
		} catch (Exception localException) {
			a.debug("Exception loading generic dao properties", localException);
		}
	}

	public void setInterceptors(String paramString) {
		this.j = paramString;
	}

	public void setRefreshModel(boolean paramBoolean) {
		this.h = paramBoolean;
	}

	public void setMergeModel(boolean paramBoolean) {
		this.i = paramBoolean;
	}

	private List<Interceptor> a() {
		if ((this.k == null) && (this.j != null)) {
			this.k = new ArrayList();
			StringTokenizer localStringTokenizer = new StringTokenizer(this.j, ";");
			while (localStringTokenizer.hasMoreTokens())
				try {
					Interceptor localInterceptor = (Interceptor) this.d.getBean(localStringTokenizer.nextToken(), Interceptor.class);
					this.k.add(localInterceptor);
				} catch (Exception localException) {
					a.debug("error getting intercepter ", localException);
				}
		}
		return this.k;
	}

	public void setApplicationContext(ApplicationContext paramApplicationContext) {
		this.d = paramApplicationContext;
	}

	static {
		(MybatisGenericDao.c = new ArrayList()).add("create");
		c.add("read");
		c.add("update");
		c.add("delete");
		c.add("count");
		c.add("find");
	}
}