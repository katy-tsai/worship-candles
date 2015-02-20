package tw.katy.com.app;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import tw.katy.com.ui.IndexFrame;
import tw.katy.com.ui.IndexPanel;
import tw.katy.com.ui.OrderPanel;
import tw.katy.com.ui.QueryPannel;
import tw.katy.com.ui.SettingAction;

/**
 * 首頁
 * 
 * @author Katy
 * 
 */
public class App {
	public static void main(String[] args) {
		final IndexFrame frame = new IndexFrame();

		IndexPanel index = new IndexPanel();
		OrderPanel order = new OrderPanel();
		QueryPannel query = new QueryPannel();
		frame.setTabInfo(index, order, query);
		JMenuBar menubar = new JMenuBar();
		JMenu settingMenu = new JMenu("設定");
		SettingAction setting = new SettingAction();
		settingMenu.add(setting);
		menubar.add(settingMenu);
		frame.setJMenuBar(menubar);
		frame.setVisible(true);
	}

}
