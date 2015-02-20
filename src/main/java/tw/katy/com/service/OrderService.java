package tw.katy.com.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import tw.katy.com.dao.GenericJDBCDao;
import tw.katy.com.dao.OrderDao;
import tw.katy.com.dao.impl.OrderDaoImpl;
import tw.katy.com.entity.BlessOrder;
import tw.katy.com.enums.SystemCode;
import tw.katy.com.util.ProxyUtils;

public class OrderService extends GenericService<BlessOrder> {
	
	private Integer fCandlesAmt;//財利燈
	
	private Integer wCandlesAmt;//光明燈
	
	private String attn;//經辦人
	
	private OrderDao dao =(OrderDao) ProxyUtils.getProxyDao(new OrderDaoImpl());
	
	protected Logger log = LoggerFactory.getLogger(OrderService.class);

	public OrderService(){
		SystemParamSerivce paramService = new SystemParamSerivce();
		String fCandlesAmtstr = paramService.getBylabel(SystemCode.fCandlesAmt);
		String wCandlesAmtstr = paramService.getBylabel(SystemCode.wCandlesAmt);
		attn = paramService.getBylabel(SystemCode.attn);
		fCandlesAmt = Integer.parseInt(fCandlesAmtstr);
		wCandlesAmt = Integer.parseInt(wCandlesAmtstr);
		
	}
	@Override
	public GenericJDBCDao<BlessOrder> getDao() {		
		return dao;
	}

	@Override
	public BlessOrder insert(BlessOrder entity) throws Exception {
		try {
			calculate(entity);
			entity.setAttn(attn);
			int result = getDao().insert(entity);
			if(result==0){
				throw new Exception("儲存失敗");
			}else{
				entity = getLastInsert();
			}
			
		} catch (SQLException e) {
			throw new Exception("儲存失敗");
		}
		
		return entity;
	}
	
	private void  calculate(BlessOrder entity){
		Integer totalAmt = 0;
		if(StringUtils.equals("Y", entity.getfCandlesFlag())){
			totalAmt += fCandlesAmt;
		}
		if(StringUtils.equals("Y",entity.getwCandlesFlag())){
			totalAmt += wCandlesAmt;
		}
		entity.setTotalAmt(totalAmt);
	}
	
	public BlessOrder getLastInsert(){
		BlessOrder result  = null;
		Integer id = dao.findByLastRowId();
		
		if(id!=null){
			result = getOne(id);
		}		
		return result;
	}

	@Override
	public BlessOrder update(BlessOrder entity) throws Exception {
		BlessOrder dbEntity = getOne(entity.getId());
		if (dbEntity == null) {
			throw new Exception("cannot update non-exist record");
		}
		
		BeanUtils.copyProperties(entity, dbEntity,getNullPropertyNames(entity, dbEntity));
		dbEntity.setModifyTime(LocalDateTime.now());
		dao.update(dbEntity);
		log.debug("{}", dbEntity);		
		return dbEntity;
	
	}
	
	public BlessOrder getByTel(String tel){
		BlessOrder result = null;
		List<BlessOrder> list = dao.findByTel(tel);
		if(list.size()>0){
			result = list.get(0);
		}		
		return result;
	}
	
	public List<BlessOrder> getByCondition(String tel,String name,String start,String end){
		DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd");
		String dateStar = "";
		String dateEnd = "";
		if(StringUtils.isNotEmpty(start)&&StringUtils.isNotEmpty(end)){
			LocalDate date1 = df.parseLocalDate(start);
			LocalDate date2 = df.parseLocalDate(end);
			if(date1.isAfter(date2)){
				dateStar = date2.toString("yyyy-MM-dd");
				dateEnd = date1.toString("yyyy-MM-dd");
			}else{
				dateEnd = date2.toString("yyyy-MM-dd");
				dateStar = date1.toString("yyyy-MM-dd");
			}			
		}
		
		List<BlessOrder> result = dao.findByCondition(tel,name,dateStar,dateEnd);
		return result;
	}
	
	

}
