package tw.katy.com.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import tw.katy.com.annotation.ChinesClumnName;

/**
 * 系統參數
 * @author Katy
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "system_param")
public class SystemParam  implements Serializable{
	
	@Id
	@Column(name = "id")
	private Integer id;
	
	/**名稱*/
	@ChinesClumnName("名        稱")
	@Column(name = "label", length = 20)
	private String label;
	
	/**值*/
	@Column(name = "value", length = 20)
	private String value;	

	public SystemParam() {
	}

	public SystemParam(String label, String value) {
		super();
		this.label = label;
		this.value = value;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	

}
