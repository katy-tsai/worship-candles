package tw.katy.com.ui;

import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
/**
 * 按鈕列
 * @author katy
 *
 */
@SuppressWarnings("serial")
public class ButtonBox extends Box {
	
	private JButton saveBtn;
	private JButton clearBtn;
	private JButton queryBtn;
	private JButton printBtn;
	private JButton nextBtn;
	
	public ButtonBox(){
		this(BoxLayout.X_AXIS);
		//createHorizontalBox();
		initSaveBtn();
		initClearBtn();
		initQueryBtn();
		initPrintBtn();
		initNextBtn();
		this.add(Box.createHorizontalStrut(250));
		this.add(saveBtn);
		this.add(Box.createHorizontalStrut(30));
		this.add(clearBtn);
		this.add(Box.createHorizontalStrut(30));
		this.add(queryBtn);
		this.add(Box.createHorizontalStrut(30));
		this.add(printBtn);
		this.add(Box.createHorizontalStrut(30));
		//this.add(nextBtn);
		this.add(Box.createHorizontalStrut(30));
		
	}

	public ButtonBox(int axis) {
		super(axis);
	}
	
	
	private void initSaveBtn(){
		saveBtn = getFunctionButtion("  儲  存  ");				
	}
	
	private void initClearBtn(){
		clearBtn = getFunctionButtion("  清 除  ");		
	}
	
	private void initQueryBtn(){
		queryBtn = getFunctionButtion("  查  詢  ");	
	}
	
	private void initPrintBtn(){
		printBtn = getFunctionButtion("  列  印  ");		
	}
	
	private void initNextBtn(){
		nextBtn = getFunctionButtion("  下一筆 ");		
	}
	
	
	private JButton getFunctionButtion(String name){
		JButton button = new JButton(name);
		button.setFont(new Font(Font.SERIF, Font.BOLD, 20));		
		return button;
	}

	public JButton getSaveBtn() {
		return saveBtn;
	}

	public void setSaveBtn(JButton saveBtn) {
		this.saveBtn = saveBtn;
	}

	public JButton getClearBtn() {
		return clearBtn;
	}

	public void setClearBtn(JButton clearBtn) {
		this.clearBtn = clearBtn;
	}

	public JButton getQueryBtn() {
		return queryBtn;
	}

	public void setQueryBtn(JButton queryBtn) {
		this.queryBtn = queryBtn;
	}

	public JButton getPrintBtn() {
		return printBtn;
	}

	public void setPrintBtn(JButton printBtn) {
		this.printBtn = printBtn;
	}

	public JButton getNextBtn() {
		return nextBtn;
	}

	public void setNextBtn(JButton nextBtn) {
		this.nextBtn = nextBtn;
	}
	
	

}
