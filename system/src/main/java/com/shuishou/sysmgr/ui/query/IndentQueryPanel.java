package com.shuishou.sysmgr.ui.query;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.ConstantValue;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.Indent;
import com.shuishou.sysmgr.beans.IndentDetail;
import com.shuishou.sysmgr.beans.LogData;
import com.shuishou.sysmgr.beans.Printer;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.JDatePicker;

public class IndentQueryPanel extends JPanel implements ActionListener{
	private final Logger logger = Logger.getLogger(IndentQueryPanel.class.getName());
	private MainFrame mainFrame;
	private JTextField tfTableName = new JTextField();
	private JDatePicker dpStartDate = new JDatePicker();
	private JDatePicker dpEndDate = new JDatePicker();
	private JCheckBox cbPayStatusPaid = new JCheckBox("Paid");
	private JCheckBox cbPayStatusUnpaid = new JCheckBox("Unpaid");
	private JCheckBox cbPayStatusOthers = new JCheckBox("Others");
	private JCheckBox cbOrderByTime = new JCheckBox("Time");
	private JCheckBox cbOrderByPayStatus = new JCheckBox("Pay Status");
	private JCheckBox cbOrderByTableName = new JCheckBox("Table Name");
	private JButton btnQuery = new JButton("Query");
	private JButton btnPrintIndent = new JButton("Print Order");
	
	private JTable tableIndent = new JTable();
	private IndentModel modelIndent = new IndentModel();
	private JTable tableIndentDetail = new JTable();
	private IndentDetailModel modelIndentDetail = new IndentDetailModel();
	
	
	private ArrayList<Indent> listIndent = new ArrayList<>();
	
	public IndentQueryPanel(MainFrame mainFrame){
		this.mainFrame = mainFrame;
		initUI();
	}
	
