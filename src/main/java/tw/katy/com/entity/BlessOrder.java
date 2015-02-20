package tw.katy.com.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import tw.katy.com.annotation.ChinesClumnName;

/**
 * 點燈祈福簽填單據
 * 
 * @author Katy
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "bless_order")
public class BlessOrder implements Serializable {

	@Id
	@ChinesClumnName("系統編號")
	@Column(name = "id")
	private Integer id;

	/** 姓名 */
	@ChinesClumnName("姓        名")
	@Column(name = "name", length = 20)
	private String name;

	/** 連絡電話 */
	@ChinesClumnName("連絡電話")
	@Column(name = "tel", length = 20)
	private String tel;

	/** 住址 */
	@ChinesClumnName("住        址")
	@Column(name = "address", length = 100)
	private String address;

	/** 公司行號 */
	@ChinesClumnName("公司行號")
	@Column(name = "company_name", length = 50)
	private String companyName;

	/** 輸入日期 */
	
	@ChinesClumnName("輸入日期")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	@Column(name = "create_Time")
	private LocalDateTime createTime;

	/** 修改日期 */
	@Column(name = "modify_time")
	@ChinesClumnName("修改日期")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime modifyTime;

	/** 是否點光明燈 */
	@ChinesClumnName("光明燈")
	@Column(name = "w_candles_flag")
	private String wCandlesFlag;

	/** 是否點財利燈 */
	@ChinesClumnName("財利燈")
	@Column(name = "f_candles_flag")
	private String fCandlesFlag;

	/** 總金額 */
	@ChinesClumnName("金額")
	@Column(name = "total_amt")
	private Integer totalAmt;

	/** 經辦人 */
	@ChinesClumnName("經辦人")
	@Column(name = "attn")
	private String attn;

	public BlessOrder() {
		super();
	}

	public BlessOrder(String name, String tel, String address,
			String companyName, LocalDateTime createTime,
			LocalDateTime modifyTime) {
		super();
		this.name = name;
		this.tel = tel;
		this.address = address;
		this.companyName = companyName;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getwCandlesFlag() {
		return wCandlesFlag;
	}

	public void setwCandlesFlag(String wCandlesFlag) {
		this.wCandlesFlag = wCandlesFlag;
	}

	public String getfCandlesFlag() {
		return fCandlesFlag;
	}

	public void setfCandlesFlag(String fCandlesFlag) {
		this.fCandlesFlag = fCandlesFlag;
	}

	public Integer getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(Integer totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(LocalDateTime modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getAttn() {
		return attn;
	}

	public void setAttn(String attn) {
		this.attn = attn;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.DEFAULT_STYLE);
	}

}
