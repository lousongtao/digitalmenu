package com.shuishou.sysmgr.ui.desk;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.Desk;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.Permission;
import com.shuishou.sysmgr.beans.UserData;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.MainFrame;

public class DeskMgmtPanel extends JPanel implements ActionListener{
	private final Logger logger = Logger.getLogger(DeskMgmtPanel.class.getName());
	private MainFrame mainFrame;
	private JTable tableDesk = new JTable();
	private DeskTableModel modelDesk;
	private JButton btnAdd = new JButton(Messages.getString("DeskMgmtPanel.Add"));
	private JButton btnModify = new JButton(Messages.getString("DeskMgmtPanel.Modify"));
	private JButton btnDelete = new JButton(Messages.getString("DeskMgmtPanel.Delete"));
	private ArrayList<Desk> deskList;
	public DeskMgmtPanel(MainFrame mainFrame, ArrayList<Desk> deskList){
		this.mainFrame = mainFrame;
		this.deskList = deskList;
		initUI();
	}
	
	private void initUI(){
		modelDesk = new DeskTableModel();
		tableDesk.setModel(modelDesk);
		tableDesk.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableDesk.getColumnModel().getColumn(0).setPreferredWidth(20);
		tableDesk.getColumnModel().getColumn(1).setPreferredWidth(20);
		tableDesk.getColumnModel().getColumn(2).setPreferredWidth(50);
		JScrollPane jspTable = new JScrollPane(tableDesk, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		JPanel pButtons = new JPanel();
		pButtons.add(btnAdd);
		pButtons.add(btnModify);
		pButtons.add(btnDelete);
		
		btnAdd.addActionListener(this);
		btnModify.addActionListener(this);
		btnDelete.addActionListener(this);
		this.setLayout(new BorderLayout());
		add(jspTable, BorderLayout.CENTER);
		add(pButtons, BorderLayout.SOUTH);
	}
	
	public void refreshData(){
		deskList = mainFrame.loadDeskList();
		tableDesk.updateUI();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAdd){
			DeskDialog dlg = new DeskDialog(mainFrame, this, "Add Desk", null);
			dlg.setVisible(true);
		} else if (e.getSource() == btnModify){
			if (tableDesk.getSelectedRow() < 0)
				return;
			Desk desk = modelDesk.getObjectAt(tableDesk.getSelectedRow());
			DeskDialog dlg = new DeskDialog(mainFrame, this, "Modify Desk", desk);
			dlg.setVisible(true);
		} else if (e.getSource() == btnDelete){
			if (tableDesk.getSelectedRow() < 0)
				return;
			if (JOptionPane.showConfirmDialog(this, 
					"Do you want to delete desk : " + modelDesk.getObjectAt(tableDesk.getSelectedRow()).getName() + " ?",
					"Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){
				return;
			}
			Map<String, String> params = new HashMap<>();
			params.put("userId", MainFrame.getLoginUser().getId() + "");
			params.put("id", modelDesk.getObjectAt(tableDesk.getSelectedRow()).getId()+"");
			String url = "common/deletedesk";
			String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
			if (response == null){
				logger.error("get null from server for remove desk. URL = " + url + ", param = "+ params);
				JOptionPane.showMessageDialog(this, "get null from server for remove desk. URL = " + url);
				return;
			}
			Gson gson = new Gson();
			HttpResult<String> result = gson.fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
			if (!result.success){
				logger.error("return false while remove desk. URL = " + url + ", response = "+response);
				JOptionPane.showMessageDialog(this, result.result);
				return;
			}
			refreshData();
		} 
	}
	
	class DeskTableModel extends AbstractTableModel{

		private String[] header = new String[]{"ID","Name","Sequence"};
		
		public DeskTableModel(){

		}
		@Override
		public int getRowCount() {
			if (deskList == null) return 0;
			return deskList.size();
		}

		@Override
		public int getColumnCount() {
			return header.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Desk desk = deskList.get(rowIndex);
			switch(columnIndex){
			case 0:
				return desk.getId();
			case 1: 
				return desk.getName();
			case 2:
				return desk.getSequence();
			}
			return "";
		}
		
		@Override
		public String getColumnName(int col){
			return header[col];
		}
		
		public Desk getObjectAt(int row){
			return deskList.get(row);
		}
	}

	
}
