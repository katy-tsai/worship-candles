package tw.katy.com.enums;
/**
 * 系統參數代碼
 * @author Katy
 *
 */
public enum SystemCode {
	
	fCandlesAmt("財利燈價格"),
	wCandlesAmt("光明燈價格"),
	attn("經辦人") 
	;
	private String localName;
	
	private SystemCode(String localName){
		this.localName = localName;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}
	
	

}
