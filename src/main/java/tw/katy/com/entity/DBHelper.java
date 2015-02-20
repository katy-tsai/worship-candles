package tw.katy.com.entity;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import tw.katy.com.enums.SystemCode;
import tw.katy.com.service.SystemParamSerivce;
import tw.katy.com.util.FiledUtil;
/**
 * 資料庫連線 Helper for sqlite
 * @author Katy
 *
 */
public class DBHelper {

	private final static String DB_NAME = "data.db";

	private final static String PK = " PRIMARY KEY AUTOINCREMENT";

	
	/**
	 * 取得DB連線
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		SQLiteConfig config = new SQLiteConfig();
		// config.setReadOnly(true);
		config.setSharedCache(true);
		config.enableRecursiveTriggers(true);
		SQLiteDataSource ds = new SQLiteDataSource(config);
		ds.setUrl("jdbc:sqlite:" + DB_NAME);
		return ds.getConnection();

	}

	/**
	 * 建立table
	 * 
	 * @param con
	 * @param className
	 * @throws SQLException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void createTable(Connection con, Class className)
			throws SQLException {
		if (className.isAnnotationPresent(Table.class)) {
			Table annotationClass = (Table) className
					.getAnnotation(Table.class);
			String tableName = annotationClass.name();
			String sql = "create table " + tableName
					+ generateCreateSql(className) + ";";
			System.out.println(sql);
			Statement stat = null;
			stat = con.createStatement();
			stat.executeUpdate(sql);

		}

	}

	/**
	 * 取得table name
	 * 
	 * @param className
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getTableName(
			@SuppressWarnings("rawtypes") Class className) {
		String tableName = "";
		if (className.isAnnotationPresent(Table.class)) {
			Table annotationClass = (Table) className
					.getAnnotation(Table.class);
			tableName = annotationClass.name();
		}
		return tableName;
	}

	/**
	 * 產生create table sql
	 * 
	 * @param className
	 *            Table 所對應的Class
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String generateCreateSql(Class className) {
		String result = "";
		StringBuffer bf = new StringBuffer();

		List<Field> classFields = FiledUtil.getClassAllField(className);
		for (Field f : classFields) {

			if (f.isAnnotationPresent(Column.class)) {
				Column annotionField = f.getAnnotation(Column.class);
				String cloudName = annotionField.name();
				String type = getType(f.getType().getSimpleName());
				bf.append(cloudName).append(" ").append(type);
				if (f.isAnnotationPresent(Id.class)) {
					bf.append(" ").append(PK);
				}
				if (StringUtils.equals("nvarchar", type)) {
					int length = annotionField.length();
					bf.append("(").append(length).append(")");
				}
				bf.append(",");
			}
		}
		if (StringUtils.isNotEmpty(bf)) {
			result = "(" + bf.toString().substring(0, bf.length() - 1) + ")";
		}

		return result;
	}

	public static void main(String[] args) {
		try {
			Connection con = getConnection();
			createTable(con, BlessOrder.class);
			createTable(con, SystemParam.class);
			//insert 系統參數
			SystemParam param1 = new SystemParam(SystemCode.fCandlesAmt.getLocalName(), "600");
			SystemParam param2 = new SystemParam(SystemCode.wCandlesAmt.getLocalName(), "600");
			SystemParam param3 = new SystemParam(SystemCode.attn.getLocalName(),"經辦人");
		
			SystemParamSerivce service = new SystemParamSerivce();
			service.insert(param1);
			service.insert(param2);
			service.insert(param3);
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 型別轉換
	 * 
	 * @param typeName
	 *            型別名稱
	 * @return
	 */
	private static String getType(String typeName) {
		String result = "";
		if (StringUtils.equals("String", typeName)) {
			result = "nvarchar";
		} else if (StringUtils.equals("Integer", typeName)) {
			result = "INTEGER ";
		} else if (StringUtils.equals("LocalDateTime", typeName)) {
			result = "TEXT";
		}

		return result;
	}

}
