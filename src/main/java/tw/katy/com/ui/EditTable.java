package tw.katy.com.ui;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.im.InputContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 可編輯table
 * @author Katy
 *
 */
@SuppressWarnings("serial")
public class EditTable extends JTable implements MouseListener {
	
	private Logger log = LoggerFactory.getLogger(EditTable.class);

	protected JPopupMenu pop = null;

	protected JMenuItem copy = null, listPrint = null;
	
	protected JFileChooser fc = new JFileChooser();
	
	//財利燈
	public static final String CANDLES_TYPE_F = "F";
	
	//光明燈
	public static final String CANDLES_TYPE_W = "W";
	
	String newline = System.getProperty("line.separator");

	public EditTable() {
		super();
		init();
	}

	public EditTable(TableModel dm) {
		super(dm, null, null);
		init();
	}

	public void init() {
		this.addMouseListener(this);
		pop = new JPopupMenu();
		pop.add(copy = new JMenuItem("複製"));
		pop.add(listPrint = new JMenuItem("元寶名單匯出"));

		copy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK));
		listPrint.setAccelerator(KeyStroke.getKeyStroke('E',
				InputEvent.CTRL_MASK));
		copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action(e);
			}
		});
		listPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action(e);
			}
		});
		
		fc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});

	}

	/**
	 * 是否可複製
	 * 
	 * @return
	 */
	public boolean isCanCopy() {
		boolean b = false;
		int[] starts = this.getSelectedRows();
		int start = starts.length;
		if (start > 0) {
			b = true;
		}
		return b;
	}

	public boolean isClipboardString() {
		boolean b = false;
		Clipboard clipboard = this.getToolkit().getSystemClipboard();
		Transferable content = clipboard.getContents(this);
		try {
			if (content.getTransferData(DataFlavor.stringFlavor) instanceof String) {
				b = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public boolean isSelectRow() {
		boolean b = false;
		int c = this.getSelectedColumn();
		if (c == 0) {
			b = true;
		}
		return b;
	}

	protected void action(ActionEvent e) {
		String str = e.getActionCommand();
		if (str.equals(copy.getText())) {
			this.copy();
		} else if (str.equals(listPrint.getText())) {
			exportList();
		}

	}

	/**
	 * 元寶名單列印
	 */
	public void exportList() {
		
		int result;
		File file = null;
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
		result = fc.showSaveDialog(this);
		String fileName="元寶名單.doc";
		
		// 當用戶沒有選擇文件,而是自己鍵入文件名稱時,系統會自動以此文件名建立新文件.
		if (result == JFileChooser.APPROVE_OPTION) {
			String filePath = fc.getSelectedFile().getPath()+"/"+fileName;
			file = new File(filePath);
		} else if (result == JFileChooser.CANCEL_OPTION) {
			log.debug("您沒有選擇任何文件");
		}
		System.out.println(file.getPath());
		FileOutputStream fileOutStream = null;

		if (file != null) {
			try {
				fileOutStream = new FileOutputStream(file);
				for (int i = 0; i < dataModel.getRowCount(); i++) {
					if (selectionModel.isSelectedIndex(i)) {
						String names = (String)dataModel.getValueAt(i, 2);
						String companyName = (String)dataModel.getValueAt(i, 3);
						String w = (String)dataModel.getValueAt(i, 5);
						String f = (String)dataModel.getValueAt(i, 6);
						System.out.println(f);
						System.out.println(w);
						String name = getName(names, companyName);
						String candlesType="";
						if(StringUtils.equals(w, "Y")){
							candlesType=CANDLES_TYPE_W;
							writeData(fileOutStream, name, candlesType);
						}
						if(StringUtils.equals(f, "Y")){
							candlesType=CANDLES_TYPE_F;
							writeData(fileOutStream, name, candlesType);
						}
					}
				}
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			}finally {
				try {
					if (fileOutStream != null)
						fileOutStream.close();
				} catch (IOException ioe2) {
				}
			}
		
		}
		
	}
	
	private String getName(String name,String companyName){
		String result = "";
		if(StringUtils.isNotEmpty(companyName)){
			result = companyName;
		}
		
		if(StringUtils.isNotEmpty(companyName)&&StringUtils.isNotEmpty(name)){
			result= result+newline;
		}
		
		if(StringUtils.isNotEmpty(name)){
			result= result+ name;
		}
				
		return result;
	}
	
	public void writeData(FileOutputStream fileOutStream,String name,String candlesType){
		
		try {
			if(StringUtils.equals(CANDLES_TYPE_F, candlesType)){
				fileOutStream.write("財利燈".getBytes());
				fileOutStream.write(newline.getBytes());
			}else{
				fileOutStream.write("光明燈".getBytes());
				fileOutStream.write(newline.getBytes());
			}
			
			fileOutStream.write(name.getBytes());
			fileOutStream.write(newline.getBytes());
			fileOutStream.write(newline.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {// 滑鼠右鍵
			copy.setEnabled(isCanCopy());
			listPrint.setEnabled(isSelectRow());
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

	/**
	 * Transfers the currently selected range in the associated text model to
	 * the system clipboard, leaving the contents in the text model. The current
	 * selection remains intact. Does nothing for <code>null</code> selections.
	 * 
	 * @see java.awt.Toolkit#getSystemClipboard
	 * @see java.awt.datatransfer.Clipboard
	 */
	public void copy() {
		invokeAction("copy", TransferHandler.getCopyAction());
	}

	/**
	 * This is a conveniance method that is only useful for <code>cut</code>,
	 * <code>copy</code> and <code>paste</code>. If an <code>Action</code> with
	 * the name <code>name</code> does not exist in the <code>ActionMap</code>,
	 * this will attemp to install a <code>TransferHandler</code> and then use
	 * <code>altAction</code>.
	 */
	private void invokeAction(String name, Action altAction) {
		ActionMap map = getActionMap();
		Action action = null;

		if (map != null) {
			action = map.get(name);
		}
		if (action == null) {
			installDefaultTransferHandlerIfNecessary();
			action = altAction;
		}
		action.actionPerformed(new ActionEvent(this,
				ActionEvent.ACTION_PERFORMED, (String) action
						.getValue(Action.NAME), EventQueue
						.getMostRecentEventTime(), getCurrentEventModifiers()));
	}

	private int getCurrentEventModifiers() {
		int modifiers = 0;
		AWTEvent currentEvent = EventQueue.getCurrentEvent();
		if (currentEvent instanceof InputEvent) {
			modifiers = ((InputEvent) currentEvent).getModifiers();
		} else if (currentEvent instanceof ActionEvent) {
			modifiers = ((ActionEvent) currentEvent).getModifiers();
		}
		return modifiers;
	}

	private static DefaultTransferHandler defaultTransferHandler;

	/**
	 * If the current <code>TransferHandler</code> is null, this will install a
	 * new one.
	 */
	private void installDefaultTransferHandlerIfNecessary() {
		if (getTransferHandler() == null) {
			if (defaultTransferHandler == null) {
				defaultTransferHandler = new DefaultTransferHandler();
			}
			setTransferHandler(defaultTransferHandler);
		}
	}

	/**
	 * A Simple TransferHandler that exports the data as a String, and imports
	 * the data from the String clipboard. This is only used if the UI hasn't
	 * supplied one, which would only happen if someone hasn't subclassed Basic.
	 */
	static class DefaultTransferHandler extends TransferHandler implements
			UIResource {
		public void exportToClipboard(JComponent comp, Clipboard clipboard,
				int action) throws IllegalStateException {
			if (comp instanceof JTextComponent) {
				JTextComponent text = (JTextComponent) comp;
				int p0 = text.getSelectionStart();
				int p1 = text.getSelectionEnd();
				if (p0 != p1) {
					try {
						Document doc = text.getDocument();
						String srcData = doc.getText(p0, p1 - p0);
						StringSelection contents = new StringSelection(srcData);

						// this may throw an IllegalStateException,
						// but it will be caught and handled in the
						// action that invoked this method
						clipboard.setContents(contents, null);

						if (action == TransferHandler.MOVE) {
							doc.remove(p0, p1 - p0);
						}
					} catch (BadLocationException ble) {
					}
				}
			}
		}

		public boolean importData(JComponent comp, Transferable t) {
			if (comp instanceof JTextComponent) {
				DataFlavor flavor = getFlavor(t.getTransferDataFlavors());

				if (flavor != null) {
					InputContext ic = comp.getInputContext();
					if (ic != null) {
						ic.endComposition();
					}
					try {
						String data = (String) t.getTransferData(flavor);

						((JTextComponent) comp).replaceSelection(data);
						return true;
					} catch (UnsupportedFlavorException ufe) {
					} catch (IOException ioe) {
					}
				}
			}
			return false;
		}

		public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
			JTextComponent c = (JTextComponent) comp;
			if (!(c.isEditable() && c.isEnabled())) {
				return false;
			}
			return (getFlavor(transferFlavors) != null);
		}

		public int getSourceActions(JComponent c) {
			return NONE;
		}

		private DataFlavor getFlavor(DataFlavor[] flavors) {
			if (flavors != null) {
				for (DataFlavor flavor : flavors) {
					if (flavor.equals(DataFlavor.stringFlavor)) {
						return flavor;
					}
				}
			}
			return null;
		}
	}

}
