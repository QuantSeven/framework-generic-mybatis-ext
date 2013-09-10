package framework.generic.mybatis;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.binding.MapperMethod;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import framework.generic.dao.Model;
import framework.generic.executor.Executor;
import framework.generic.util.FrameworkUtil;

public class MybatisGenericExecutor<T extends Model<PK>, PK extends Serializable> extends SqlSessionDaoSupport implements Executor, ApplicationContextAware {

	private static final Log log = LogFactory.getLog(MybatisGenericExecutor.class);
	public static final String EXECUTE_INSERT = "insert";
	public static final String EXECUTE_FIND = "find";
	public static final String EXECUTE_UPDATE = "update";
	public static final String EXECUTE_DELETE = "delete";
	public static final String EXECUTE_COUNT = "count";

	private MethodSignature methodSignature;
	private Map<Method, MethodSignature> methodCache = new HashMap<Method, MethodSignature>();
	private ApplicationContext applicationContext;

	public MybatisGenericExecutor() {
		// methodSignature = new MethodSignature(method);
	}

	public boolean supports(Method paramMethod) {
		return true;
	}

	public Object execute(Class<?> paramClass, Method paramMethod, Object[] paramArrayOfObject) {
		methodSignature = cachedMethodSignature(paramMethod);
		int i = 0;
		Object param = methodSignature.convertArgsToSqlCommandParam(paramArrayOfObject);
		if (!FrameworkUtil.isNullOrEmpty(paramMethod.getName())) {
			if (paramMethod.getName().startsWith(EXECUTE_INSERT)) {
				i = getSqlSession().insert(paramClass.getName(), param);
			} else if (paramMethod.getName().startsWith(EXECUTE_UPDATE)) {
				i = getSqlSession().update(paramMethod.getName(), param);
			}else if (paramMethod.getName().startsWith(EXECUTE_FIND)) {
				if(containsMappedStatementName("com.pousheng.demo.user"+"."+paramMethod.getName())){
					
					getSqlSession().selectOne("com.pousheng.demo.user"+"."+paramMethod.getName(), param);
				}
			}
		}

		return i;
	}

	private boolean containsMappedStatementName(String paramString) {
		return getSqlSession().getConfiguration().getMappedStatementNames().contains(paramString);
	}

	private MethodSignature cachedMethodSignature(Method method) {
		MethodSignature methodSignature = methodCache.get(method);
		if (methodSignature == null) {
			methodSignature = new MethodSignature(method);
			methodCache.put(method, methodSignature);
		}
		return methodSignature;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}