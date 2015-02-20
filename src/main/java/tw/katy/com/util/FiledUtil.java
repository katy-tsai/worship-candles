package tw.katy.com.util;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
/**
 * 欄位util
 * @author Katy
 *
 */
public class FiledUtil {
	
	
	
	/**
	 * 依欄位名取得欄位(包括super 類別欄位)
	 * @param className
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Field getClassField(Class className,String fieldName){
		List<Field> fields = getClassAllField(className);
		for(Field f :fields){
			if(StringUtils.equals(f.getName(), fieldName)){
				return f;
			}
		}		
		return null;
	}
	
	/**
	 * 取得類別的所有欄位
	 * @param className
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<Field> getClassAllField(Class className){
		 List<Field> result=Lists.newArrayList();
		 while(!className.equals(Object.class)){
			 Field[] classFields=className.getDeclaredFields();
			 for(Field f:classFields){
				 result.add(f);
			 }
			 className=className.getSuperclass();
		 }
		 return result;
	}

}
