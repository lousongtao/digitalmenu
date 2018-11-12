package com.shuishou.sysmgr.ui.menu;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.beans.Category1;
import com.shuishou.sysmgr.beans.Category2;
import com.shuishou.sysmgr.beans.Dish;
import com.shuishou.sysmgr.beans.DishConfig;
import com.shuishou.sysmgr.beans.DishConfigGroup;
import com.shuishou.sysmgr.beans.DishMaterialConsume;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.Material;
import com.shuishou.sysmgr.beans.MaterialCategory;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.CommonDialog;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.NumberTextField;

public class DishMaterialConsumeMgmtDialog extends JDialog implements ActionListener{

	private final Logger logger = Logger.getLogger(DishConfigGroupDialog.class.getName());
	private int modifiedDishMaterialConsumeId;//一个缓存变量, 只是用来修改对象时保存id
	private MainFrame parent;
	private Dish dish;
	private JTable table = new JTable();
	private DishMaterialConsumeModel model  = new DishMaterialConsumeModel();
//	private ArrayList<Category1> category1List;
	private ArrayList<MaterialCategory> materialCategoryList;
	private ArrayList<DishMaterialConsume> dishMaterialConsumeList;
	private JButton btnAdd = new JButton("ADD");
	private JButton btnModify = new JButton("MODIFY");
	private JButton btnDelete = new JButton("DELETE");
	private JButton btnCloseDialog = new JButton("Close");
	private JButton btnSaveModify = new JButton("SAVE MODIFIED");
	private JButton btnCancelModify = new JButton("CANCEL MODIFITY");
//	private JComboBox<Category1> cbCategory1 = new JComboBox<>();
//	private JComboBox<Category2> cbCategory2 = new JComboBox<>();
//	private JComboBox<Dish> cbDish = new JComboBox<>();
	private JComboBox<MaterialCategory> cbMaterialCategory = new JComboBox<>();
	private JComboBox<Material> cbMaterial = new JComboBox<>();
	private NumberTextField tfAmount = new NumberTextField(true);
	
	public DishMaterialConsumeMgmtDialog(MainFrame parent, Dish dish, int width, int height, ArrayList<MaterialCategory> materialCategoryList, ArrayList<DishMaterialConsume> dishMaterialConsumeList){
		this.parent = parent;
		this.dish = dish;
		this.setModal(true);
		this.setTitle("Config Dish Material Consume - " + dish.getFirstLanguageName());
//		this.category1List = category1List;
		this.materialCategoryList = materialCategoryList;
		this.dishMaterialConsumeList = dishMaterialConsumeList;
		initUI();
		initData();
		setSize(width, height);
		this.setLocation((int)(parent.getWidth() / 2 - this.getWidth() /2 + parent.getLocation().getX()), 
				(int)(parent.getHeight() / 2 - this.getHeight() / 2 + parent.getLocation().getY()));
	}
	
