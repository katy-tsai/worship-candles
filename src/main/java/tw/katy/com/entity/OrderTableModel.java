package tw.katy.com.entity;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.katy.com.annotation.ChinesClumnName;

@SuppressWarnings("serial")
public class OrderTableModel extends DefaultTableModel{

	protected Logger log = LoggerFactory.getLogger(OrderTableModel.class);
	
	public OrderTableModel() {
		super();
	}
	
	
	public OrderTableModel(List<String> columnNameLists){
		this(columnNameLists,null);
	}

	public OrderTableModel(List<String> columnNameLists,List<BlessOrder> orders){
		setData(columnNameLists, orders);
	}
	
	public void setData(List<String> columnNameLists,List<BlessOrder> orders){
		Vector<String> columnNames = convertListToVector(columnNameLists);
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		for(BlessOrder order:orders){
			Vector<Object> vector = covertToVector(order,columnNameLists);
			data.add(vector);
		}
		 setDataVector(data, columnNames);
	}
	
	
	private Vector<Object> covertToVector(BlessOrder order,List<String> columnNameLists){
		Vector<Object> vector = new Vector<Object>();
		for(String fieldName:columnNameLists){			
			try {				
				Object value = PropertyUtils.getProperty(order, fieldName);
				if(StringUtils.equals(fieldName, "createTime")){
					
					value = ((LocalDateTime)value).toString("yyyy-MM-dd kk:mm:ss");
				}
				vector.add(value);
				
			} catch (IllegalAccessException e) {
				log.debug("欄位名:{}轉換錯誤:: IllegalAccessException", fieldName);
			} catch (InvocationTargetException e) {
				log.debug("欄位名:{}轉換錯誤:: InvocationTargetException", fieldName);
			} catch (NoSuchMethodException e) {
				log.debug("欄位名:{}轉換錯誤:: NoSuchMethodException", fieldName);
			}		
		}
					
		return vector;
	}
	
	private Vector<String> convertListToVector(List<String> columnNameLists){
		Vector<String> columnVector = new Vector<String>();
		for(String column:columnNameLists){		
			try {
				Field field = BlessOrder.class.getDeclaredField(column);
				if (field.isAnnotationPresent(ChinesClumnName.class)) {
					ChinesClumnName annotionField = field.getAnnotation(ChinesClumnName.class);
					columnVector.add(annotionField.value());					
				}
			} catch (NoSuchFieldException e) {
				log.debug("{} 轉換錯誤：NoSuchFieldException {} ", column,
						e.getMessage());
			} catch (SecurityException e) {
				log.debug("{} 轉換錯誤：SecurityException {} ", column,
						e.getMessage());
			}
	
		}
		return columnVector;
	}
	

	
}
