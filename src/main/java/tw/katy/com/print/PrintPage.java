package tw.katy.com.print;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.katy.com.entity.BlessOrder;
import tw.katy.com.service.OrderService;
import tw.katy.com.util.MoneyUtil;

import com.google.common.collect.Lists;

/**
 * 列印頁
 * @author katy
 *
 */
public class PrintPage implements Printable {
	
	protected Logger log = LoggerFactory.getLogger(PrintPage.class);

	ImageIcon printImage = new javax.swing.ImageIcon(getClass().getClassLoader().getResource("cdr.jpg"));
	
	Font font = new Font(Font.SERIF, Font.BOLD, 15);
	
	Font smallfont = new Font(Font.SERIF, Font.BOLD, 12);
	
	Font moneyFont = new Font(Font.MONOSPACED, Font.BOLD, 20);
	
	Font noFont = new Font(Font.SERIF, Font.PLAIN, 10);
	
	private BlessOrder order;
	
	private boolean isPrintImage = false;
	
	private static final int NAME_X0 = 470;
	
	private static final int NAME_X1 = 460;
	
	private static final int NAME_X2 = 480;
	
	private static final int NAME_Y0 = 140;
	
	private static final int NAME_MAX_HIGHT = 310;
	
	private static final int AMT_X0 = 385;
	
	private static final int AMT_Y0 =180;
	
	private static final int AMT_MAX_HIGHT = 310;
	
	private static final int ADDRESS_X0 = 275;
	
	private static final int ADDRESS_Y0 = 140;
	
	private static final int ADDRESS_MAX_HIGHT = 360;
	
	private static final int TEL_X0 = 237;
	
	private static final int TEL_Y0 = 140;
	
	private static final int TEL_MAX_HIGHT = 360;
	
	private static final int ATTN_X0 = 195;
	
	private static final int ATTN_Y0 = 225;
	
	private static final int ATTN_MAX_HIGHT = 360;
	
	private static final int DATE_X0 = 100;
	
	private static final int DATE_Y0 = 170;
	
	private static final int DATE_Y1 = 240;
	
	private static final int DATE_Y2 = 310;
	
	private static final int DATE_MAX_HIGHT = 360;
	
	private static final int NO_X0 =500;
	
	private static final int NO_Y0 =400;
	
	
	public PrintPage(BlessOrder order){
		this.order =order;
	}
	
	public PrintPage(BlessOrder order,boolean isPrintImage){
		this.order =order;
		this.isPrintImage = isPrintImage;
	}
	
	public PrintPage(){
	}

