package tw.katy.com.ui;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
/**
 * 可編輯欄位
 * @author katy
 *
 */
@SuppressWarnings("serial")
public class EditTextField extends JTextField  implements MouseListener {

	protected JPopupMenu pop = null;
	
	protected JMenuItem copy = null,paste = null,cut = null;
	
	public EditTextField(){
		super();
		init();
	}
	
	public void init(){
		this.addMouseListener(this);
		pop = new JPopupMenu();
		pop.add(copy = new JMenuItem("複製"));
		pop.add(paste = new JMenuItem("貼上"));
		pop.add(cut = new JMenuItem("剪下"));
		
		copy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK));
		paste.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_MASK));
		cut.setAccelerator(KeyStroke.getKeyStroke('X',  InputEvent.CTRL_MASK));
		
		copy.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				action(e);
			}
		});
		
		paste.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				action(e);				
			}
		});
		
		cut.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				action(e);
			}
		});
	}
	
	public boolean isCanCopy(){
		boolean b = false;
		int start = this.getSelectionStart();
		int end = this.getSelectionEnd();
		if(start!=end){
			b = true;
		}
		return b;
	}
	
	public boolean isClipboardString(){
		boolean b = false;
		Clipboard clipboard = this.getToolkit().getSystemClipboard();
		Transferable content = clipboard.getContents(this);
		try {
			if(content.getTransferData(DataFlavor.stringFlavor) instanceof String){
				b = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return b;
	}

	protected void action(ActionEvent e) {
		String str = e.getActionCommand();
		if(str.equals(copy.getText())){
			this.copy();
		}else if(str.equals(paste.getText())){
			this.paste();
		}else if(str.equals(cut.getText())){
			this.cut();
		}
		
	}
	

	

	public void mousePressed(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON3){//滑鼠右鍵
			copy.setEnabled(isCanCopy());
			paste.setEnabled(isClipboardString());
			cut.setEnabled(isCanCopy());
			pop.show(this, e.getX(), e.getY());
		}
	}

	public void mouseReleased(MouseEvent e) {		
	}

	public void mouseEntered(MouseEvent e) {		
	}

	public void mouseExited(MouseEvent e) {		
	}

	public void mouseClicked(MouseEvent e) {		
	}


}
