package tw.katy.com.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.katy.com.entity.SystemParam;
import tw.katy.com.enums.SystemCode;
import tw.katy.com.service.SystemParamSerivce;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
/**
 * 系統參數設定Panel
 * @author Katy
 *
 */
@SuppressWarnings("serial")
public class SettingDialog extends JDialog {
	
	private Logger log = LoggerFactory.getLogger(SettingDialog.class);
	
	private List<JLabel> labels = Lists.newArrayList();
	
	private Map<SystemCode,JTextField> texts =  Maps.newHashMap();
	
	private Dimension dialogDimension= new Dimension(500, 300);
	
	private Font font = new Font(Font.DIALOG, Font.PLAIN, 14);
	
	SystemParamSerivce  paramService = new SystemParamSerivce();

	public SettingDialog(){
		
		this.setTitle("系統設定");
		this.setSize(dialogDimension);
		JPanel mainPanel = initMainPanel();
		Box btnBox = initBtnBox();
		mainPanel.add(btnBox);
		this.add(mainPanel);
	}
	
	public JPanel initMainPanel(){
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(Box.createVerticalStrut(10));
		for(SystemCode code:SystemCode.values()){
			JPanel labelTexBox = new JPanel();
			labelTexBox.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
			JLabel label = new JLabel(code.getLocalName());
			label.setFont(font);
			label.setPreferredSize(new Dimension(120, 30));
			labelTexBox.add(label);	
			labels.add(label);
			JTextField text = new JTextField();
			text.setPreferredSize(new Dimension(300, 30));
			String value = paramService.getBylabel(code);
			text.setText(value);
			labelTexBox.add(text);
			texts.put(code, text);
			mainPanel.add(labelTexBox);
		}
		return mainPanel;
	}
	
	public Box initBtnBox(){
		Box btnBox = Box.createHorizontalBox();
		JButton okBtn = new JButton("確認");
		JButton cancelBtn = new JButton("取消");
		btnBox.add(Box.createGlue());
		btnBox.add(okBtn);
		btnBox.add(Box.createGlue());
		btnBox.add(cancelBtn);
		btnBox.add(Box.createGlue());
		//取消
		cancelBtn.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				setVisible(false); 
			    dispose(); 				
			}
		});
		//確認
		okBtn.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				
				for(SystemCode code:texts.keySet()){
					JTextField text = texts.get(code);
					String value = text.getText();
					SystemParam param = new SystemParam(code.getLocalName(), value);
					try {
						paramService.update(param);
					} catch (Exception e1) {
						log.debug("{}",e1);
					}
				}
				setVisible(false); 
			    dispose(); 				
			}
		});
		return btnBox;
	}
}
