package tw.katy.com.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.katy.com.annotation.ChinesClumnName;
import tw.katy.com.entity.BlessOrder;
import tw.katy.com.print.PrintPage;
import tw.katy.com.service.OrderService;
import tw.katy.com.util.MoneyUtil;

import com.google.common.collect.Maps;
/**
 * 表單panel
 * @author Katy
 *
 */
@SuppressWarnings("serial")
public class OrderPanel extends JPanel{

	private Logger log = LoggerFactory.getLogger(OrderPanel.class);

	private String[] cloumNames = { "tel", "address", "companyName", "name" };
	
	private Map<String,EditTextField> textMap = Maps.newHashMap();
	
	private JCheckBox wCandlesCheckBox;
	
	private JCheckBox fCandlesCheckBox;
	
	private JLabel idMessagelabel;
	
	private JTextField idText = new JTextField();
	

	public OrderPanel() {
		// 空白邊緣
		this.setLayout(new BorderLayout());
		this.setName(" 表   單  ");
		this.setBorder(new EmptyBorder(20, 20, 20, 20));
		JPanel mainPanel = new JPanel();
		// 標題邊框
		TitledBorder title = new TitledBorder(new EtchedBorder(),
				" 點 燈 祈 福 簽 填 單  ");
		title.setTitleFont(new Font(Font.SERIF, Font.BOLD, 20));
		title.setTitleColor(Color.BLUE);
		mainPanel.setBorder(title);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		// 光明燈、財利燈
		JPanel checkRow = createCheckRow();
		mainPanel.add(checkRow);
		mainPanel.add(Box.createVerticalStrut(5));
		idText.setVisible(false);
		mainPanel.add(idText);
		// 欄位
		for (String cloumName : cloumNames) {
			JPanel row = createNewCloum(cloumName, BlessOrder.class);
			mainPanel.add(row);
			mainPanel.add(Box.createVerticalStrut(20));
		}
		//電話自動查詢
		addTelQueryListener();
		Box fixedcol = Box.createVerticalBox();
		fixedcol.add(Box.createVerticalStrut(12));
		fixedcol.add(mainPanel);
		fixedcol.add(Box.createVerticalStrut(200));

		Box fixedRow = Box.createHorizontalBox();
		fixedRow.add(Box.createHorizontalStrut(12));
		fixedRow.add(fixedcol);
		fixedRow.add(Box.createHorizontalStrut(200));

		this.add(fixedRow, BorderLayout.CENTER);

		// 下方按鈕
		ButtonBox btnRow = new ButtonBox();
		addListener(btnRow);
		this.add(btnRow, BorderLayout.SOUTH);
	}
	
	private void addTelQueryListener(){
		final JTextField telField = textMap.get("tel");
		telField.addFocusListener(new FocusListener () {					
			public void focusGained(FocusEvent e) {
			
			}
			public void focusLost(FocusEvent e) {
				String tel = telField.getText();
				OrderService orderService = new OrderService();
				BlessOrder order = orderService.getByTel(tel);
				if(order!=null){
					setValueToField(order);
				}
				
			}
		});
	}
	
