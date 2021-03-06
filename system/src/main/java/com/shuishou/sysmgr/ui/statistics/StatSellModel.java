package com.shuishou.sysmgr.ui.statistics;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.StatItem;

public class StatSellModel extends AbstractTableModel{
	private String[] header = new String[]{
			Messages.getString("StatSellModel.Header.ObjName"),
			Messages.getString("StatModel.Header.Price"), 
			Messages.getString("StatModel.Header.Amount"), 
			Messages.getString("StatModel.Header.Weight")};
	
	private ArrayList<StatItem> statItems;
	public StatSellModel(ArrayList<StatItem> statItems){
		this.statItems = statItems;
	}
	@Override
	public int getRowCount() {
		if (statItems == null) return 0;
		return statItems.size();
	}

	@Override
	public int getColumnCount() {
		return header.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		StatItem item = statItems.get(rowIndex);
		switch(columnIndex){
		case 0:
			return item.itemName;
		case 1: 
			return item.totalPrice;
		case 2:
			return item.soldAmount;
		case 3:
			return item.weight;
		}
		return "";
	}
	
	@Override
	public String getColumnName(int col){
		return header[col];
	}
	
	public void setData(ArrayList<StatItem> statItems){
		this.statItems = statItems;
	}
	
	public StatItem getObjectAt(int row){
		return statItems.get(row);
	}
	
	
}
