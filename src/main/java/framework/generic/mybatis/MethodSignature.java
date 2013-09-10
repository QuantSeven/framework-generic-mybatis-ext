package framework.generic.mybatis;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

public class MethodSignature {

	private boolean returnsMany;
	private boolean returnsMap;
	private boolean returnsVoid;
	private Class<?> returnType;
	private String mapKey;
	private Integer resultHandlerIndex;
	private Integer rowBoundsIndex;
	private SortedMap<Integer, String> params;
	private boolean hasNamedParameters;

	public MethodSignature(Method method) throws BindingException {
		this.returnType = method.getReturnType();
		this.returnsVoid = void.class.equals(this.returnType);
		this.mapKey = getMapKey(method);
		this.returnsMap = (this.mapKey != null);
		this.hasNamedParameters = hasNamedParams(method);
		this.rowBoundsIndex = getUniqueParamIndex(method, RowBounds.class);
		this.resultHandlerIndex = getUniqueParamIndex(method, ResultHandler.class);
		this.params = Collections.unmodifiableSortedMap(getParams(method, this.hasNamedParameters));
	}

	public MethodSignature(Configuration configuration, Method method) throws BindingException {
		this.returnType = method.getReturnType();
		this.returnsVoid = void.class.equals(this.returnType);
		this.returnsMany = (configuration.getObjectFactory().isCollection(this.returnType) || this.returnType.isArray());
		this.mapKey = getMapKey(method);
		this.returnsMap = (this.mapKey != null);
		this.hasNamedParameters = hasNamedParams(method);
		this.rowBoundsIndex = getUniqueParamIndex(method, RowBounds.class);
		this.resultHandlerIndex = getUniqueParamIndex(method, ResultHandler.class);
		this.params = Collections.unmodifiableSortedMap(getParams(method, this.hasNamedParameters));
	}

	public Object convertArgsToSqlCommandParam(Object[] args) {
		final int paramCount = params.size();
		if (args == null || paramCount == 0) {
			return null;
		} else if (!hasNamedParameters && paramCount == 1) {
			return args[params.keySet().iterator().next()];
		} else {
			final Map<String, Object> param = new ParamMap<Object>();
			int i = 0;
			for (Map.Entry<Integer, String> entry : params.entrySet()) {
				param.put(entry.getValue(), args[entry.getKey()]);
				// issue #71, add param names as param1, param2...but ensure
				// backward compatibility
				final String genericParamName = "param" + String.valueOf(i + 1);
				if (!param.containsKey(genericParamName)) {
					param.put(genericParamName, args[entry.getKey()]);
				}
				i++;
			}
			return param;
		}
	}

	public boolean hasRowBounds() {
		return (rowBoundsIndex != null);
	}

	public RowBounds extractRowBounds(Object[] args) {
		return (hasRowBounds() ? (RowBounds) args[rowBoundsIndex] : null);
	}

	public boolean hasResultHandler() {
		return (resultHandlerIndex != null);
	}

	public ResultHandler extractResultHandler(Object[] args) {
		return (hasResultHandler() ? (ResultHandler) args[resultHandlerIndex] : null);
	}

	public String getMapKey() {
		return mapKey;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public boolean returnsMany() {
		return returnsMany;
	}

	public boolean returnsMap() {
		return returnsMap;
	}

	public boolean returnsVoid() {
		return returnsVoid;
	}

	private Integer getUniqueParamIndex(Method method, Class<?> paramType) {
		Integer index = null;
		final Class<?>[] argTypes = method.getParameterTypes();
		for (int i = 0; i < argTypes.length; i++) {
			if (paramType.isAssignableFrom(argTypes[i])) {
				if (index == null) {
					index = i;
				} else {
					throw new BindingException(method.getName() + " cannot have multiple " + paramType.getSimpleName() + " parameters");
				}
			}
		}
		return index;
	}

	private String getMapKey(Method method) {
		String mapKey = null;
		if (Map.class.isAssignableFrom(method.getReturnType())) {
			final MapKey mapKeyAnnotation = method.getAnnotation(MapKey.class);
			if (mapKeyAnnotation != null) {
				mapKey = mapKeyAnnotation.value();
			}
		}
		return mapKey;
	}

	private SortedMap<Integer, String> getParams(Method method, boolean hasNamedParameters) {
		final SortedMap<Integer, String> params = new TreeMap<Integer, String>();
		final Class<?>[] argTypes = method.getParameterTypes();
		for (int i = 0; i < argTypes.length; i++) {
			if (!RowBounds.class.isAssignableFrom(argTypes[i]) && !ResultHandler.class.isAssignableFrom(argTypes[i])) {
				String paramName = String.valueOf(params.size());
				if (hasNamedParameters) {
					paramName = getParamNameFromAnnotation(method, i, paramName);
				}
				params.put(i, paramName);
			}
		}
		return params;
	}

	private String getParamNameFromAnnotation(Method method, int i, String paramName) {
		final Object[] paramAnnos = method.getParameterAnnotations()[i];
		for (Object paramAnno : paramAnnos) {
			if (paramAnno instanceof Param) {
				paramName = ((Param) paramAnno).value();
			}
		}
		return paramName;
	}

	private boolean hasNamedParams(Method method) {
		boolean hasNamedParams = false;
		final Object[][] paramAnnos = method.getParameterAnnotations();
		for (Object[] paramAnno : paramAnnos) {
			for (Object aParamAnno : paramAnno) {
				if (aParamAnno instanceof Param) {
					hasNamedParams = true;
					break;
				}
			}
		}
		return hasNamedParams;
	}

}