package tw.katy.com.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * 
 * @author Katy
 *
 * @param <T>
 */
public interface GenericJDBCDao<T extends Serializable>  {
	
	
	public List<T> jdbcSqlExecuteQuery(String sql,List<Object> args);

	/**
	 * 新增	 
	 * @param entity
	 * @return
	 * @throws SQLException
	 */
	public abstract int insert(T entity) throws SQLException;

	/**
	 * 修改
	 * @param entity
	 * @return
	 */
	public abstract int update(T entity);

	/**
	 * 刪除
	 * @param id
	 * @return
	 */
	public abstract int delete(Integer id);

	/**
	 * find by id
	 * @param id
	 * @return
	 */
	public abstract List<T> findById(Integer id);
	

	/**
	 * find by codition
	 * @param entity
	 * @return
	 */
	public abstract List<T> findByCondition(T entity);

	/**
	 * find All
	 * @return
	 */
	public abstract List<T> findAll();
	
	public Integer findByLastRowId(); 
	
	

	

}