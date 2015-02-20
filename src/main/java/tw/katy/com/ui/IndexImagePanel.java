package tw.katy.com.ui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
/**
 * 首頁圖片
 * @author katy
 *
 */
@SuppressWarnings("serial")
public class IndexImagePanel  extends javax.swing.JPanel {

	 protected void paintComponent(Graphics g) {  
	    	ImageIcon icon =new ImageIcon(getClass().getClassLoader().getResource("index.jpg"));
	    	System.out.println(getClass().getClassLoader().getResource("index.jpg"));
	        Image img = icon.getImage();  
	        g.drawImage(img, 0, 0, icon.getIconWidth(),  
	                icon.getIconHeight(), icon.getImageObserver());  
	        

	    }   
}
