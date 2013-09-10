package framework.generic.mybatis;

import static org.springframework.util.Assert.notNull;

import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.StringUtils;

import framework.generic.executor.Executor;
import framework.generic.executor.ExecutorScannerConfigurer;

public class MapperScannerConfigurer extends ExecutorScannerConfigurer {

	private String basePackage;

	private boolean addToConfig = true;

	private SqlSessionFactory sqlSessionFactory;

	private SqlSessionTemplate sqlSessionTemplate;

	private String sqlSessionFactoryBeanName;

	private String sqlSessionTemplateBeanName;

	private String beanName;

	private boolean processPropertyPlaceHolders;

	private BeanNameGenerator nameGenerator;

	private Executor executor;

	public void setGenericDao(Executor executor) {
		setExecutor(executor);
	}

	public Executor getExecutor() {
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public void setAddToConfig(boolean addToConfig) {
		this.addToConfig = addToConfig;
	}

	@Deprecated
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public void setSqlSessionTemplateBeanName(String sqlSessionTemplateName) {
		this.sqlSessionTemplateBeanName = sqlSessionTemplateName;
	}

	@Deprecated
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public void setSqlSessionFactoryBeanName(String sqlSessionFactoryName) {
		this.sqlSessionFactoryBeanName = sqlSessionFactoryName;
	}

	public void setProcessPropertyPlaceHolders(boolean processPropertyPlaceHolders) {
		this.processPropertyPlaceHolders = processPropertyPlaceHolders;
	}

	public void setBeanName(String name) {
		this.beanName = name;
	}

	public BeanNameGenerator getNameGenerator() {
		return nameGenerator;
	}

	public void setNameGenerator(BeanNameGenerator nameGenerator) {
		this.nameGenerator = nameGenerator;
	}

	public void afterPropertiesSet() throws Exception {
		notNull(this.basePackage, "Property 'basePackage' is required");
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		// left intentionally blank
	}

	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		if (this.processPropertyPlaceHolders) {
			processPropertyPlaceHolders();
		}

		ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
		scanner.setAddToConfig(this.addToConfig);
		scanner.setAnnotationClass(this.annotationClass);
		scanner.setMarkerInterface(this.markerInterface);
		scanner.setSqlSessionFactory(this.sqlSessionFactory);
		scanner.setSqlSessionTemplate(this.sqlSessionTemplate);
		scanner.setSqlSessionFactoryBeanName(this.sqlSessionFactoryBeanName);
		scanner.setSqlSessionTemplateBeanName(this.sqlSessionTemplateBeanName);
		scanner.setResourceLoader(this.applicationContext);
		scanner.setBeanNameGenerator(this.nameGenerator);
		scanner.setExecutor(this.executor);
		scanner.registerFilters();
		scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
	}

	private void processPropertyPlaceHolders() {
		Map<String, PropertyResourceConfigurer> prcs = applicationContext.getBeansOfType(PropertyResourceConfigurer.class);

		if (!prcs.isEmpty() && applicationContext instanceof GenericApplicationContext) {
			BeanDefinition mapperScannerBean = ((GenericApplicationContext) applicationContext).getBeanFactory().getBeanDefinition(beanName);

			DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
			factory.registerBeanDefinition(beanName, mapperScannerBean);

			for (PropertyResourceConfigurer prc : prcs.values()) {
				prc.postProcessBeanFactory(factory);
			}

			PropertyValues values = mapperScannerBean.getPropertyValues();

			this.basePackage = updatePropertyValue("basePackage", values);
			this.sqlSessionFactoryBeanName = updatePropertyValue("sqlSessionFactoryBeanName", values);
			this.sqlSessionTemplateBeanName = updatePropertyValue("sqlSessionTemplateBeanName", values);
		}
	}

	private String updatePropertyValue(String propertyName, PropertyValues values) {
		PropertyValue property = values.getPropertyValue(propertyName);

		if (property == null) {
			return null;
		}

		Object value = property.getValue();

		if (value == null) {
			return null;
		} else if (value instanceof String) {
			return value.toString();
		} else if (value instanceof TypedStringValue) {
			return ((TypedStringValue) value).getValue();
		} else {
			return null;
		}
	}

}
