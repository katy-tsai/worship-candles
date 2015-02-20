package tw.katy.com.util;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
/**
 * 大寫金額
 * @author Katy
 *
 */
public class MoneyUtil {

	private static Map<Integer, String> moneyWords;
	private static Map<Integer, String> digitsWords;

	static {
		moneyWords = Maps.newHashMap();
		moneyWords.put(0, "零");
		moneyWords.put(1, "壹");
		moneyWords.put(2, "貳");
		moneyWords.put(3, "參");
		moneyWords.put(4, "肆");
		moneyWords.put(5, "伍");
		moneyWords.put(6, "陸");
		moneyWords.put(7, "柒");
		moneyWords.put(8, "捌");
		moneyWords.put(9, "玖");
		digitsWords = Maps.newHashMap();
		digitsWords.put(1, "");
		digitsWords.put(2, "拾");
		digitsWords.put(3, "佰");
		digitsWords.put(4, "仟");
		digitsWords.put(5, "萬");
		digitsWords.put(6, "拾");
	}
	
	/**
	 * 轉大寫金額
	 * @return
	 */
	public static String converBigDollor(Integer amt){
		String result = "零元整";
		if(amt!=null){
			String amount = amt.toString();
			char[] moneys = amount.toCharArray();
			result = convertBigNumber(moneys);			
		}		
		return result;
	}
	
	public static String converBigDollorForPrint(Integer amt){
		String result = "零";
		if(amt!=null){
			String amount = amt.toString();
			char[] moneys = amount.toCharArray();
			result = convertBigNumberForPrint(moneys);			
		}		
		return result;
	}

	public static String convertBigNumber(char[] moneys) {
		StringBuffer bf = new StringBuffer();
		int i = moneys.length;
		for (char m : moneys) {
			int money = Character.getNumericValue(m);
			bf.append(getDigitsDollar(i,money,moneys));			
			i--;
		}
		if(StringUtils.isNotEmpty(bf)){
			bf.append("元整");
		}
		return bf.toString();
	}
	
	public static String convertBigNumberForPrint(char[] moneys) {
		StringBuffer bf = new StringBuffer();
		int i = moneys.length;
		for (char m : moneys) {
			int money = Character.getNumericValue(m);
			bf.append(getDigitsDollar(i,money,moneys));			
			i--;
		}
		
		return bf.toString();
	}

	public static String getDigitsDollar(int digits, int money,char[] moneys) {
		StringBuffer bf = new StringBuffer();
		String number = moneyWords.get(money);
		String unit = digitsWords.get(digits);		
		if(StringUtils.equals(number, "零")){
			if(StringUtils.equals(unit, "萬")){
				bf.append(unit);
			}else{
				int nextDigits = moneys.length-(digits);
				int nextMoney =Character.getNumericValue(moneys[nextDigits]);
				if(nextMoney!=0){
					bf.append(number).append(unit);
				}
			}			
		}else{
			bf.append(number).append(unit);
		}
		return bf.toString();
	}



}