	private void initData(){
//		cbCategory1.removeAllItems();
		cbMaterialCategory.removeAllItems();
//		for (int i = 0; i < category1List.size(); i++) {
//			cbCategory1.addItem(category1List.get(i));
//		}
		for (int i = 0; i < materialCategoryList.size(); i++) {
			cbMaterialCategory.addItem(materialCategoryList.get(i));
		}
	}
	private void initUI(){
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getColumnModel().getColumn(0).setPreferredWidth(200);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		JScrollPane jspTable = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tfAmount.setMinimumSize(new Dimension(120,25));
//		cbMaterial.setRenderer(new MaterialListRender());
//		cbMaterialCategory.setRenderer(new MaterialCategoryListRender());
		JLabel lbCategory1 = new JLabel("Category1");
		JLabel lbCategory2 = new JLabel("Category2");
		JLabel lbDish = new JLabel("Dish");
		JLabel lbMaterialCategory = new JLabel("Material Category");
		JLabel lbMaterial = new JLabel("Material");
		JLabel lbAmount = new JLabel("Amount");
		JPanel pDishMaterial = new JPanel(new GridBagLayout());
//		pDishMaterial.add(lbCategory1, 		new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
//		pDishMaterial.add(cbCategory1, 		new GridBagConstraints(1, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
//		pDishMaterial.add(lbCategory2, 		new GridBagConstraints(2, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
//		pDishMaterial.add(cbCategory2, 		new GridBagConstraints(3, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
//		pDishMaterial.add(lbDish, 			new GridBagConstraints(4, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
//		pDishMaterial.add(cbDish, 			new GridBagConstraints(5, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pDishMaterial.add(lbMaterialCategory,new GridBagConstraints(0,1, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		pDishMaterial.add(cbMaterialCategory,new GridBagConstraints(1,1, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));
		pDishMaterial.add(lbMaterial, 		new GridBagConstraints(2, 1, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		pDishMaterial.add(cbMaterial, 		new GridBagConstraints(3, 1, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));
		pDishMaterial.add(lbAmount, 		new GridBagConstraints(4, 1, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		pDishMaterial.add(tfAmount, 		new GridBagConstraints(5, 1, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));
		JPanel pButton = new JPanel();
		pButton.add(btnAdd);
		pButton.add(btnModify);
		pButton.add(btnDelete);
		pButton.add(btnCloseDialog);
		pButton.add(btnSaveModify);
		pButton.add(btnCancelModify);
		btnSaveModify.setVisible(false);
		btnCancelModify.setVisible(false);
		
		Container c = this.getContentPane();
		c.setLayout(new GridBagLayout());
		c.add(jspTable, 		new GridBagConstraints(0,0, 1, 1,1,1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10,0,0,0), 0, 0));
		c.add(pDishMaterial, 	new GridBagConstraints(0,1, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));
		c.add(pButton,  		new GridBagConstraints(0,2, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));
		
		
		btnAdd.addActionListener(this);
		btnModify.addActionListener(this);
		btnDelete.addActionListener(this);
		btnCloseDialog.addActionListener(this);
		btnSaveModify.addActionListener(this);
		btnCancelModify.addActionListener(this);
		
//		cbCategory1.addActionListener(this);
//		cbCategory2.addActionListener(this);
//		cbDish.addActionListener(this);
		cbMaterialCategory.addActionListener(this);
		cbMaterial.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAdd){
			doAdd();
		} else if (e.getSource() == btnModify){
			doModify();
		} else if (e.getSource() == btnDelete){
			doDelete();
		} else if (e.getSource() == btnCloseDialog){
			this.setVisible(false);
		} else if (e.getSource() == btnSaveModify){
			doSaveModify();
		} else if (e.getSource() == btnCancelModify){
			doCancelModify();
		} 
//		else if (e.getSource() == cbCategory1){
//			doChangeCategory1();
//		} else if (e.getSource() == cbCategory2){
//			doChangeCategory2();
//		} else if (e.getSource() == cbDish){
//		} 
		else if (e.getSource() == cbMaterialCategory){
			doChangeMaterialCategory();
		} else if (e.getSource() == cbMaterial){
		} 
	}
//	private void doChangeCategory1(){
//		cbCategory2.removeAllItems();
//		cbDish.removeAllItems();
//		Category1 c1 = (Category1) cbCategory1.getSelectedItem();
//		if (c1.getCategory2s() != null){
//			for (int i = 0; i < c1.getCategory2s().size(); i++) {
//				cbCategory2.addItem(c1.getCategory2s().get(i));
//			}
//		}
//	}
//	
//	private void doChangeCategory2(){
//		cbDish.removeAllItems();
//		Category2 c2 = (Category2) cbCategory2.getSelectedItem();
//		if (c2.getDishes() != null){
//			for (int i = 0; i < c2.getDishes().size(); i++) {
//				cbDish.addItem(c2.getDishes().get(i));
//			}
//		}
//	}
	
	private void doChangeMaterialCategory(){
		cbMaterial.removeAllItems();
		MaterialCategory mc = (MaterialCategory) cbMaterialCategory.getSelectedItem();
		if (mc != null && mc.getMaterials() != null){
			for (int i = 0; i < mc.getMaterials().size(); i++) {
				cbMaterial.addItem(mc.getMaterials().get(i));
			}
		}
	}

	private void doDelete() {
		if (table.getSelectedRow() < 0)
			return;
		DishMaterialConsume csm = model.getObjectAt(table.getSelectedRow());
		if (JOptionPane.NO_OPTION == 
				JOptionPane.showConfirmDialog(this, "Do you want to delete " + csm.getMaterial().getName(), 
						"Confirm", JOptionPane.YES_NO_OPTION)){
			return;
		}
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("id", csm.getId() +"");
		String url = "menu/deletedishmaterialconsume";
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for delete DishMaterialConsume. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for delete DishMaterialConsume. URL = " + url);
			return;
		}
		Gson gson = new Gson();
		HttpResult<Dish> result = gson.fromJson(response, new TypeToken<HttpResult<Dish>>(){}.getType());
		if (!result.success){
			logger.error("return false while delete DishMaterialConsume. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		JOptionPane.showMessageDialog(this, "Delete item successfully");
		dishMaterialConsumeList.remove(csm);
		model.fireTableDataChanged();
		
	}

	/**
	 * initial the combobox components data;
	 * visible the save button
	 */
	private void doModify() {
		if (table.getSelectedRow() < 0)
			return;
		DishMaterialConsume csm = model.getObjectAt(table.getSelectedRow());
//		Category1 c1 = csm.getDish().getCategory2().getCategory1();
//		cbCategory1.setSelectedItem(c1);
//		cbCategory2.setSelectedItem(csm.getDish().getCategory2());
//		cbDish.setSelectedItem(csm.getDish());
		cbMaterialCategory.setEnabled(false);
		cbMaterial.setEnabled(false);
		tfAmount.setText(csm.getAmount()+"");
		modifiedDishMaterialConsumeId = csm.getId();
		btnAdd.setVisible(false);
		btnModify.setVisible(false);
		btnCloseDialog.setVisible(false);
		btnDelete.setVisible(false);
		btnSaveModify.setVisible(true);
		btnCancelModify.setVisible(true);
	}
	
	private void doCancelModify() {
//		cbCategory1.setSelectedIndex(-1);
//		cbCategory2.setSelectedIndex(-1);
//		cbDish.setSelectedIndex(-1);
		cbMaterialCategory.setSelectedIndex(-1);
		cbMaterial.setSelectedIndex(-1);
		cbMaterialCategory.setEnabled(true);
		cbMaterial.setEnabled(true);
		tfAmount.setText("");
		btnAdd.setVisible(true);
		btnModify.setVisible(true);
		btnCloseDialog.setVisible(true);
		btnDelete.setVisible(true);
		btnSaveModify.setVisible(false);
		btnCancelModify.setVisible(false);
	}

	private void doSaveModify() {
		if (tfAmount.getText() == null || tfAmount.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Must input the amount");
			return ;
		}
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("id", modifiedDishMaterialConsumeId+"");
		params.put("amount", tfAmount.getText());
		String url = "menu/updatedishmaterialconsume";
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for add Dish Material Consume. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for add Dish Material Consume. URL = " + url);
			return;
		}
		Gson gson = new Gson();
		HttpResult<DishMaterialConsume> result = gson.fromJson(response, new TypeToken<HttpResult<DishMaterialConsume>>(){}.getType());
		if (!result.success){
			logger.error("return false while add Dish Material Consume. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		JOptionPane.showMessageDialog(this, "Update item successfully");
		for (int i = 0; i < dishMaterialConsumeList.size(); i++) {
			if (dishMaterialConsumeList.get(i).getId() == modifiedDishMaterialConsumeId){
				dishMaterialConsumeList.set(i, result.data);
				break;
			}
		}
		model.fireTableDataChanged();
		cbMaterialCategory.setSelectedIndex(-1);
		cbMaterial.setSelectedIndex(-1);
		cbMaterialCategory.setEnabled(true);
		cbMaterial.setEnabled(true);
		tfAmount.setText("");
		btnAdd.setVisible(true);
		btnModify.setVisible(true);
		btnCloseDialog.setVisible(true);
		btnDelete.setVisible(true);
		btnSaveModify.setVisible(false);
		btnCancelModify.setVisible(false);
	}
	
	private void doAdd() {
		if (!checkData())
			return;
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("dishId", dish.getId() +"");
		params.put("amount", tfAmount.getText());
		params.put("materialId", ((Material)cbMaterial.getSelectedItem()).getId() + "");
		String url = "menu/adddishmaterialconsume";
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for add Dish Material Consume. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for add Dish Material Consume. URL = " + url);
			return;
		}
		Gson gson = new Gson();
		HttpResult<DishMaterialConsume> result = gson.fromJson(response, new TypeToken<HttpResult<DishMaterialConsume>>(){}.getType());
		if (!result.success){
			logger.error("return false while add Dish Material Consume. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		JOptionPane.showMessageDialog(this, "Add item successfully");
		dishMaterialConsumeList.add(result.data);
		model.fireTableDataChanged();
	}
	
	private boolean checkData(){
		if (cbMaterial.getSelectedIndex() < 0){
			JOptionPane.showMessageDialog(this, "Must select a Material");
			return false;
		}
		if (tfAmount.getText() == null || tfAmount.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Must input the amount");
			return false;
		}
		return true;
	}

	class DishMaterialConsumeModel extends DefaultTableModel{
		private String[] header = new String[]{"Material Name","Amount"};
		
		public DishMaterialConsumeModel(){
			
		}
		
		public String getColumnName(int column) {
			return header[column];
		}
		
		@Override
		public int getRowCount() {
			if (dishMaterialConsumeList == null) return 0;
			return dishMaterialConsumeList.size();
		}
		
		@Override
		public int getColumnCount() {
			return header.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			DishMaterialConsume dmConsume = dishMaterialConsumeList.get(rowIndex);
			switch(columnIndex){
			case 0:
				return dmConsume.getMaterial().getName();
			case 1: 
				return dmConsume.getAmount();
			}
			return "";
		}
		
		public DishMaterialConsume getObjectAt(int row){
			return dishMaterialConsumeList.get(row);
		}
	}
	
	class MaterialCategoryListRender extends JLabel implements ListCellRenderer{
		
		public MaterialCategoryListRender(){}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			if (value != null)
				setText(((MaterialCategory)value).getName());
			return this;
		}
	}
	
	class MaterialListRender extends JLabel implements ListCellRenderer{
		
		public MaterialListRender(){}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			if (value != null)
				setText(((Material)value).getName());
			return this;
		}
	}
}
