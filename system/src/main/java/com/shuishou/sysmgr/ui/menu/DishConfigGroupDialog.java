package com.shuishou.sysmgr.ui.menu;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.beans.Dish;
import com.shuishou.sysmgr.beans.DishConfig;
import com.shuishou.sysmgr.beans.DishConfigGroup;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.CommonDialog;
import com.shuishou.sysmgr.ui.MainFrame;

public class DishConfigGroupDialog extends JDialog implements ActionListener{

	private final Logger logger = Logger.getLogger(Category2Panel.class.getName());
	private MenuMgmtPanel parent;
	private Dish dish;
	private ArrayList<DishConfigGroup> configGroupList;
	private JTable tableGroup = new JTable(); 
	private ConfigGroupModel modelGroup  = new ConfigGroupModel();
	private JButton btnAddGroup = new JButton("ADD");
	private JButton btnModifyGroup = new JButton("MODIFY");
	private JButton btnDeleteGroup = new JButton("DELETE");
	private JButton btnChooseGroup = new JButton("Choose");
	private JButton btnCloseDialog = new JButton("Close");
	
	public DishConfigGroupDialog(MenuMgmtPanel parent, Dish dish, ArrayList<DishConfigGroup> allGroups, int width, int height){
		this.parent = parent;
		this.dish = dish;
		configGroupList = allGroups;
		this.setModal(true);
		this.setTitle("Config Dish - " + dish.getFirstLanguageName());
		initUI();
		setSize(width, height);
		this.setLocation((int)(parent.getMainFrame().getWidth() / 2 - this.getWidth() /2 + parent.getMainFrame().getLocation().getX()), 
				(int)(parent.getMainFrame().getHeight() / 2 - this.getHeight() / 2 + parent.getMainFrame().getLocation().getY()));
	}
	
	private void initUI(){
		tableGroup.setModel(modelGroup);
		tableGroup.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableGroup.getColumnModel().getColumn(0).setPreferredWidth(200);
		tableGroup.getColumnModel().getColumn(1).setPreferredWidth(200);
		tableGroup.getColumnModel().getColumn(2).setPreferredWidth(200);
		tableGroup.getColumnModel().getColumn(3).setPreferredWidth(120);
		tableGroup.getColumnModel().getColumn(4).setPreferredWidth(120);
		JScrollPane jspTableGroup = new JScrollPane(tableGroup, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		tableGroup.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JPanel pBtnGroup = new JPanel();
		pBtnGroup.add(btnAddGroup);
		pBtnGroup.add(btnModifyGroup);
		pBtnGroup.add(btnDeleteGroup);
		pBtnGroup.add(btnChooseGroup);
		pBtnGroup.add(btnCloseDialog);
		
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(jspTableGroup, BorderLayout.CENTER);
		c.add(pBtnGroup, BorderLayout.SOUTH);
		
		btnAddGroup.addActionListener(this);
		btnModifyGroup.addActionListener(this);
		btnDeleteGroup.addActionListener(this);
		btnChooseGroup.addActionListener(this);
		btnCloseDialog.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAddGroup){
			doAddGroup();
		} else if (e.getSource() == btnModifyGroup){
			doModifyGroup();
		} else if (e.getSource() == btnDeleteGroup){
			doDeleteGroup();
		} else if (e.getSource() == btnChooseGroup){
			doChooseGroup();
		} else if (e.getSource() == btnCloseDialog){
			this.setVisible(false);
		}
	}
	