	private void initUI(){
		JLabel lbTableName = new JLabel("Table Name : ");
		JLabel lbStartDate = new JLabel("Start Date : ");
		JLabel lbEndDate = new JLabel("End Date : ");
		tfTableName.setPreferredSize(new Dimension(150, 25));
		dpStartDate.setShowYearButtons(true);
		dpEndDate.setShowYearButtons(true);
		
		tableIndent.setModel(modelIndent);
		tableIndent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableIndent.getColumnModel().getColumn(0).setPreferredWidth(50);
		tableIndent.getColumnModel().getColumn(1).setPreferredWidth(80);
		tableIndent.getColumnModel().getColumn(2).setPreferredWidth(80);
		tableIndent.getColumnModel().getColumn(3).setPreferredWidth(60);
		tableIndent.getColumnModel().getColumn(4).setPreferredWidth(80);
		tableIndent.getColumnModel().getColumn(5).setPreferredWidth(80);
		tableIndent.getColumnModel().getColumn(6).setPreferredWidth(130);
		tableIndent.getColumnModel().getColumn(7).setPreferredWidth(130);
		tableIndent.getColumnModel().getColumn(8).setPreferredWidth(80);
		JScrollPane jspTableIndent = new JScrollPane(tableIndent, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		tableIndent.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		tableIndentDetail.setModel(modelIndentDetail);
		tableIndentDetail.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableIndentDetail.getColumnModel().getColumn(0).setPreferredWidth(120);
		tableIndentDetail.getColumnModel().getColumn(1).setPreferredWidth(140);
		tableIndentDetail.getColumnModel().getColumn(2).setPreferredWidth(50);
		tableIndentDetail.getColumnModel().getColumn(3).setPreferredWidth(50);
		tableIndentDetail.getColumnModel().getColumn(4).setPreferredWidth(50);
		tableIndentDetail.getColumnModel().getColumn(5).setPreferredWidth(200);
		JScrollPane jspTableIndentDetail = new JScrollPane(tableIndentDetail, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		tableIndentDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JPanel pPayStatus = new JPanel();
		pPayStatus.setBorder(BorderFactory.createTitledBorder("Pay Status"));
		pPayStatus.add(cbPayStatusOthers);
		pPayStatus.add(cbPayStatusUnpaid);
		pPayStatus.add(cbPayStatusPaid);
		JPanel pOrderBy = new JPanel();
		pOrderBy.setBorder(BorderFactory.createTitledBorder("Order By"));
		pOrderBy.add(cbOrderByTime);
		pOrderBy.add(cbOrderByPayStatus);
		pOrderBy.add(cbOrderByTableName);
		
		JPanel pCondition = new JPanel(new GridBagLayout());
		pCondition.add(lbTableName,	new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(tfTableName,	new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(lbStartDate,	new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(dpStartDate,	new GridBagConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(lbEndDate,	new GridBagConstraints(4, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(dpEndDate,	new GridBagConstraints(5, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(new JLabel(),new GridBagConstraints(6, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(pPayStatus,	new GridBagConstraints(0, 1, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(pOrderBy, 	new GridBagConstraints(2, 1, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(btnQuery,	new GridBagConstraints(4, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(btnPrintIndent,new GridBagConstraints(5, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(new JLabel(),new GridBagConstraints(6, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		btnQuery.addActionListener(this);
		btnPrintIndent.addActionListener(this);
		
		JPanel pTable = new JPanel(new GridBagLayout());
		pTable.add(jspTableIndent,		new GridBagConstraints(0, 0, 1, 1, 3, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));
		pTable.add(jspTableIndentDetail,new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));
		
		setLayout(new BorderLayout());
		add(pTable, BorderLayout.CENTER);
		add(pCondition, BorderLayout.NORTH);
		
		tableIndent.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				modelIndentDetail.setData((ArrayList)modelIndent.getObjectAt(tableIndent.getSelectedRow()).getItems());
				tableIndentDetail.updateUI();
			}
			
		});
	}
	
	private void doQuery(){
		String url = "indent/queryindent";
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId() + "");
		if (tfTableName.getText() != null && tfTableName.getText().length() > 0)
			params.put("deskname",tfTableName.getText());
		if (dpStartDate.getModel() != null && dpStartDate.getModel().getValue() != null){
			Calendar c = (Calendar)dpStartDate.getModel().getValue();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			params.put("starttime", ConstantValue.DFYMDHMS.format(c.getTime()));
		}
		if (dpEndDate.getModel() != null && dpEndDate.getModel().getValue() != null){
			Calendar c = (Calendar)dpEndDate.getModel().getValue();
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			params.put("endtime", ConstantValue.DFYMDHMS.format(c.getTime()));
		}
		if (cbPayStatusOthers.isSelected() || cbPayStatusUnpaid.isSelected() || cbPayStatusPaid.isSelected()){
			String paystatus = "";
			if (cbPayStatusPaid.isSelected())
				paystatus += "Paid";
			if (cbPayStatusUnpaid.isSelected())
				paystatus += "Unpaid";
			if (cbPayStatusOthers.isSelected())
				paystatus += "Other";
			params.put("status", paystatus);
		}
		if (cbOrderByTime.isSelected() || cbOrderByPayStatus.isSelected() || cbOrderByTableName.isSelected()){
			String orderby = "";
			if (cbOrderByTime.isSelected())
				orderby += "startTime";
			if (cbOrderByPayStatus.isSelected())
				orderby+= "status";
			if (cbOrderByTableName.isSelected())
				orderby += "deskname";
			params.put("orderby", orderby);
		}
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for query indent. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for query indent. URL = " + url);
			return;
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMDHMS).create();
		HttpResult<ArrayList<Indent>> result = gson.fromJson(response, new TypeToken<HttpResult<ArrayList<Indent>>>(){}.getType());
		if (!result.success){
			logger.error("return false while query indent. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		listIndent = result.data;
		tableIndent.updateUI();
	}
	
	private void doPrint(){
		int row = tableIndent.getSelectedRow();
		if (row < 0){
			JOptionPane.showMessageDialog(this, "Please choose a record from Order table.", "Error", JOptionPane.YES_NO_OPTION);
			return;
		}
		String url = "indent/printindent";
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", mainFrame.getLoginUser().getId()+"");
		params.put("indentId", modelIndent.getObjectAt(row).getId()+"");
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params, "UTF-8");
		if (response == null || response.length() == 0){
			logger.error("get null from server while print indent. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server while print indent. URL = " + url + ", param = "+ params);
			return;
		}
		HttpResult<Indent> result = new Gson().fromJson(response, new TypeToken<HttpResult<Indent>>(){}.getType());
		if (!result.success){
			logger.error("return false while print indent. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnQuery){
			doQuery();
		} else if (e.getSource() == btnPrintIndent){
			doPrint();
		} 
	}
	
	class IndentModel extends AbstractTableModel{

		private String[] header = new String[]{"Table","Status", "Sequence", "Price", "Paid Price", "Pay Way", "Start Time", "End Time", "Discount Temp"};
		
		public IndentModel(){

		}
		@Override
		public int getRowCount() {
			if (listIndent == null) return 0;
			return listIndent.size();
		}

		@Override
		public int getColumnCount() {
			return header.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Indent indent = listIndent.get(rowIndex);
			switch(columnIndex){
			case 0:
				return indent.getDeskName();
			case 1: 
				return statusString(indent.getStatus());
			case 2:
				return indent.getDailySequence();
			case 3:
				return indent.getTotalPrice();
			case 4: 
				return indent.getPaidPrice();
			case 5:
				return indent.getPayWay();
			case 6:
				return ConstantValue.DFYMDHMS.format(indent.getStartTime());
			case 7:
				if (indent.getEndTime() == null)
					return "";
				else return ConstantValue.DFYMDHMS.format(indent.getEndTime());
			case 8:
				return indent.getDiscountTemplate() != null ? indent.getDiscountTemplate() : "";
			}
			return "";
		}
		
		private String statusString(int status){
			switch(status){
			case ConstantValue.INDENT_STATUS_PAID:
				return "Paid";
			case ConstantValue.INDENT_STATUS_CLOSED:
				return "Closed";
			case ConstantValue.INDENT_STATUS_CANCELED:
				return "Canceled";
			case ConstantValue.INDENT_STATUS_OPEN:
				return "Open";
			case ConstantValue.INDENT_STATUS_FORCEEND:
				return "Force Clean";
			case ConstantValue.INDENT_STATUS_REFUND:
				return "Refund";
			}
			return "";
		}
		
		@Override
		public String getColumnName(int col){
			return header[col];
		}
		
		public Indent getObjectAt(int row){
			return listIndent.get(row);
		}
	}

	class IndentDetailModel extends AbstractTableModel{

		private String[] header = new String[]{"First Language Name","Second Language Name", "Amount", "Price", "Weight", "Requirement"};
		private ArrayList<IndentDetail> details = new ArrayList<>();
		public IndentDetailModel(){
		}
		@Override
		public int getRowCount() {
			if (details == null) return 0;
			return details.size();
		}

		@Override
		public int getColumnCount() {
			return header.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			IndentDetail detail = details.get(rowIndex);
			switch(columnIndex){
			case 0:
				return detail.getDishFirstLanguageName();
			case 1: 
				return detail.getDishSecondLanguageName();
			case 2:
				return detail.getAmount();
			case 3:
				return detail.getDishPrice();
			case 4:
				if (detail.getWeight() == 0)
					return "";
				return detail.getWeight();
			case 5: 
				return detail.getAdditionalRequirements();
			}
			return "";
		}
		
		@Override
		public String getColumnName(int col){
			return header[col];
		}
		
		public void setData(ArrayList<IndentDetail> details){
			this.details = details;
		}
		
		public IndentDetail getObjectAt(int row){
			return details.get(row);
		}
	}
}
