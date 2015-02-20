package tw.katy.com.dao;

import java.util.List;

import tw.katy.com.annotation.Query;
import tw.katy.com.entity.SystemParam;
/**
 * 系統參數
 * @author Katy
 *
 */
public interface SystemParamDao extends GenericJDBCDao<SystemParam> {

	@Query("select * from system_param where id=?")
	public List<SystemParam> findById(Integer id);
	
	@Query("select * from system_param")
	public List<SystemParam> findAll();
	
	@Query("select * from system_param where label=? order by id desc")
	public List<SystemParam> findByLabel(String label);



	
}
