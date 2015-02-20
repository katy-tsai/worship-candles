package tw.katy.com.ui;

import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * index frame
 * @author Katy
 *
 */
@SuppressWarnings("serial")
public class IndexFrame extends JFrame {
	
	private static final String TITLE = "點燈祈福系統";
	
	private static int width = 1250;
	
	private static int height = 700;
	public IndexFrame(){
		initComponets();
	}

	private void initComponets() {
		setTitle(TITLE);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFrame.setDefaultLookAndFeelDecorated(true);	   
	   // setExtendedState(Frame.MAXIMIZED_BOTH);
	   // setLocationByPlatform(true);
	   
	   this.setSize(width, height);
		
	}
	
	public void setTabInfo(JComponent... components){
		JTabbedPane pane =  new JTabbedPane();
		pane.setFont(new Font(Font.SERIF, Font.BOLD, 20));		
		for(JComponent component:components){
			 pane.addTab("    "+component.getName()+"    ", component);
		}
	   
		this.getContentPane().add(pane);
	}

}