	/**
	 * 設定事件監聽
	 * @param btnRow
	 */
	private void addListener(ButtonBox btnRow){
		
		
		//儲存
		btnRow.getSaveBtn().addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				saveBtnActionPerformed(e);
			}
		});
		
		//清除
		btnRow.getClearBtn().addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				clearBtnActionPerformed();
			}
		});
		//查詢
		btnRow.getQueryBtn().addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				queryBtnActionPerformed();
			}
		});
		//列印
		btnRow.getPrintBtn().addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				printBtnActionPerformed();
			}
		});
		
		//下一筆
		btnRow.getNextBtn().addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				clearBtnActionPerformed();
			}
		});
	}
	
	protected void printBtnActionPerformed() {
		String input = idText.getText();
		if(StringUtils.isEmpty(input)){
			input=JOptionPane.showInputDialog("請輸入系統編號：");
		}		
		if(StringUtils.isNotEmpty(input)){
			Integer id = Integer.parseInt(input);
			OrderService orderService = new OrderService();
			BlessOrder order = orderService.getOne(id);
			if(order!=null){
				PrinterJob pjob = PrinterJob.getPrinterJob();
				if (pjob.printDialog() == false)
					return;
				pjob.setPrintable(new PrintPage(order,true));
				try {
					pjob.print();
				} catch (PrinterException e) {
					e.printStackTrace();
				}

			}else{
				JOptionPane.showMessageDialog(null, "系統編號:"+input+",查無資料");
			}
		}else{
			JOptionPane.showMessageDialog(null, "未輸入系統編號");
		}
	
		
	}

	private void queryBtnActionPerformed() {
		String input=JOptionPane.showInputDialog("請輸入系統編號：");
		if(StringUtils.isNotEmpty(input)){
			Integer id = Integer.parseInt(input);
			OrderService orderService = new OrderService();
			BlessOrder order = orderService.getOne(id);
			if(order!=null){
				setValueToField(order);
				if(StringUtils.equals("Y", order.getwCandlesFlag())){
					wCandlesCheckBox.setSelected(true);
				}
				if(StringUtils.equals("Y", order.getfCandlesFlag())){
					fCandlesCheckBox.setSelected(true);
				}
				idText.setText(order.getId().toString());
				idMessagelabel.setText("系統編號："+order.getId()+"  ( 熱心贊助油香新台幣"+MoneyUtil.converBigDollor(order.getTotalAmt())+"元正)");
			}else{
				JOptionPane.showMessageDialog(null, "系統編號:"+input+",查無資料");
			}
		}else{
			JOptionPane.showMessageDialog(null, "未輸入系統編號");
		}
	
	}

	private void clearBtnActionPerformed(){
		for(String cloumName:cloumNames){
			JTextField text = textMap.get(cloumName);
			text.setText("");
		}	
		wCandlesCheckBox.setSelected(false);
		fCandlesCheckBox.setSelected(false);
		idText.setText("");
		idMessagelabel.setText("");
	}
	
	/**
	 * 儲存
	 * @param e
	 */
	private void saveBtnActionPerformed(ActionEvent e){
		
		BlessOrder order = setValue();
		Object [] wCandlesCheck = wCandlesCheckBox.getSelectedObjects();
		
		if(wCandlesCheck!=null){
			order.setwCandlesFlag("Y");
		}else{
			order.setwCandlesFlag("N");
		}
		
		Object [] fCandlesCheck = fCandlesCheckBox.getSelectedObjects();
		if(fCandlesCheck!=null){
			order.setfCandlesFlag("Y");
		}else{
			order.setfCandlesFlag("N");
		}
		String idtxt = idText.getText();
		if(StringUtils.isNotEmpty(idtxt)){
			Integer id = Integer.parseInt(idtxt);
			order.setId(id);
		}
		
		//檢核欄位不可為空
		String errmsg = validateField(order);
		if(StringUtils.isNotEmpty(errmsg)){
			JOptionPane.showMessageDialog(null,errmsg,"注意",JOptionPane.QUESTION_MESSAGE);
			return ;
		}
		OrderService orderService = new OrderService();
		try {
			if(order.getId()==null){
				order.setCreateTime(LocalDateTime.now());
				order.setModifyTime(LocalDateTime.now());
				order = orderService.insert(order);
				idText.setText(order.getId().toString());
				idMessagelabel.setText("系統編號："+order.getId()+" ( 熱心贊助油香新台幣"+MoneyUtil.converBigDollor(order.getTotalAmt())+"元正)");
				JOptionPane.showMessageDialog(null, "儲存成功");
			}else{
				order = orderService.update(order);
				idText.setText(order.getId().toString());
				idMessagelabel.setText("系統編號："+order.getId()+" ( 熱心贊助油香新台幣"+MoneyUtil.converBigDollor(order.getTotalAmt())+"元正)");
				JOptionPane.showMessageDialog(null, "系統編號："+order.getId()+",更新成功");
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "資料儲存錯誤","注意",JOptionPane.QUESTION_MESSAGE);
		}
		
	}
	
	public String validateField(BlessOrder order){
		StringBuffer bf = new StringBuffer();
		if(StringUtils.equals(order.getwCandlesFlag(), "N")&&StringUtils.equals(order.getfCandlesFlag(), "N")){
			bf.append("請選擇點燈種類").append("\n");
		}
		if(StringUtils.isEmpty(order.getTel())){
			bf.append("連絡電話未填寫").append("\n");
		}
		if(StringUtils.isEmpty(order.getAddress())){
			bf.append("地址未填寫").append("\n");
		}
		if(StringUtils.isEmpty(order.getCompanyName())&&StringUtils.isEmpty(order.getName())){
			bf.append("姓名和公司行號至少填一個").append("\n");
		}
		return bf.toString();
		
	}
	
	/**
	 * 將 UI 欄位設定到entity
	 * @param cloumNames
	 * @return
	 */
	public BlessOrder setValue(){
		BlessOrder order = new BlessOrder();
		for(String cloumName:cloumNames){
			JTextField text = textMap.get(cloumName);
			String value = text.getText();
			try {
				PropertyUtils.setProperty(order, cloumName, value);
			} catch (IllegalAccessException e) {
				log.debug("欄位名:{}轉換錯誤:: UnsupportedEncodingException", cloumName);
			} catch (InvocationTargetException e) {
				log.debug("欄位名:{}轉換錯誤:: InvocationTargetException", cloumName);
			} catch (NoSuchMethodException e) {
				log.debug("欄位名:{}轉換錯誤:: NoSuchMethodException", cloumName);
			}	
		}
		return order;
	}
	
	/**
	 * 將entity set to UI
	 * @param order
	 */
	public void setValueToField(BlessOrder order){
		for(String cloumName:cloumNames){
			try {
				Object value = PropertyUtils.getProperty(order, cloumName);
				JTextField text = textMap.get(cloumName);
				text.setText(value.toString());				
			} catch (IllegalAccessException e) {
				log.debug("欄位名:{}轉換錯誤:: IllegalAccessException", cloumName);
			} catch (InvocationTargetException e) {
				log.debug("欄位名:{}轉換錯誤:: UnsupportedEncodingException", cloumName);
			} catch (NoSuchMethodException e) {
				log.debug("欄位名:{}轉換錯誤:: NoSuchMethodException", cloumName);
			}
		}
	}
	

	/**
	 * 是否光明燈、財利燈
	 * 
	 * @return
	 */
	private JPanel createCheckRow() {
		JPanel umnberRow = new JPanel();
		umnberRow.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
		// 光明燈
		wCandlesCheckBox = new JCheckBox("光明燈");
		wCandlesCheckBox.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		wCandlesCheckBox.setName("wCandlesFlag");
		wCandlesCheckBox.setForeground(Color.RED);
		wCandlesCheckBox.setPreferredSize(new Dimension(120, 50));
		umnberRow.add(wCandlesCheckBox);

		// 財利燈
		fCandlesCheckBox = new JCheckBox("財利燈");
		fCandlesCheckBox.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		fCandlesCheckBox.setName("fCandlesFlag");
		fCandlesCheckBox.setForeground(Color.RED);
		fCandlesCheckBox.setPreferredSize(new Dimension(120, 50));
		umnberRow.add(fCandlesCheckBox);
		
		//系統編號label
		umnberRow.add(Box.createHorizontalStrut(50));
		idMessagelabel = new JLabel();
		idMessagelabel.setFont(new Font(Font.SERIF, Font.PLAIN, 15));
		idMessagelabel.setForeground(Color.BLUE);
		umnberRow.add(idMessagelabel);
		return umnberRow;
	}

	/**
	 * 單據欄位
	 * 
	 * @param cloumName
	 * @param className
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private JPanel createNewCloum(String cloumName, Class className) {

		JPanel lableTextRow = new JPanel();
		lableTextRow.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
		JLabel label = new JLabel();
		label.setName(cloumName + "Lable");
		label.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		label.setPreferredSize(new Dimension(120, 50));

		try {
			Field field = className.getDeclaredField(cloumName);
			if (field.isAnnotationPresent(ChinesClumnName.class)) {
				ChinesClumnName annotionField = field
						.getAnnotation(ChinesClumnName.class);
				label.setText(annotionField.value());
			}
		} catch (NoSuchFieldException e) {
			log.debug("{} 轉換錯誤：NoSuchFieldException {} ", cloumName,
					e.getMessage());
		} catch (SecurityException e) {
			log.debug("{} 轉換錯誤：SecurityException {} ", cloumName,
					e.getMessage());
		}
		lableTextRow.add(label);
		EditTextField text = new EditTextField();
		text.setName(cloumName);
		text.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
		if (StringUtils.contains(cloumName, "tel")) {
			text.setPreferredSize(new Dimension(500, 50));
		} else {
			text.setPreferredSize(new Dimension(800, 50));
		}
		lableTextRow.add(text);
		textMap.put(cloumName,text);

		return lableTextRow;
	}



	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
