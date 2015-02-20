package tw.katy.com.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import tw.katy.com.dao.DynamicDaoInvocationHandler;
import tw.katy.com.dao.GenericJDBCDao;
import tw.katy.com.dao.impl.GenericJDBCDaoImpl;
/**
 * proxy util
 * @author Katy
 *
 */
public class ProxyUtils {

	@SuppressWarnings("rawtypes")
	public static GenericJDBCDao getProxyDao(GenericJDBCDaoImpl dao){
		
		InvocationHandler handler =  new DynamicDaoInvocationHandler(dao);
		
		Class<?> classType = dao.getClass();
		
		//生成動態代理		
		GenericJDBCDao subject = (GenericJDBCDao) Proxy.newProxyInstance(classType.getClassLoader(), classType.getInterfaces(), handler);
		
		return subject;
	}
}
