/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tw.katy.com.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

/**
 * 首頁 Panel
 * 
 * @author Katy
 */
@SuppressWarnings("serial")
public class IndexPanel extends javax.swing.JPanel {

	/**
	 * Creates new form Panel1
	 */
	public IndexPanel() {
		this.setLayout(new BorderLayout());
		this.setName(" 首  頁  ");
		this.setBackground(new Color(8, 8, 8));
		//this.setBackground(Color.black);
		this.setBorder(new EmptyBorder(10, 250, 10, 250));
		IndexImagePanel image = new IndexImagePanel();
		image.setSize(new Dimension(1000, 600));
		this.add(image);
		
		Box box = Box.createHorizontalBox();
		JLabel copyRight = new JLabel("COPYRIGHT © 2015   Katy");
		copyRight.setForeground(new Color(243,243, 96));
		box.add(Box.createHorizontalStrut(300));
		box.add(copyRight);
		this.add(box,BorderLayout.SOUTH);

	}

	

}
