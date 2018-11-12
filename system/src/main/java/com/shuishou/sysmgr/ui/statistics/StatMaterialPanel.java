package com.shuishou.sysmgr.ui.statistics;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.ConstantValue;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.IndentDetail;
import com.shuishou.sysmgr.beans.StatItem;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.JDatePicker;
import com.shuishou.sysmgr.ui.statistics.bean.MaterialRecordInfo;

public class StatMaterialPanel extends JPanel implements ActionListener{
	private final Logger logger = Logger.getLogger(StatMaterialPanel.class.getName());
	private Gson gson = new Gson();

	private MainFrame mainFrame;
	
	private JDatePicker dpStartDate = new JDatePicker();
	private JDatePicker dpEndDate = new JDatePicker();
	private JButton btnToday = new JButton(Messages.getString("StatisticsPanel.Today"));
	private JButton btnYesterday = new JButton(Messages.getString("StatisticsPanel.Yesterday"));
	private JButton btnThisWeek = new JButton(Messages.getString("StatisticsPanel.Thisweek"));
	private JButton btnLastWeek = new JButton(Messages.getString("StatisticsPanel.Lastweek"));
	private JButton btnThisMonth = new JButton(Messages.getString("StatisticsPanel.Thismonth"));
	private JButton btnLastMonth = new JButton(Messages.getString("StatisticsPanel.Lastmonth"));
	private JButton btnQuery = new JButton("Query");
	private JButton btnExportExcel = new JButton("Export");
	private JTable tabReport = new JTable();
	private JPanel pChart = new JPanel(new GridLayout(0, 1));
	private JLabel lbTotalInfo = new JLabel();
	private IntComparator intComp = new IntComparator();
	private DoubleComparator doubleComp = new DoubleComparator();
	private StringComparator stringComp = new StringComparator();
	
	public StatMaterialPanel(MainFrame mainFrame){
		this.mainFrame = mainFrame;
		initUI();
	}
	
