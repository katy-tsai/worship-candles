package tw.katy.com.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tw.katy.com.dao.OrderDao;
import tw.katy.com.entity.BlessOrder;

import com.google.common.collect.Lists;
/**
 * 點燈祈福簽填單據Dao
 * @author Katy
 *
 */
public class OrderDaoImpl extends GenericJDBCDaoImpl<BlessOrder> implements OrderDao{

	public List<BlessOrder> findByTel(String tel) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<BlessOrder> findByCondition(String tel, String name,
			String dateStar, String dateEnd) {
		List<Object> args = Lists.newArrayList();  
		StringBuffer sql = new StringBuffer();
		sql.append("Select * from bless_order ");
		
		String telQuery ="";
		if(StringUtils.isNotEmpty(tel)){
			telQuery = "tel like ?";
			args.add("%"+tel+"%");
		}
		
		String nameQuery = "";
		if(StringUtils.isNotEmpty(name)){
			nameQuery = "name like ?";
			args.add("%"+name+"%");
		}
		
		String str1 = appendString(telQuery, nameQuery);
		
		String dateQuery = "";
		if(StringUtils.isNotEmpty(dateStar)&&StringUtils.isNotEmpty(dateEnd)){
			dateStar =dateStar+" 00:00:00";
			dateEnd = dateEnd +" 23:59:59";
			dateQuery = " (create_Time between ? and ? )" ;

			args.add(dateStar);
			args.add(dateEnd);
		}
		
		String temp = appendString(str1, dateQuery);
		if(StringUtils.isNotEmpty(temp)){
			sql.append("where ").append(temp);
		}
		sql.append(" order by create_Time desc");
		log.debug("args={}",args);
		log.debug("sql={}",sql.toString());
		List<BlessOrder> result = jdbcSqlExecuteQuery(sql.toString(), args);
		
		return result;
	}
	
	private String appendString(String str1,String str2){
		StringBuffer bf = new StringBuffer();
		if(StringUtils.isNotEmpty(str1)){
			bf.append(str1);
		}
		if(StringUtils.isNotEmpty(str1)&&StringUtils.isNotEmpty(str2)){
			bf.append(" and ");
		}
		if(StringUtils.isNotEmpty(str2)){
			bf.append(str2);
		}
		return bf.toString();
	}
	

	

	
}
