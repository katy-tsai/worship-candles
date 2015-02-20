package tw.katy.com.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.katy.com.annotation.Query;
import tw.katy.com.dao.impl.GenericJDBCDaoImpl;

import com.google.common.collect.Lists;


/**
 * 動態代理 dao
 * 
 * @author katy
 * 
 *     1.須implements InvocationHandler
 * 
 */
@SuppressWarnings("rawtypes")
public class DynamicDaoInvocationHandler  implements InvocationHandler {

	private Object sub;
	
	private Logger log = LoggerFactory.getLogger(DynamicDaoInvocationHandler.class);

	public DynamicDaoInvocationHandler(Object obj) {
		this.sub = obj;
	}

	
	 

	@SuppressWarnings("unused")
	protected String getQuerySql(String menothName, Method method) {
		String result = "";
		
		try {
			Class daoClass = sub.getClass();
			log.debug("{}",menothName);
			
			if (method.isAnnotationPresent(Query.class)) {
				Query query = method.getAnnotation(Query.class);
				result = query.value();
			}
		}  catch (SecurityException e) {
			log.debug("{} query sql錯誤:: SecurityException", menothName);
		}

		return result;
	}


	@SuppressWarnings("unchecked")
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = null;
		String querySql = getQuerySql(method.getName(),method);
		if(StringUtils.isNotEmpty(querySql)){
			Class daoClass = GenericJDBCDaoImpl.class;
			List arg = Lists.newArrayList();
			if(args!=null){
				 arg = Lists.newArrayList(args);
			}
			method = daoClass.getDeclaredMethod("jdbcSqlExecuteQuery", String.class,List.class);
			result = method.invoke(sub,querySql, arg);
		}else{
			result = method.invoke(sub,args);
		}
			
		return result;
	}


}
