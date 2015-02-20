package tw.katy.com.dao;

import java.util.List;

import tw.katy.com.annotation.Query;
import tw.katy.com.entity.BlessOrder;
/**
 * 
 * @author Katy
 *
 */
public interface OrderDao extends GenericJDBCDao<BlessOrder> {

	@Query("select * from bless_order where id=?")
	public List<BlessOrder> findById(Integer id);
	
	@Query("select * from bless_order order by create_Time desc")
	public List<BlessOrder> findAll();
	
	@Query("select * from bless_order where tel=? order by id desc")
	public List<BlessOrder> findByTel(String tel);
	
	public List<BlessOrder> findByCondition(String tel,String name,String dateStar,String dateEnd);


	
}
