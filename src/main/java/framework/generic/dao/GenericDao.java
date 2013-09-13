package framework.generic.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;

import framework.generic.mybatis.paginator.domain.PageBounds;
import framework.generic.mybatis.paginator.domain.PageList;

/**
 * MyBatis的CRUD基接口类
 * <p>
 * framework-generic-mybatis-ext
 * </p>
 * <p>
 * 项目名称：2013-广州扬基信息科技有限公司
 * </p>
 * 
 * @see framework.generic.dao.GenericDao
 * @version 1.0, 2013-9-10 上午10:07:11
 * @author quanyongan
 */
public abstract interface GenericDao<T extends Model<PK>, PK extends Serializable> {

	public static final String EXECUTE_INSERT = "insert";
	public static final String EXECUTE_FIND_ALL = "find";
	public static final String EXECUTE_UPDATE = "update";
	public static final String EXECUTE_DELETE = "delete";
	public static final String EXECUTE_COUNT = "count";

	public abstract T insert(T entity);

	public abstract T findByPk(PK primaryKey);

	public abstract List<T> findAll();

	public abstract PageList<T> findByPage(Object object,PageBounds pageBounds);

	public abstract T update(T entity);

	public abstract int delete(T entity);

	public abstract int deleteByPk(PK primaryKey);

	/**
	 * /** 插入一个实体（在数据库INSERT一条记录）
	 * 
	 * @param entity
	 *            实体对象
	 */
	@InsertProvider(type = CurdTemplate.class, method = "getInsertSql")
	abstract Integer insertEntity(T entity);

	/**
	 * 修改一个实体对象（MODIFY一条记录）
	 * 
	 * @param entity
	 *            实体对象
	 * @return 修改的对象个数，正常情况=1
	 */
	@UpdateProvider(type = CurdTemplate.class, method = "getUpdateSql")
	abstract Integer updateEntity(T entity);

	/**
	 * 根据传入的对象删除记录
	 * 
	 * @param entity
	 *            实体对象
	 * @return 删除的对象个数，正常情况=1
	 */
	@DeleteProvider(type = CurdTemplate.class, method = "getDeleteSql")
	abstract Integer deleteEntity(T entity);
}