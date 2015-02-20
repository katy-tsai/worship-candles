package tw.katy.com.service;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import tw.katy.com.dao.GenericJDBCDao;


public abstract class GenericService<T extends Serializable> {

	public abstract  GenericJDBCDao<T>  getDao();

	public abstract T insert(T entity)throws Exception;
	
	public abstract T update(T entity)throws Exception;
	
	public T getOne(final Integer id) {
		T result = null;
		List<T> list = getDao().findById(id);
		if(list.size()>0){
			result = list.get(0);
		}		
		return result;
	}
	
	
	public List<T> getAll() {
		return getDao().findAll();
	}


	
	public void delete(final Integer id) {
		getDao().delete(id);

	}
	
	/**
	 * 取得排除欄位
	 * 
	 * @param source
	 * @param target
	 * @param isCopyPolicy
	 * @return
	 */
	protected static String[] getNullPropertyNames(final Object source, final Object target) {
		final Set<String> emptyNames = new HashSet<String>();
		// 1.來源資料為null不copy

		final BeanWrapper src = new BeanWrapperImpl(source);
		final java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
		for (final java.beans.PropertyDescriptor pd : pds) {
			final Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null) {
				emptyNames.add(pd.getName());
			}

		}

		final String[] result = new String[emptyNames.size()];
		
		return emptyNames.toArray(result);
	}
	
}
