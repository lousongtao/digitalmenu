package com.shuishou.sysmgr.ui.member;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.beans.Dish;
import com.shuishou.sysmgr.beans.IndentDetail;
import com.shuishou.sysmgr.printertool.PrintJob;
import com.shuishou.sysmgr.printertool.PrintQueue;
import com.shuishou.sysmgr.ConstantValue;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.Member;
import com.shuishou.sysmgr.beans.MemberBalance;
import com.shuishou.sysmgr.beans.MemberScore;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.CommonDialog;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.NumberInputDialog;

public class MemberQueryPanel extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Logger logger = Logger.getLogger(MemberQueryPanel.class.getName());

	private MainFrame mainFrame;
	private JTextField tfName = new JTextField();
	private JTextField tfMemberCard = new JTextField();
	private JTextField tfTelephone = new JTextField();
	private JTextField tfAddress = new JTextField();
	private JTextField tfPostcode = new JTextField();
	private JTable table = new JTable();
	private TableModel model = new TableModel();
	private JButton btnQuery = new JButton("Query");
	private JButton btnAdd = new JButton("Add");
	private JButton btnUpdate = new JButton("Update");
	private JButton btnDelete = new JButton("Delete");
	private JButton btnPrintTicket = new JButton("Print Ticket");
	private JButton btnUpdatePassword = new JButton("Update Password");
	private JButton btnResetPassword111111 = new JButton("Reset Password 111111");
	private JButton btnUpdateScore = new JButton("Update Score");
	private JButton btnScoreHistory = new JButton("Score Log");
	private JButton btnUpdateBalance = new JButton("Update Balance");
	private JButton btnBalanceHistory = new JButton("Balance Log");
	private JButton btnRecharge = new JButton("Recharge");
	
	private ArrayList<Member> members = new ArrayList<>();
	
	public MemberQueryPanel(MainFrame mainFrame){
		this.mainFrame = mainFrame;
		initUI();
	}
	
	private void initUI(){
		JLabel lbName = new JLabel("Name : ");
		JLabel lbMemberCard = new JLabel("Member Card : ");
		JLabel lbTelephone = new JLabel("Telephone : ");
		JLabel lbAddress = new JLabel("Address : ");
		JLabel lbPostcode = new JLabel("Postcode : ");
		tfName.setPreferredSize(new Dimension(120, 25));
		tfMemberCard.setPreferredSize(new Dimension(120, 25));
		tfTelephone.setPreferredSize(new Dimension(120, 25));
		tfAddress.setPreferredSize(new Dimension(120, 25));
		tfPostcode.setPreferredSize(new Dimension(120, 25));
		
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setPreferredWidth(100);
		table.getColumnModel().getColumn(8).setPreferredWidth(100);
		table.getColumnModel().getColumn(9).setPreferredWidth(100);
		table.setAutoCreateRowSorter(true);
		JScrollPane jspTable = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		JPanel pCondition = new JPanel(new GridBagLayout());
		pCondition.add(lbName, 		new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(tfName, 		new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(lbMemberCard,new GridBagConstraints(2, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(tfMemberCard,new GridBagConstraints(3, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(lbTelephone, new GridBagConstraints(4, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(tfTelephone, new GridBagConstraints(5, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(lbAddress, 	new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(tfAddress, 	new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(lbPostcode, 	new GridBagConstraints(2, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(tfPostcode, 	new GridBagConstraints(3, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(btnQuery, 	new GridBagConstraints(4, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		
		btnQuery.addActionListener(this);
		JPanel pButton1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pButton1.add(btnAdd);
		pButton1.add(btnUpdate);
		pButton1.add(btnDelete);
		pButton1.add(btnPrintTicket);
		pButton1.add(btnUpdateScore);
		pButton1.add(btnScoreHistory);
		JPanel pButton2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pButton2.add(btnUpdateBalance);
		pButton2.add(btnBalanceHistory);
		pButton2.add(btnRecharge);
		pButton2.add(btnUpdatePassword);
		pButton2.add(btnResetPassword111111);
		JPanel pButtons = new JPanel(new GridLayout(2, 1));
		pButtons.add(pButton1);
		pButtons.add(pButton2);
		
		btnAdd.addActionListener(this);
		btnUpdate.addActionListener(this);
		btnDelete.addActionListener(this);
		btnPrintTicket.addActionListener(this);
		btnUpdateScore.addActionListener(this);
		btnUpdateBalance.addActionListener(this);
		btnRecharge.addActionListener(this);
		btnBalanceHistory.addActionListener(this);
		btnScoreHistory.addActionListener(this);
		btnUpdatePassword.addActionListener(this);
		btnResetPassword111111.addActionListener(this);
		
		setLayout(new BorderLayout());
		add(jspTable, BorderLayout.CENTER);
		add(pCondition, BorderLayout.NORTH);
		add(pButtons, BorderLayout.SOUTH);
	}
	
	private void doQuery(){
		members.clear();
		((TableModel)table.getModel()).setRowCount(0);
		table.clearSelection();
		String url = "member/querymember";
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId() + "");
		if (tfName.getText() != null && tfName.getText().length() > 0)
			params.put("name",tfName.getText());
		if (tfMemberCard.getText() != null && tfMemberCard.getText().length() > 0)
			params.put("memberCard", tfMemberCard.getText());
		if (tfAddress.getText() != null && tfAddress.getText().length() > 0)
			params.put("address", tfAddress.getText());
		if (tfPostcode.getText() != null && tfPostcode.getText().length() > 0)
			params.put("postCode", tfPostcode.getText());
		if (tfTelephone.getText() != null && tfTelephone.getText().length() > 0)
			params.put("telephone", tfTelephone.getText());
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for query member. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for query member. URL = " + url);
			return;
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<ArrayList<Member>> result = gson.fromJson(response, new TypeToken<HttpResult<ArrayList<Member>>>(){}.getType());
		if (!result.success){
			logger.error("return false while query member. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		members = result.data;
		table.updateUI();
	}
	
	private void doAddMember(){
		MemberPanel p = new MemberPanel(this);
		CommonDialog dlg = new CommonDialog(mainFrame, p, "Add Member", 300, 400);
		dlg.setVisible(true);
	}

	private void doUpdateMember(){
		if (table.getSelectedRow() < 0)
			return;
		MemberPanel p = new MemberPanel(this);
		p.hidePassword();
		int modelRow = table.convertRowIndexToModel(table.getSelectedRow());
		p.setObjectValue(model.getObjectAt(modelRow));
		CommonDialog dlg = new CommonDialog(mainFrame, p, "Update Member", 300, 300);
		dlg.setVisible(true);
	}
	
	private void doDeleteMember(){
		if (table.getSelectedRow() < 0)
			return;
		int modelRow = table.convertRowIndexToModel(table.getSelectedRow());
		Member m = model.getObjectAt(modelRow);
		if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(this, "Will you delete the customer ["+m.getName()+"]?", "Confirm", JOptionPane.YES_NO_OPTION)){
			return;
		}
		String url = "member/deletemember";
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId() + "");
		params.put("id",String.valueOf(m.getId()));
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for delete member. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for delete member. URL = " + url);
			return;
		}
		Gson gson = new Gson();
		HttpResult<String> result = gson.fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
		if (!result.success){
			logger.error("return false while delete member. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		JOptionPane.showMessageDialog(this, "Delete member successfully.");
		members.remove(m);
		model.fireTableDataChanged();
	}
	
	private void doUpdateScore(){
		if (table.getSelectedRow() < 0)
			return;
		int modelRow = table.convertRowIndexToModel(table.getSelectedRow());
		Member m = model.getObjectAt(modelRow);
		NumberInputDialog dlg = new NumberInputDialog(mainFrame, "Update Score", "Member " + m.getName() + " current score is "+m.getScore()+ ", \nPlease input new score.", true);
		dlg.setVisible(true);
		if (!dlg.isConfirm){
			return;
		}
		double newscore = dlg.inputDouble;
		String url = "member/updatememberscore";
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId() + "");
		params.put("id",String.valueOf(m.getId()));
		params.put("newScore", String.valueOf(newscore));
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for update member score. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for update member score. URL = " + url);
			return;
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMDHMS).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			logger.error("return false while update member score. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		m.setScore(result.data.getScore());
		table.updateUI();//here is low efficient, buy using model.fireDataChange will occur an exception if existing a rowSorter.
	}
	
	private void doUpdateBalance(){
		if (table.getSelectedRow() < 0)
			return;
		int modelRow = table.convertRowIndexToModel(table.getSelectedRow());
		Member m = model.getObjectAt(modelRow);
		UpdateBalanceDialog dlg = new UpdateBalanceDialog(mainFrame, this, m);
		dlg.setVisible(true);
	}
	
	private void doRecharge(){
		if (table.getSelectedRow() < 0)
			return;
		int modelRow = table.convertRowIndexToModel(table.getSelectedRow());
		Member m = model.getObjectAt(modelRow);
		MemberRechargeDialog dlg = new MemberRechargeDialog(mainFrame, this, m);
		dlg.setVisible(true);
	}
	
	public void doPrintRechargeTicket(String printType, String memberCard, String memberName, double rechargeAmount, double balance, String payway, int printTimes){
		Map<String,String> keys = new HashMap<String, String>();
		keys.put("printType", printType);
		keys.put("dateTime", ConstantValue.DFYMDHMS.format(new Date()));
		keys.put("memberCard", memberCard);
		keys.put("memberName", memberName);
		keys.put("rechargeAmount", String.format(ConstantValue.FORMAT_DOUBLE, rechargeAmount));
		keys.put("balance", String.format(ConstantValue.FORMAT_DOUBLE,balance));
		keys.put("branchName", mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_BRANCHNAME));
		keys.put("payway", payway);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("keys", keys);
		PrintJob job = new PrintJob("/recharge_template.json", params, mainFrame.printerName);
		for (int i = 0; i < printTimes; i++) {
			PrintQueue.add(job);
		}
	}
	
	public void insertRow(Member m){
		members.add(0, m);
		model.fireTableDataChanged();
	}
	
	public void updateRow(Member m){
		for (int i = 0; i < members.size(); i++) {
			if (m.getId() == members.get(i).getId()){
				members.set(i, m);
				break;
			}
		}
		model.fireTableDataChanged();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnQuery){
			doQuery();
		} else if (e.getSource() == btnAdd){
			doAddMember();
		} else if (e.getSource() == btnUpdate){
			doUpdateMember();
		} else if (e.getSource() == btnDelete){
			doDeleteMember();
		} else if (e.getSource() == btnPrintTicket){
			if (table.getSelectedRow() < 0)
				return;
			int modelRow = table.convertRowIndexToModel(table.getSelectedRow());
			Member m = model.getObjectAt(modelRow);
			doPrintRechargeTicket("Customer Balance", m.getMemberCard(), m.getName(), 0, m.getBalanceMoney(), "", 1);
		} else if (e.getSource() == btnUpdateScore){
			doUpdateScore();
		} else if (e.getSource() == btnUpdateBalance){
			doUpdateBalance();
		} else if (e.getSource() == btnRecharge){
			doRecharge();
		} else if (e.getSource() == btnUpdatePassword){
			doChangePassword();
		} else if (e.getSource() == btnResetPassword111111){
			doResetPassword111111();
		} else if (e.getSource() == btnScoreHistory){
			if (table.getSelectedRow() < 0)
				return;
			int modelRow = table.convertRowIndexToModel(table.getSelectedRow());
			Member m = model.getObjectAt(modelRow);
			ArrayList<MemberScore> mss = HttpUtil.loadMemberScoreRecord(mainFrame, MainFrame.getLoginUser().getId(), m.getId());
			MemberScoreRecordDialog dlg = new MemberScoreRecordDialog(mainFrame, "Member Score", mss, 600, 400);
			dlg.setVisible(true);
		} else if (e.getSource() == btnBalanceHistory){
			if (table.getSelectedRow() < 0)
				return;
			int modelRow = table.convertRowIndexToModel(table.getSelectedRow());
			Member m = model.getObjectAt(modelRow);
			ArrayList<MemberBalance> mss = HttpUtil.loadMemberBalanceRecord(mainFrame, MainFrame.getLoginUser().getId(), m.getId());
			MemberBalanceRecordDialog dlg = new MemberBalanceRecordDialog(mainFrame, "Member Balance", mss, 600, 400);
			dlg.setVisible(true);
		}
	}
	
	private void doResetPassword111111(){
		if (table.getSelectedRow() < 0)
			return;
		int modelRow = table.convertRowIndexToModel(table.getSelectedRow());
		Member m = model.getObjectAt(modelRow);
		if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(this, "Will you update the password of customer ["+m.getName()+"] to 111111?", "Confirm", JOptionPane.YES_NO_OPTION)){
			return;
		}
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId() + "");
		params.put("id", m.getId() + "");
		String url = "member/resetmemberpassword111111";
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for change member password to 111111. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for change member password to 111111. URL = " + url);
			return;
		}
        Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			logger.error("return false while change member password to 111111. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		JOptionPane.showMessageDialog(this, "Update password successfully to 111111.");
	}
	
	private void doChangePassword(){
		if (table.getSelectedRow() < 0)
			return;
		int modelRow = table.convertRowIndexToModel(table.getSelectedRow());
		Member m = model.getObjectAt(modelRow);
		ChangePasswordDialog dlg = new ChangePasswordDialog(this.mainFrame, this, "Change Password", m);
		dlg.setVisible(true);
	}
	
	public JTable getTable() {
		return table;
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	class TableModel extends DefaultTableModel{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String[] header = new String[]{"Name", "Member Card", "Telephone", "Discount Rate", "Create Date","Score", "Balance Money","Address", "Postcode",  "Birthday"};
		
		public TableModel(){

		}
		@Override
		public int getRowCount() {
			if (members == null) return 0;
			return members.size();
		}

		@Override
		public int getColumnCount() {
			return header.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Member m = members.get(rowIndex);
			switch(columnIndex){
			case 0:
				return m.getName();
			case 1: 
				return m.getMemberCard();
			case 2:
				return m.getTelephone();
			case 3:
				return m.getDiscountRate();
			case 4:
				return ConstantValue.DFYMD.format(m.getCreateTime());
			case 5: 
				return m.getScore();
			case 6:
				return m.getBalanceMoney();
			case 7:
				return m.getAddress();
			case 8:
				return m.getPostCode();
			case 9:
				if (m.getBirth() == null) return "";
				return ConstantValue.DFYMD.format(m.getBirth());
			}
			return "";
		}
		
		@Override
		public String getColumnName(int col){
			return header[col];
		}
		
		public Member getObjectAt(int row){
			return members.get(row);
		}
		
		@Override
		public Class<?> getColumnClass(int col){
			switch(col){
			case 2:
				return Long.class;
			case 3:
				return Double.class;
			case 5: 
				return Double.class;
			case 6:
				return Double.class;
			}
			return String.class;
		}
		
		public boolean isCellEditable(int row, int column) {
        return false;
    }
	}
}
