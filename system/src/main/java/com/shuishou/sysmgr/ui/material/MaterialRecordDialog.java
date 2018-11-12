package com.shuishou.sysmgr.ui.material;

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
import com.shuishou.sysmgr.ConstantValue;
import com.shuishou.sysmgr.beans.Dish;
import com.shuishou.sysmgr.beans.DishConfig;
import com.shuishou.sysmgr.beans.DishConfigGroup;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.Material;
import com.shuishou.sysmgr.beans.MaterialRecord;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.CommonDialog;
import com.shuishou.sysmgr.ui.MainFrame;

public class MaterialRecordDialog extends JDialog implements ActionListener{

	private final Logger logger = Logger.getLogger(MaterialRecordDialog.class.getName());
	private MainFrame parent;
	private JTable table = new JTable(); 
	private MaterialRecordModel model  = new MaterialRecordModel();
	private JButton btnCloseDialog = new JButton("Close");
	
	public MaterialRecordDialog(MainFrame parent, Material material, int width, int height){
		this.parent = parent;
		this.setModal(true);
		this.setTitle("Material Record - " + material.getName());
		initUI();
		setSize(width, height);
		this.setLocation((int)(parent.getWidth() / 2 - this.getWidth() /2 + parent.getLocation().getX()), 
				(int)(parent.getHeight() / 2 - this.getHeight() / 2 + parent.getLocation().getY()));
		initData(material);
	}
	
	private void initUI(){
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getColumnModel().getColumn(0).setPreferredWidth(180);
		table.getColumnModel().getColumn(1).setPreferredWidth(120);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(120);
		JScrollPane jsptable = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JPanel pBtnGroup = new JPanel();
		pBtnGroup.add(btnCloseDialog);
		
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(jsptable, BorderLayout.CENTER);
		c.add(pBtnGroup, BorderLayout.SOUTH);
		
		btnCloseDialog.addActionListener(this);
	}
	
	private void initData(Material m){
		ArrayList<MaterialRecord> records = HttpUtil.loadMaterialRecordByMaterial(parent, m.getId());
		model.setData(records);
		model.fireTableDataChanged();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCloseDialog){
			this.setVisible(false);
		}
	}
	

	class MaterialRecordModel extends DefaultTableModel{
		private String[] header = new String[]{"Time", "Type","Amount", "Left Amount", "Operator"};
		private ArrayList<MaterialRecord> records;
		public MaterialRecordModel(){
			
		}
		
		public String getColumnName(int column) {
			return header[column];
		}
		
		@Override
		public int getRowCount() {
			if (records == null) return 0;
			return records.size();
		}
		
		@Override
		public int getColumnCount() {
			return header.length;
		}
		
		public void setData(ArrayList<MaterialRecord> records){
			this.records = records;
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			MaterialRecord mr = records.get(rowIndex);
			switch(columnIndex){
			case 0:
				return ConstantValue.DFYMDHMS.format(mr.getDate());
			case 1:
				if (mr.getType() == ConstantValue.MATERIALRECORD_TYPE_PURCHASE)
					return "PURCHASE";
				else if (mr.getType() == ConstantValue.MATERIALRECORD_TYPE_SELLDISH)
					return "SELLDISH";
				else if (mr.getType() == ConstantValue.MATERIALRECORD_TYPE_CHANGEAMOUNT)
					return "CHANGEAMOUNT";
			case 2: 
				return mr.getAmount();
			case 3:
				return mr.getLeftAmount();
			case 4:
				return mr.getOperator();
			
			}
			return "";
		}
		
	}
	
}