	private void initUI(){
		tabReport.setAutoCreateRowSorter(false);
		JPanel pReport = new JPanel(new GridLayout());
		JScrollPane jspTable = new JScrollPane(tabReport, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel pData = new JPanel(new BorderLayout());
		pData.add(jspTable, BorderLayout.CENTER);
		pData.add(lbTotalInfo, BorderLayout.SOUTH);
		JScrollPane jspChart = new JScrollPane(pChart, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pReport.add(pData);
		pReport.add(jspChart);
		
		JPanel pQueryTimeButton = new JPanel();
		pQueryTimeButton.add(btnToday,		new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pQueryTimeButton.add(btnYesterday,	new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pQueryTimeButton.add(btnThisWeek,	new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pQueryTimeButton.add(btnLastWeek,	new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pQueryTimeButton.add(btnThisMonth,	new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pQueryTimeButton.add(btnLastMonth,	new GridBagConstraints(2, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		
		JLabel lbStartDate = new JLabel(Messages.getString("StatisticsPanel.StartDate"));
		JLabel lbEndDate = new JLabel(Messages.getString("StatisticsPanel.EndDate"));
		
		JPanel pQueryTime = new JPanel(new GridBagLayout());
		pQueryTime.add(lbStartDate,	new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pQueryTime.add(dpStartDate,	new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pQueryTime.add(lbEndDate,	new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pQueryTime.add(dpEndDate,	new GridBagConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pQueryTime.add(pQueryTimeButton,	new GridBagConstraints(0, 1, 4, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		
		JPanel pQuery = new JPanel(new GridLayout(0, 1, 0, 20));
		pQuery.add(btnQuery);
		pQuery.add(btnExportExcel);
		JPanel pCondition = new JPanel(new GridBagLayout());
		pCondition.add(pQueryTime,	new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(pQuery,		new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(new JLabel(),new GridBagConstraints(3, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		setLayout(new BorderLayout());
		add(pReport, BorderLayout.CENTER);
		add(pCondition, BorderLayout.NORTH);
		
		btnQuery.addActionListener(this);
		btnToday.addActionListener(this);
		btnYesterday.addActionListener(this);
		btnThisWeek.addActionListener(this);
		btnLastWeek.addActionListener(this);
		btnThisMonth.addActionListener(this);
		btnLastMonth.addActionListener(this);
		btnExportExcel.addActionListener(this);
	}

	private void showReport(ArrayList<MaterialRecordInfo> data){
		AbstractTableModel model = new StatModel(data);
		tabReport.setModel(model);
		TableRowSorter trs = new TableRowSorter(model);
		trs.setComparator(0, stringComp);
		trs.setComparator(1, stringComp);
		trs.setComparator(2, doubleComp);
		trs.setComparator(3, doubleComp);
		trs.setComparator(4, stringComp);
		trs.setComparator(5, doubleComp);
		trs.setComparator(6, doubleComp);
		tabReport.setRowSorter(trs);
		
		tabReport.setAutoCreateRowSorter(false);
		double totalConsume = 0;
		for (int i = 0; i < data.size(); i++) {
			totalConsume += data.get(i).totalPrice;
		}
		lbTotalInfo.setText("record : " + tabReport.getRowCount()
				+ ", total price : $" + String.format("%.2f", totalConsume));
	}
	
	private void doQuery(){
		if (!dpStartDate.getModel().isSelected() || !dpEndDate.getModel().isSelected()){
			JOptionPane.showMessageDialog(mainFrame, Messages.getString("StatisticsPanel.MustChooseDate"));
			return;
		}
		String url = "material/statisticsconsume";
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId() + "");
		params.put("usePreDay", String.valueOf(false));//TODO:暂时不用这个值
		if (dpStartDate.getModel() != null && dpStartDate.getModel().getValue() != null){
			Calendar c = (Calendar)dpStartDate.getModel().getValue();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			params.put("startTime", ConstantValue.DFYMDHMS.format(c.getTime()));
		}
		if (dpEndDate.getModel() != null && dpEndDate.getModel().getValue() != null){
			Calendar c = (Calendar)dpEndDate.getModel().getValue();
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			params.put("endTime", ConstantValue.DFYMDHMS.format(c.getTime()));
		}
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for material consume statistics. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for material consume statistics. URL = " + url);
			return;
		}
		HttpResult<ArrayList<MaterialRecordInfo>> result = gson.fromJson(response, new TypeToken<HttpResult<ArrayList<MaterialRecordInfo>>>(){}.getType());
		if (!result.success){
			logger.error("return false while material consume statistics. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		Collections.sort(result.data, new Comparator<MaterialRecordInfo>(){

			@Override
			public int compare(MaterialRecordInfo o1, MaterialRecordInfo o2) {
				if (o1.categoryName.equals(o2.categoryName))
					return o1.materialName.compareTo(o2.materialName);
				else 
					return o1.categoryName.compareTo(o2.categoryName);
			}});
		showReport(result.data);
		showChart(result.data);
	}
	
	private void showChart(ArrayList<MaterialRecordInfo> items){
		//创建主题样式  
        StandardChartTheme mChartTheme = new StandardChartTheme("CN");  
        //设置标题字体  
        mChartTheme.setExtraLargeFont(new Font("黑体", Font.BOLD, 20));  
        //设置轴向字体  
        mChartTheme.setLargeFont(new Font("宋体", Font.CENTER_BASELINE, 15));  
        //设置图例字体  
        mChartTheme.setRegularFont(new Font("宋体", Font.CENTER_BASELINE, 15));  
        //应用主题样式  
        ChartFactory.setChartTheme(mChartTheme);  
		pChart.removeAll();
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
		for (int i = 0; i < items.size(); i++) {
			pieDataset.setValue(items.get(i).materialName, items.get(i).totalPrice);
			barDataset.setValue(items.get(i).totalPrice, items.get(i).materialName, "");
		}
		JFreeChart pieChart = ChartFactory.createPieChart(Messages.getString("StatisticsPanel.Sell"),pieDataset,true, true, false);
		pieChart.removeLegend();
		ChartPanel cpPie = new ChartPanel(pieChart);
		pChart.add(cpPie);
		
		
		JFreeChart barChart = ChartFactory.createBarChart(Messages.getString("StatisticsPanel.Sell"), "dish", "sold", barDataset);
		barChart.removeLegend();
		ChartPanel cpBar = new ChartPanel(barChart);
		pChart.add(cpBar);
		pChart.updateUI();
	}
	
	private void doExport(){
		if (tabReport.getRowCount() == 0)
			return;
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showSaveDialog(mainFrame);
		if (returnVal != JFileChooser.APPROVE_OPTION){
			return;
		}
		HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("stat");
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i< tabReport.getColumnCount(); i++){
        	HSSFCell cell = row.createCell(i);
        	cell.setCellValue(tabReport.getColumnName(i));
        }
        for (int i = 0; i < tabReport.getRowCount(); i++) {
			HSSFRow rowi = sheet.createRow(i+1);
			for (int j = 0; j < tabReport.getColumnCount(); j++) {
				HSSFCell cell = rowi.createCell(j);
				cell.setCellValue(String.valueOf(tabReport.getValueAt(i, j)));
			}
		}
        FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(fc.getSelectedFile());
			workbook.write(outputStream);
	        workbook.close();
		} catch (IOException e) {
			logger.error("", e);
			JOptionPane.showMessageDialog(mainFrame, e.getMessage());
		} finally{
			if (outputStream != null)
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.error("", e);
					JOptionPane.showMessageDialog(mainFrame, e.getMessage());
				}
		}
        
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnQuery){
			doQuery();
		} else if (e.getSource() == btnExportExcel){
			doExport();
		} else if (e.getSource() == btnToday){
			Calendar c = Calendar.getInstance();
			dpStartDate.getModel().setYear(c.get(Calendar.YEAR));
			dpStartDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpStartDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpStartDate.getModel().setSelected(true);
			dpEndDate.getModel().setYear(c.get(Calendar.YEAR));
			dpEndDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpEndDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpEndDate.getModel().setSelected(true);
		} else if (e.getSource() == btnYesterday){
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 1);
			dpStartDate.getModel().setYear(c.get(Calendar.YEAR));
			dpStartDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpStartDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpStartDate.getModel().setSelected(true);
			dpEndDate.getModel().setYear(c.get(Calendar.YEAR));
			dpEndDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpEndDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpEndDate.getModel().setSelected(true);
		} else if (e.getSource() == btnThisWeek){
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 7);
			dpStartDate.getModel().setYear(c.get(Calendar.YEAR));
			dpStartDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpStartDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpStartDate.getModel().setSelected(true);
			c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 7);
			dpEndDate.getModel().setYear(c.get(Calendar.YEAR));
			dpEndDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpEndDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpEndDate.getModel().setSelected(true);
		} else if (e.getSource() == btnLastWeek){
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 14);
			dpStartDate.getModel().setYear(c.get(Calendar.YEAR));
			dpStartDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpStartDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpStartDate.getModel().setSelected(true);
			c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 7);
			dpEndDate.getModel().setYear(c.get(Calendar.YEAR));
			dpEndDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpEndDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpEndDate.getModel().setSelected(true);
		} else if (e.getSource() == btnThisMonth){
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_MONTH, 1);
			dpStartDate.getModel().setYear(c.get(Calendar.YEAR));
			dpStartDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpStartDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpStartDate.getModel().setSelected(true);
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
			dpEndDate.getModel().setYear(c.get(Calendar.YEAR));
			dpEndDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpEndDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpEndDate.getModel().setSelected(true);
		} else if (e.getSource() == btnLastMonth){
			Calendar c = Calendar.getInstance();
			c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
			c.set(Calendar.DAY_OF_MONTH, 1);
//			dpStartDate.setDateTime(c);
			dpStartDate.getModel().setYear(c.get(Calendar.YEAR));
			dpStartDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpStartDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpStartDate.getModel().setSelected(true);
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
			dpEndDate.getModel().setYear(c.get(Calendar.YEAR));
			dpEndDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpEndDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpEndDate.getModel().setSelected(true);
		} 
	}
	
	class IntComparator implements Comparator{

		@Override
		public int compare(Object o1, Object o2) {
			if (o1 instanceof Integer && o2 instanceof Integer){
				return ((Integer)o1).compareTo((Integer)o2);
			}
			return 0;
		}
		
	}
	
	class DoubleComparator implements Comparator{

		@Override
		public int compare(Object o1, Object o2) {
			if (o1 instanceof Double && o2 instanceof Double){
				return ((Double)o1).compareTo((Double)o2);
			}
			return 0;
		}
		
	}
	
	class StringComparator implements Comparator{

		@Override
		public int compare(Object o1, Object o2) {
			if (o1 instanceof String && o2 instanceof String){
				return ((String)o1).compareTo((String)o2);
			}
			return 0;
		}
		
	}
	
	class StatModel extends AbstractTableModel{
		private String[] header = new String[]{"Category", "Material", "Purchase", "Consume", "Unit", "Price", "Total Price"};
		
		private ArrayList<MaterialRecordInfo> statItems;
		public StatModel(ArrayList<MaterialRecordInfo> statItems){
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
			MaterialRecordInfo item = statItems.get(rowIndex);
			switch(columnIndex){
			case 0:
				return item.categoryName;
			case 1: 
				return item.materialName;
			case 2:
				return item.purchaseAmount;
			case 3:
				return item.consumeAmount;
			case 4:
				return item.unit;
			case 5:
				return item.price;
			case 6:
				return String.format(ConstantValue.FORMAT_DOUBLE, item.totalPrice);
			}
			return "";
		}
		
		@Override
		public String getColumnName(int col){
			return header[col];
		}
		
		public void setData(ArrayList<MaterialRecordInfo> statItems){
			this.statItems = statItems;
		}
		
		public MaterialRecordInfo getObjectAt(int row){
			return statItems.get(row);
		}
		
		
	}
}
