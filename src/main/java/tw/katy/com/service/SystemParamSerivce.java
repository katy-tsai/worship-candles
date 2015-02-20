package tw.katy.com.service;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import tw.katy.com.dao.GenericJDBCDao;
import tw.katy.com.dao.SystemParamDao;
import tw.katy.com.dao.impl.SystemParamDaoImpl;
import tw.katy.com.entity.SystemParam;
import tw.katy.com.enums.SystemCode;
import tw.katy.com.util.ProxyUtils;

public class SystemParamSerivce extends GenericService<SystemParam> {
	
	private SystemParamDao dao =(SystemParamDao) ProxyUtils.getProxyDao(new SystemParamDaoImpl());
	
	protected Logger log = LoggerFactory.getLogger(SystemParamSerivce.class);


	@Override
	public GenericJDBCDao<SystemParam> getDao() {
		return dao;
	}

	@Override
	public SystemParam insert(SystemParam entity) throws Exception {
		try {
			
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

	@Override
	public SystemParam update(SystemParam entity) throws Exception {
		SystemParam dbEntity = getBySystemCode(entity.getLabel());
		if (dbEntity == null) {
			throw new Exception("cannot update non-exist record");
		}
		
		BeanUtils.copyProperties(entity, dbEntity,getNullPropertyNames(entity, dbEntity));
		dao.update(dbEntity);
		log.debug("{}", dbEntity);
		return dbEntity;
	}
	
	public SystemParam getLastInsert(){
		SystemParam result  = null;
		Integer id = dao.findByLastRowId();
		
		if(id!=null){
			result = getOne(id);
		}		
		return result;
	}
	
	public String getBylabel(SystemCode code){
		String result = "";
		List<SystemParam> list = dao.findByLabel(code.getLocalName());
		if(list.size()>0){
			SystemParam param = list.get(0);
			result = param.getValue();
		}		
		return result;
	}
	
	public SystemParam getBySystemCode(String code){
		SystemParam result = null;
		List<SystemParam> list = dao.findByLabel(code);
		if(list.size()>0){
			result = list.get(0);
			
		}		
		return result;
	}

}