	/*public static void main(String[] args) {
		
		
		OrderService service = new OrderService();
		BlessOrder order = service.getOne(7);
		PrinterJob pjob = PrinterJob.getPrinterJob();
		if (pjob.printDialog() == false)
			return;
		pjob.setPrintable(new PrintPage(order,true));
		try {
			pjob.print();
		} catch (PrinterException e) {
			e.printStackTrace();
		}
	}
*/
	public int print(Graphics g, PageFormat pf, int pageIndex)
			throws PrinterException {
		
		g.translate((int) (pf.getImageableX()), (int) (pf.getImageableY()));		
		if (pageIndex == 0) {
			if(isPrintImage){
				printImage(g, pf);
			}			
			g.setFont(font);
			//姓名
			StringBuffer name = new StringBuffer();
			if(StringUtils.isNotEmpty(order.getCompanyName())){
				name.append(order.getCompanyName());
			}
			if(StringUtils.isNotEmpty(order.getCompanyName())&&StringUtils.isNotEmpty(order.getName())){
				name.append(",");
			}
			if(StringUtils.isNotEmpty(order.getName())){
				name.append(order.getName());
			}
			String[] names =name.toString().split(",");
			if(names.length>3){
				if(names.length>6){
					StringBuffer bf =new StringBuffer();
					int i =0;
					for(String n:names){
						if(i<8){
							bf.append(n).append(",");
							i++;
						}						
					}
					String shortName = bf.toString().substring(0, bf.length()-1)+" 等";
					printText(g,shortName, NAME_X2, NAME_Y0,NAME_MAX_HIGHT,0);
				}else{
					printText(g,name.toString(), NAME_X0, NAME_Y0,NAME_MAX_HIGHT,0);
				}
				
			}else{
				printText(g,name.toString(), NAME_X1, NAME_Y0,NAME_MAX_HIGHT,0);
			}
			//住址
			printText(g,order.getAddress(), ADDRESS_X0, ADDRESS_Y0,ADDRESS_MAX_HIGHT,0);
			//電話
			printNumber(g, order.getTel(), TEL_X0, TEL_Y0, TEL_MAX_HIGHT, 0);
			//經手人
			printText(g, order.getAttn(), ATTN_X0, ATTN_Y0, ATTN_MAX_HIGHT, 0);
			//日期
			LocalDateTime datetime = order.getCreateTime();
			Integer year = datetime.getYear()-1911;
			printText(g,year.toString(), DATE_X0-5, DATE_Y0,DATE_MAX_HIGHT,10);
			Integer month = datetime.getMonthOfYear();
			printText(g,month.toString(), DATE_X0, DATE_Y1,DATE_MAX_HIGHT,10);
			Integer date = datetime.getDayOfMonth();
			printText(g,date.toString(), DATE_X0, DATE_Y2,DATE_MAX_HIGHT,10);
			//系統編號
			g.setFont(noFont);
			g.drawString("NO："+order.getId(), NO_X0, NO_Y0);
			
			//金額
			g.setFont(moneyFont);
			String amt = MoneyUtil.converBigDollorForPrint(order.getTotalAmt());
			printText(g,amt, AMT_X0, AMT_Y0,AMT_MAX_HIGHT,10);
			
			
			return Printable.PAGE_EXISTS;
		}
		return Printable.NO_SUCH_PAGE;
	}
	

	
	private void printText(Graphics g,String str,int x0,int y0,int maxHight,int hightsapce){		
		List<String> strs =convertStringArray(str);
		int y1=y0;
		for(String s:strs){	
			
			g.drawString(s, x0, y1);
			y1+=font.getSize()+hightsapce;
			if(y1>maxHight){
				x0-=font.getSize()+5;
				y1 = y0;
			}
		}
		
	}
	
	private void printNumber(Graphics g,String str,int x0,int y0,int maxHight,int hightsapce){
		char[] chars = str.toCharArray();
		int y1=y0;
		for(char c:chars){	
			
			g.drawString(String.valueOf(c), x0, y1);
			y1+=font.getSize()+hightsapce;
			if(y1>maxHight){
				x0-=font.getSize();
				y1 = y0;
			}
		}
	}
	
	public List<String> convertStringArray(String str){
		List<String> strs = Lists.newArrayList();
		char[] chars = str.toCharArray();
		String number = "";
		for(char c:chars){			
			if(Character.isDigit(c)){
				number=number+c;
			}else{
				if(StringUtils.isNotEmpty(number)){
					strs.add(number);
					number = "";
				}
				strs.add(String.valueOf(c));
			}
		}
		
		if(StringUtils.isNotEmpty(number)){
			strs.add(number);
			number = "";
		}
		
		return strs;
		
	}
	
	
	
	/**
	 * 底圖
	 * @param g
	 * @param pf
	 */
	private void printImage(Graphics g,PageFormat pf){
		Graphics2D g2d = (Graphics2D) g;
		double pageWidth = pf.getImageableWidth();
		double pageHeight = pf.getImageableHeight();
		double imageWidth = printImage.getIconWidth();
		double imageHeight = printImage.getIconHeight();
		double scaleX = pageWidth / imageWidth;
		double scaleY = pageHeight / imageHeight;
		double scaleFactor = Math.min(scaleX, scaleY);
		g2d.scale(scaleFactor, scaleFactor);
		g.drawImage(printImage.getImage(), 0, 0, null);
		//g.drawImage(printImage.getImage(), 0, 400, null);
	}

}
