package tw.katy.com.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.katy.com.dao.GenericJDBCDao;
import tw.katy.com.entity.DBHelper;
import tw.katy.com.entity.SqlEntity;
import tw.katy.com.util.FiledUtil;

import com.google.common.collect.Maps;
/**
 * GenericJDBCDAO FOR sqlite
 * @author Katy
 *
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public class GenericJDBCDaoImpl<T extends Serializable>  implements GenericJDBCDao<T>  {

	protected Class<T> entityClass;

	protected Logger log = LoggerFactory.getLogger(GenericJDBCDaoImpl.class);
	
	private static String SQL_UPDATE_WHERE = "where id = ?" ;
	private static String SQL_INSERT ="insert into ";
	private static String SQL_UPDATE ="update ";
	
	@SuppressWarnings({ "unchecked"})
	public GenericJDBCDaoImpl(){
		Type type = this.getClass().getGenericSuperclass();
		Type[] arguments = ((ParameterizedType)type).getActualTypeArguments();
		entityClass = (Class)arguments[0];
	}
	
	public SqlEntity generateUpdateSql(T entity){
		SqlEntity result = new SqlEntity();
		Class className = entityClass;
    	Map<Integer,Object> args = Maps.newHashMap();    	
    	StringBuffer sql = new StringBuffer();
    	StringBuffer updateBf = new StringBuffer();
    	List<Field> classFields = FiledUtil.getClassAllField(className);
    	Object idValue = null;
    	Integer i = 1;
    	for(Field f :classFields ){
    		String fileName = f.getName();
    		try {
				if(!f.isAnnotationPresent(Id.class)){
					Column annotionField = f.getAnnotation(Column.class);						
					String cloudName = annotionField.name();
					updateBf.append(cloudName).append(" = ?").append(",");
					log.debug("fileName={}",fileName);
					Object value = PropertyUtils.getProperty(entity, fileName);
					String argValue =  parseString(value);					
					args.put(i, argValue);
					i++;
				}else{					
					idValue = PropertyUtils.getProperty(entity, fileName);
				}
			} catch (IllegalAccessException e) {
				log.debug("{} generateUpdateSql 錯誤:: IllegalAccessException",fileName);
			} catch (InvocationTargetException e) {
				log.debug("{} generateUpdateSql 錯誤:: InvocationTargetException",fileName);
			} catch (NoSuchMethodException e) {
				log.debug("{} generateUpdateSql 錯誤:: NoSuchMethodException",fileName);
			}	
    	}
    	
    	String tableName = DBHelper.getTableName(entityClass);
    	sql.append(SQL_UPDATE).append(tableName).append(" set ");
    	if(StringUtils.isNotEmpty(updateBf)){
    		sql.append(updateBf.toString().substring(0, updateBf.length()-1)) ;
    	}
    	sql.append(SQL_UPDATE_WHERE);
    	args.put(i, idValue);
    	result.setSql(sql.toString());
    	result.setArgs(args);
   	
		return result;
	}
    
    /**
     * 產生insert sql
     * @param entity
     * @return
     */
   
    public  SqlEntity generateInsertSql(T entity){
    	SqlEntity result = new SqlEntity();
    	Class className = entityClass;
    	Map<Integer,Object> args = Maps.newHashMap();    	
    	StringBuffer sql = new StringBuffer();
    	StringBuffer insetBf = new StringBuffer();
    	StringBuffer valuesBf = new StringBuffer();
    	List<Field> classFields = FiledUtil.getClassAllField(className);
    	Integer i = 1;
    	for(Field f :classFields ){
    		String fileName = f.getName();
    		if(!f.isAnnotationPresent(Id.class)){
    			try {
					if (f.isAnnotationPresent(Column.class)) {
						Column annotionField = f.getAnnotation(Column.class);						
						String cloudName = annotionField.name();
						insetBf.append(cloudName).append(",");
						valuesBf.append("?").append(",");
						log.debug("fileName={}",fileName);
						Object value = PropertyUtils.getProperty(entity, fileName);
						String argValue =  parseString(value);
						
						args.put(i, argValue);
						i++;
						
					}
				} catch (IllegalAccessException e) {
					log.debug("{} generateInsertSql 錯誤:: IllegalAccessException",fileName);
				} catch (InvocationTargetException e) {
					log.debug("{} generateInsertSql 錯誤:: InvocationTargetException",fileName);
				} catch (NoSuchMethodException e) {
					log.debug("{} generateInsertSql 錯誤:: NoSuchMethodException",fileName);
				}    	
			}   			
    	}
    	//組合insert sql
    	String tableName = DBHelper.getTableName(entityClass);
    	sql.append(SQL_INSERT).append(tableName).append(" ");
    	if(StringUtils.isNotEmpty(insetBf)){
    		sql.append("("+insetBf.toString().substring(0, insetBf.length()-1)+")") ;
    	}
    	if(StringUtils.isNotEmpty(insetBf)){
    		sql.append("Values ("+valuesBf.toString().substring(0, valuesBf.length()-1)+");") ;
    	}
    	result.setSql(sql.toString());
    	result.setArgs(args);
    	return result;
    }
    /**
     * 將型別轉為String
     * @param value
     * @return
     */
    public String parseString(Object value){
		String result = null;
		if(value !=null){
			if(value instanceof String){
				result = (String) value;
			}else if(value instanceof LocalDate){
				result = ((LocalDate) value).toString("yyyy-MM-dd");
			}else if(value instanceof LocalDateTime){
				result = ((LocalDateTime) value).toString("yyyy-MM-dd kk:mm:ss");
			}else if(value instanceof Integer){				
				result = value.toString();
			}
			
		}		
		return result;
	}
   
    /**
     * jdbc 連線Connection ,  Statement
     * @param sql
     * @return
     */
    public int jdbcSqlExuteUpdate(SqlEntity sqlEntity){
    	String sql = sqlEntity.getSql();
    	Map<Integer,Object> args = sqlEntity.getArgs();
		PreparedStatement pst = null;
		Connection conn = null;	
		int result = 0;
		try {
			conn = DBHelper.getConnection();
			pst = conn.prepareStatement(sql);
			for(Integer s :args.keySet()){
				pst.setObject(s, args.get(s));
			}

			result = pst.executeUpdate();
			
		}  catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
    }
	/**
	 * jdbc 連線Connection ,  PreparedStatement , ResultSet
	 * @param className
	 * @param sql
	 * @param arg
	 * @return
	 */

	public List<T> jdbcSqlExecuteQuery(String sql,List<Object> args){
		List<T> result = new ArrayList<T>();
		ResultSet res = null;
		PreparedStatement stmt = null;
		Connection conn = null;	
		try {
			conn = DBHelper.getConnection();
			stmt = conn.prepareStatement(sql);
			if(args!=null && args.size()!=0){
				int i = 1;
				for(Object arg:args){
					stmt.setObject(i, arg);
					i++;
				}				
			}
			res = stmt.executeQuery();
			T entity;
			while (res.next()) {
				entity = (T) entityClass.newInstance();
				entity = setValus(entity, res);
				result.add(entity);
			}

		}  catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (res != null) {
				try {
					res.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
		return result;
	}
	
	/**
	 * resultSet 取值設定到entity
	 * @param entity
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public T setValus(T entity, ResultSet res) {
		Field[] classFields = entity.getClass().getDeclaredFields();
		Field collectionField = null;
		
		for (Field f : classFields) {
			String fileName=f.getName();
			Long id =0L;
			try {
				if (f.isAnnotationPresent(Column.class)) {
					Column annotionField = f.getAnnotation(Column.class);
					String columnName = annotionField.name();
					Type fieldType = f.getType();
					Object value = res.getObject(columnName);
					value=convertValue(value, fieldType);
					log.debug("columnName={}",columnName);
					log.debug("value={}",value);
					
					if(value!=null){
						log.debug("type={}",value.getClass());
						PropertyUtils.setProperty(entity, fileName, value);
					}	
				}
			} catch (IllegalAccessException e) {
			
				log.debug("欄位名:{}轉換錯誤:: IllegalAccessException",f.getName());
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				log.debug("欄位名:{}轉換錯誤:: InvocationTargetException",f.getName());
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				log.debug("欄位名:{}轉換錯誤:: NoSuchMethodException",f.getName());
				e.printStackTrace();
			} catch (SQLException e) {
				log.debug("欄位名:{}轉換錯誤:: SQLException",f.getName());
			}    
			
			
		}
		
		return entity;
	}
	
	private Object convertValue(Object value,Type fieldType){
		if(fieldType.equals(LocalDateTime.class)){
			DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd kk:mm:ss");
			value =  df.parseLocalDateTime((String) value);
		}else if(fieldType.equals(LocalDate.class)){
			DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd");
			value =  df.parseLocalDate((String) value);
		}
		return value;
	}


	/* (non-Javadoc)
	 * @see tw.katy.com.dao.GenericJDBCDao#insert(T)
	 */
	public int insert(T entity)throws SQLException{        
        SqlEntity sqlEntity = generateInsertSql(entity);
        log.debug("sql={}",sqlEntity.getSql());
        log.debug("args={}",sqlEntity.getArgs());
        int result = jdbcSqlExuteUpdate(sqlEntity);
        return result;
       
    }
    /* (non-Javadoc)
	 * @see tw.katy.com.dao.GenericJDBCDao#update(T)
	 */
    public int update(T entity){
    	 SqlEntity sqlEntity =generateUpdateSql(entity);
    	 log.debug("sql={}",sqlEntity.getSql());
         log.debug("args={}",sqlEntity.getArgs());
         int result = jdbcSqlExuteUpdate(sqlEntity);
		return result;
	}
	
    /* (non-Javadoc)
	 * @see tw.katy.com.dao.GenericJDBCDao#delete(java.lang.Integer)
	 */
	public int delete(Integer id){
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see tw.katy.com.dao.GenericJDBCDao#findById(java.lang.Integer)
	 */
	public List<T> findById(Integer id){
		
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see tw.katy.com.dao.GenericJDBCDao#findByCondition(T)
	 */
	public List<T> findByCondition(T entity){
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see tw.katy.com.dao.GenericJDBCDao#findAll()
	 */
	public List<T> findAll(){
		
		return null;
	}
	
	public Integer findByLastRowId() {
		Integer result = null;
    	String tableName = DBHelper.getTableName(entityClass);
    	String sql = "Select max(id) from "+tableName;
    	ResultSet res = null;
		PreparedStatement stmt = null;
		Connection conn = null;	
		try {
			conn = DBHelper.getConnection();
			stmt = conn.prepareStatement(sql);
			
			res = stmt.executeQuery();
			while (res.next()) {
				result = res.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
