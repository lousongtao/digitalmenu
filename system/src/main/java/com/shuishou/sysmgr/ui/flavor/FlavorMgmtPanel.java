package com.shuishou.sysmgr.ui.flavor;

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
import com.shuishou.sysmgr.beans.Flavor;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.Permission;
import com.shuishou.sysmgr.beans.UserData;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.MainFrame;

public class FlavorMgmtPanel extends JPanel implements ActionListener{
	private final Logger logger = Logger.getLogger(FlavorMgmtPanel.class.getName());
	private MainFrame mainFrame;
	private JTable table = new JTable();
	private FlavorTableModel model;
	private JButton btnAdd = new JButton(Messages.getString("DeskMgmtPanel.Add"));
	private JButton btnModify = new JButton(Messages.getString("DeskMgmtPanel.Modify"));
	private JButton btnDelete = new JButton(Messages.getString("DeskMgmtPanel.Delete"));
	private ArrayList<Flavor> flavorList;
	public FlavorMgmtPanel(MainFrame mainFrame, ArrayList<Flavor> flavorList){
		this.mainFrame = mainFrame;
		this.flavorList = flavorList;
		initUI();
	}
	
	private void initUI(){
		model = new FlavorTableModel();
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(20);
		table.getColumnModel().getColumn(2).setPreferredWidth(50);
		JScrollPane jspTable = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
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
		flavorList = mainFrame.loadFlavorList();
		table.updateUI();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAdd){
			FlavorDialog dlg = new FlavorDialog(mainFrame, this, "Add Flavor", null);
			dlg.setVisible(true);
		} else if (e.getSource() == btnModify){
			if (table.getSelectedRow() < 0)
				return;
			Flavor flavor= model.getObjectAt(table.getSelectedRow());
			FlavorDialog dlg = new FlavorDialog(mainFrame, this, "Modify Flavor", flavor);
			dlg.setVisible(true);
		} else if (e.getSource() == btnDelete){
			if (table.getSelectedRow() < 0)
				return;
			if (JOptionPane.showConfirmDialog(this, 
					"Do you want to delete flavor: " + model.getObjectAt(table.getSelectedRow()).getFirstLanguageName() + " ?",
					"Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){
				return;
			}
			Map<String, String> params = new HashMap<>();
			params.put("userId", MainFrame.getLoginUser().getId() + "");
			params.put("id", model.getObjectAt(table.getSelectedRow()).getId()+"");
			String url = "menu/deleteflavor";
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
	
	class FlavorTableModel extends AbstractTableModel{

		private String[] header = new String[]{"ID","First Language Name","Second LanguageName"};
		
		@Override
		public int getRowCount() {
			if (flavorList == null) return 0;
			return flavorList.size();
		}

		@Override
		public int getColumnCount() {
			return header.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Flavor flavor = flavorList.get(rowIndex);
			switch(columnIndex){
			case 0:
				return flavor.getId();
			case 1: 
				return flavor.getFirstLanguageName();
			case 2:
				return flavor.getSecondLanguageName();
			}
			return "";
		}
		
		@Override
		public String getColumnName(int col){
			return header[col];
		}
		
		public Flavor getObjectAt(int row){
			return flavorList.get(row);
		}
	}

	
}