	private void doChooseGroup() {
		if (tableGroup.getSelectedRow() < 0)
			return;
		DishConfigGroup group = modelGroup.getObjectAt(tableGroup.getSelectedRow());
		if (JOptionPane.NO_OPTION == 
				JOptionPane.showConfirmDialog(this, "Do you want to add config group " + group.getFirstLanguageName() +" to Dish : "+ dish.getFirstLanguageName(), 
						"Confirm", JOptionPane.YES_NO_OPTION)){
			return;
		}
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("dishId", dish.getId() +"");
		params.put("configGroupId", modelGroup.getObjectAt(tableGroup.getSelectedRow()).getId() + "");
		String url = "menu/movein_configgroup_fordish";
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for add config group into dish. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for add config group into dish. URL = " + url);
			return;
		}
		Gson gson = new Gson();
		HttpResult<Dish> result = gson.fromJson(response, new TypeToken<HttpResult<Dish>>(){}.getType());
		if (!result.success){
			logger.error("return false while add config group into dish. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		this.setVisible(false);
		parent.insertNode(dish, modelGroup.getObjectAt(tableGroup.getSelectedRow()));
		result.data.setCategory2(dish.getCategory2());//fill up dish's parent
		parent.updateNode(result.data, dish);
	}

	public void addConfigGroupToList(DishConfigGroup cg){
		configGroupList.add(cg);
		modelGroup.fireTableDataChanged();
	}
	
	public void updateConfigGroupInList(DishConfigGroup cg){
		for (int i = 0; i < configGroupList.size(); i++) {
			if (cg.getId() == configGroupList.get(i).getId()){
				configGroupList.set(i, cg);
				modelGroup.fireTableRowsUpdated(i, i);
				break;
			}
		}
	}

	private void doDeleteGroup() {
		if (tableGroup.getSelectedRow() < 0)
			return;
		DishConfigGroup group = modelGroup.getObjectAt(tableGroup.getSelectedRow());
		if (JOptionPane.NO_OPTION == 
				JOptionPane.showConfirmDialog(this, "Do you want to delete config group " + group.getFirstLanguageName(), 
						"Confirm", JOptionPane.YES_NO_OPTION)){
			return;
		}
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("id", group.getId() +"");
		String url = "menu/delete_dishconfiggroup";
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for delete config group. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for delete config group. URL = " + url);
			return;
		}
		Gson gson = new Gson();
		HttpResult<Dish> result = gson.fromJson(response, new TypeToken<HttpResult<Dish>>(){}.getType());
		if (!result.success){
			logger.error("return false while delete config group. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		JOptionPane.showMessageDialog(this, "Delete config group successfully");
		configGroupList.remove(tableGroup.getSelectedRow());
		modelGroup.fireTableDataChanged();
	}

	private void doModifyGroup() {
		if (tableGroup.getSelectedRow() < 0)
			return;
		DishConfigGroupPanel p = new DishConfigGroupPanel(this, parent);
		p.setObjectValue(modelGroup.getObjectAt(tableGroup.getSelectedRow()));
		CommonDialog dlg = new CommonDialog(parent.getMainFrame(), p, "Modify Dish Config Group", 300, 300);
		dlg.setVisible(true);
	}

	private void doAddGroup() {
		DishConfigGroupPanel p = new DishConfigGroupPanel(this, parent);
		CommonDialog dlg = new CommonDialog(parent.getMainFrame(), p, "Add Dish Config Group", 400, 300);
		dlg.setVisible(true);
	}

	class ConfigGroupModel extends DefaultTableModel{
		private String[] header = new String[]{"Unique Name", "1st Language Name","2nd Language Name", "Required Quantity", "Allow Duplicate", "Sequence"};
		
		public ConfigGroupModel(){
			
		}
		
		public String getColumnName(int column) {
			return header[column];
		}
		
		@Override
		public int getRowCount() {
			if (configGroupList == null) return 0;
			return configGroupList.size();
		}
		
		@Override
		public int getColumnCount() {
			return header.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			DishConfigGroup cg = configGroupList.get(rowIndex);
			switch(columnIndex){
			case 0:
				return cg.getUniqueName();
			case 1:
				return cg.getFirstLanguageName();
			case 2: 
				return cg.getSecondLanguageName();
			case 3:
				return cg.getRequiredQuantity();
			case 4:
				return cg.isAllowDuplicate();
			case 5:
				return cg.getSequence();
			
			}
			return "";
		}
		
		public DishConfigGroup getObjectAt(int row){
			return configGroupList.get(row);
		}
	}
	
	class ConfigModel extends DefaultTableModel{
		private String[] header = new String[]{"1st Language Name","2nd Language Name", "Sequence", "Price Adjust"};
		private ArrayList<DishConfig> data;
		public ConfigModel(){
			
		}
		
		public String getColumnName(int column) {
			return header[column];
		}
		
		public void setData(ArrayList<DishConfig> dcs){
			data = dcs;
		}
		
		@Override
		public int getRowCount() {
			if (data == null) return 0;
			return data.size();
		}
		
		@Override
		public int getColumnCount() {
			return header.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			DishConfig dc = data.get(rowIndex);
			switch(columnIndex){
			case 0:
				return dc.getFirstLanguageName();
			case 1: 
				return dc.getSecondLanguageName();
			case 2:
				return dc.getSequence();
			case 3:
				return dc.getPrice();
			}
			return "";
		}
		
		public DishConfig getObjectAt(int row){
			return data.get(row);
		}
	}
}
