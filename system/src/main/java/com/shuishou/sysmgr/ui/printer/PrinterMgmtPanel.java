package com.shuishou.sysmgr.ui.printer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.ConstantValue;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.Desk;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.Permission;
import com.shuishou.sysmgr.beans.Printer;
import com.shuishou.sysmgr.beans.UserData;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.MainFrame;

public class PrinterMgmtPanel extends JPanel implements ActionListener{
	private final Logger logger = Logger.getLogger(PrinterMgmtPanel.class.getName());
	private MainFrame mainFrame;
	private JTable tablePrinter = new JTable();
	private PrinterTableModel modelPrinter;
	private JButton btnAdd = new JButton(Messages.getString("Add"));
	private JButton btnModify = new JButton(Messages.getString("Modify"));
	private JButton btnDelete = new JButton(Messages.getString("Delete"));
	private JButton btnTestConnection = new JButton(Messages.getString("PrinterMgmtPanel.TestConnection"));
	private ArrayList<Printer> printerList;
	public PrinterMgmtPanel(MainFrame mainFrame, ArrayList<Printer> printerList){
		this.mainFrame = mainFrame;
		this.printerList = printerList;
		initUI();
	}
	
	private void initUI(){
		JLabel lbInfo = new JLabel(Messages.getString("PrinterMgmtPanel.help"));
		lbInfo.setBorder(BorderFactory.createTitledBorder("Information"));
		modelPrinter = new PrinterTableModel();
		tablePrinter.setModel(modelPrinter);
		tablePrinter.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablePrinter.getColumnModel().getColumn(0).setPreferredWidth(20);
		tablePrinter.getColumnModel().getColumn(1).setPreferredWidth(20);
		tablePrinter.getColumnModel().getColumn(2).setPreferredWidth(50);
		JScrollPane jspTable = new JScrollPane(tablePrinter, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		JPanel pContent = new JPanel(new BorderLayout());
		pContent.add(jspTable, BorderLayout.CENTER);
		pContent.add(lbInfo, BorderLayout.SOUTH);
		
		JPanel pButtons = new JPanel();
		pButtons.add(btnAdd);
		pButtons.add(btnModify);
		pButtons.add(btnDelete);
		pButtons.add(btnTestConnection);
		
		btnAdd.addActionListener(this);
		btnModify.addActionListener(this);
		btnDelete.addActionListener(this);
		btnTestConnection.addActionListener(this);
		this.setLayout(new BorderLayout());
		add(pContent, BorderLayout.CENTER);
		add(pButtons, BorderLayout.SOUTH);
	}
	
	public void refreshData(){
		printerList = mainFrame.loadPrinterList();
		tablePrinter.updateUI();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAdd){
			PrinterDialog dlg = new PrinterDialog(mainFrame, this, "Add Printer");
			dlg.setVisible(true);
		} else if (e.getSource() == btnModify){
			if (tablePrinter.getSelectedRow() < 0)
				return;
			PrinterDialog dlg = new PrinterDialog(mainFrame, this, "Modify Printer");
			dlg.setObject(modelPrinter.getObjectAt(tablePrinter.getSelectedRow()));
			dlg.setVisible(true);
		}  else if (e.getSource() == btnDelete){
			if (tablePrinter.getSelectedRow() < 0)
				return;
			if (JOptionPane.showConfirmDialog(this, 
					"Do you want to delete discount template : " + modelPrinter.getObjectAt(tablePrinter.getSelectedRow()).getName() + " ?",
					"Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){
				return;
			}
			Map<String, String> params = new HashMap<>();
			params.put("userId", MainFrame.getLoginUser().getId() + "");
			params.put("id", modelPrinter.getObjectAt(tablePrinter.getSelectedRow()).getId()+"");
			String url = "common/deleteprinter";
			String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
			if (response == null){
				logger.error("get null from server for remove printer. URL = " + url + ", param = "+ params);
				JOptionPane.showMessageDialog(this, "get null from server for remove printer. URL = " + url);
				return;
			}
			Gson gson = new Gson();
			HttpResult<String> result = gson.fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
			if (!result.success){
				logger.error("return false while remove printer. URL = " + url + ", response = "+response);
				JOptionPane.showMessageDialog(this, result.result);
				return;
			}
			refreshData();
		} else if (e.getSource() == btnTestConnection){
			String url = "common/testconnection";
			Map<String, String> params = new HashMap<>();
			params.put("id", modelPrinter.getObjectAt(tablePrinter.getSelectedRow()).getId()+"");
			String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
			if (response == null){
				logger.error("get null from server for test connection. URL = " + url + ", param = "+ params);
				JOptionPane.showMessageDialog(this, "get null from server for test connection. URL = " + url);
				return;
			}
			Gson gson = new Gson();
			HttpResult<String> result = gson.fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
			if (!result.success){
				logger.error("return false while test connection. URL = " + url + ", response = "+response);
				JOptionPane.showMessageDialog(this, result.result);
				return;
			}
		}
	}
	
	class PrinterTableModel extends AbstractTableModel{

		private String[] header = new String[]{"ID","Display Name","Printer Name", "Type"};
		
		public PrinterTableModel(){

		}
		@Override
		public int getRowCount() {
			if (printerList == null) return 0;
			return printerList.size();
		}

		@Override
		public int getColumnCount() {
			return header.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Printer printer = printerList.get(rowIndex);
			switch(columnIndex){
			case 0:
				return printer.getId();
			case 1: 
				return printer.getName();
			case 2:
				return printer.getPrinterName();
			case 3:
				if (printer.getType() == ConstantValue.PRINTER_TYPE_COUNTER)
					return "Counter";
				else if (printer.getType() == ConstantValue.PRINTER_TYPE_KITCHEN)
					return "Kitchen";
			}
			return "";
		}
		
		@Override
		public String getColumnName(int col){
			return header[col];
		}
		
		public Printer getObjectAt(int row){
			return printerList.get(row);
		}
	}

	
}
