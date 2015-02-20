package tw.katy.com.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
/**
 * 系統設定action
 * @author Katy
 *
 */
@SuppressWarnings("serial")
public class SettingAction extends AbstractAction{
	
	public SettingAction(){
		super("系統設定");
	}

	public void actionPerformed(ActionEvent e) {
		final SettingDialog setting = new SettingDialog();
		setting.setVisible(true);
	}
	
}
