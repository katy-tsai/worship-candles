package tw.katy.com.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import tw.katy.com.entity.BlessOrder;
import tw.katy.com.entity.OrderTableModel;
import tw.katy.com.service.OrderService;

import com.google.common.collect.Lists;
 
/**
 * 查詢pannel
 * @author Katy
 *
 */
@SuppressWarnings("serial")
public class QueryPannel extends JPanel {

	//private Logger log = LoggerFactory.getLogger(QueryPannel.class);
	
	private JLabel telLabel;
	
	private EditTextField tel;
	
	private JLabel nameLabel;
	
	private EditTextField name;
	
	private JLabel dateLabel;
	
	private DateChooser dateChooser1;
	
	private DateChooser dateChooser2;
	
	private JButton searchBtn;
	
	private JButton clearBtn;
	
	private EditTable queryTable;
	
	private Dimension labelDimension= new Dimension(100, 40);
	
	private Dimension textDimension= new Dimension(200, 40);
	
	private List<String> columnNameLists = Lists.newArrayList(new String[]{"id","tel","name","companyName","address","wCandlesFlag","fCandlesFlag","totalAmt","createTime"});
	
	private int pitch = 100;
	
	private int font_size = 18;
	
	private Font tableFont = new Font(Font.SERIF, Font.PLAIN, 15);
	
	private OrderTableModel tableModel;

	public QueryPannel() {

		// 空白邊緣
		this.setLayout(new BorderLayout());
		this.setName(" 查   詢  ");
		this.setBorder(new EmptyBorder(10, 10, 10, 10));
		//查詢panel
		JPanel seachPanel = initSearchPanel();		
		this.add(seachPanel,BorderLayout.NORTH);
		
		JPanel tablePanel = initTablePanel();
		this.add(tablePanel,BorderLayout.CENTER);	
		addListener();

	}
	
	/**
	 * 設定事件監聽
	 * @param btnRow
	 */
	private void addListener(){
		//查詢
		searchBtn.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				searchBtnActionPerformed();
				
			}
		});
		
		//清除
		clearBtn.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				clearBtnActionPerformed();
			}
		});
	}
	
	private void searchBtnActionPerformed(){
		OrderService service =new OrderService();
		String starDate = dateChooser1.getShowDate().getText();
		String endDate = dateChooser2.getShowDate().getText();
		List<BlessOrder> orders = service.getByCondition(tel.getText(), name.getText(), starDate, endDate);
		tableModel.setData(columnNameLists, orders);
		
	}
	
	private void clearBtnActionPerformed(){
		tel.setText("");
		name.setText("");
		dateChooser1.getShowDate().setText("");
		dateChooser2.getShowDate().setText("");
	}
	
	
	/**
	 * table panel
	 * @return
	 */
	private JPanel initTablePanel(){
		JPanel tablePanel = new JPanel();
		
		OrderService service =new OrderService();
		List<BlessOrder> orders = service.getAll();
		tableModel = new OrderTableModel(columnNameLists, orders);	
		queryTable = new EditTable(tableModel);
		queryTable.setPreferredScrollableViewportSize(new Dimension(1190, 400));
		queryTable.setFont(tableFont);
		queryTable.setRowHeight(25);	
		queryTable.setCellSelectionEnabled(true);
	    JScrollPane scrollPane = new JScrollPane(queryTable);
	    tablePanel.add(scrollPane);
		
		return tablePanel;
	}
	
	/**
	 * 查詢panel
	 * @return
	 */
	private JPanel initSearchPanel(){
		JPanel seachPanel = new JPanel();

		// 標題邊框
		TitledBorder title = new TitledBorder(new EtchedBorder(),"查  詢 ");
		title.setTitleFont(new Font(Font.SERIF, Font.BOLD, 20));
		title.setTitleColor(Color.BLUE);
		seachPanel.setBorder(title);
		seachPanel.setLayout(new BoxLayout(seachPanel, BoxLayout.Y_AXIS));
		
		//搜尋欄位ROW1		
		JPanel lableTextRow1 = new JPanel();
		lableTextRow1.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		//電話
		telLabel = new JLabel("電        話");
		telLabel.setFont(new Font(Font.SERIF, Font.BOLD, font_size));
		telLabel.setPreferredSize(labelDimension);
		lableTextRow1.add(telLabel);
		tel = new EditTextField();
		tel.setFont(new Font(Font.SERIF, Font.PLAIN, font_size));
		tel.setPreferredSize(textDimension);
		lableTextRow1.add(tel);
		lableTextRow1.add(Box.createHorizontalStrut(pitch));
		//姓名
		nameLabel = new JLabel("姓        名");
		nameLabel.setFont(new Font(Font.SERIF, Font.BOLD, font_size));
		nameLabel.setPreferredSize(labelDimension);
		lableTextRow1.add(nameLabel);
		name = new EditTextField();
		name.setPreferredSize(textDimension);
		name.setFont(new Font(Font.SERIF, Font.PLAIN, font_size));
		lableTextRow1.add(name);
		seachPanel.add(lableTextRow1);
		//搜尋欄位ROW2		
		JPanel lableTextRow2 = new JPanel();
		lableTextRow2.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		//日期
		dateLabel = new JLabel("輸入日期");
		dateLabel.setFont(new Font(Font.SERIF, Font.BOLD, font_size));
		dateLabel.setPreferredSize(labelDimension);
		lableTextRow2.add(dateLabel);
		dateChooser1 = new DateChooser("yyyy-MM-dd");
		dateChooser1.setSize(textDimension);
		lableTextRow2.add(dateChooser1);
		JLabel label = new JLabel("~");
		label.setFont(new Font(Font.SERIF, Font.BOLD, 30));
		lableTextRow2.add(label);
		dateChooser2 = new DateChooser("yyyy-MM-dd");
		dateChooser2.setSize(textDimension);
		lableTextRow2.add(dateChooser2);
		lableTextRow2.add(Box.createHorizontalStrut(300));
		searchBtn = new JButton(" 查      詢    ");
		searchBtn.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		lableTextRow2.add(searchBtn);
		clearBtn = new JButton(" 清      除    ");
		clearBtn.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		lableTextRow2.add(clearBtn);
		seachPanel.add(lableTextRow2);
		return seachPanel;
	}
	
	

}
