package com.shuishou.sysmgr.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import com.shuishou.sysmgr.printertool.PrintThread;
import com.shuishou.sysmgr.ConstantValue;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.Category1;
import com.shuishou.sysmgr.beans.Category2;
import com.shuishou.sysmgr.beans.Desk;
import com.shuishou.sysmgr.beans.DiscountTemplate;
import com.shuishou.sysmgr.beans.Dish;
import com.shuishou.sysmgr.beans.Flavor;
import com.shuishou.sysmgr.beans.Material;
import com.shuishou.sysmgr.beans.MaterialCategory;
import com.shuishou.sysmgr.beans.PayWay;
import com.shuishou.sysmgr.beans.Permission;
import com.shuishou.sysmgr.beans.Printer;
import com.shuishou.sysmgr.beans.UserData;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.account.AccountMgmtPanel;
import com.shuishou.sysmgr.ui.config.ConfigsDialog;
import com.shuishou.sysmgr.ui.desk.DeskMgmtPanel;
import com.shuishou.sysmgr.ui.discounttemplate.DiscountTemplateMgmtPanel;
import com.shuishou.sysmgr.ui.flavor.FlavorMgmtPanel;
import com.shuishou.sysmgr.ui.material.MaterialMgmtPanel;
import com.shuishou.sysmgr.ui.member.MemberBalanceQueryPanel;
import com.shuishou.sysmgr.ui.member.MemberQueryPanel;
import com.shuishou.sysmgr.ui.menu.MenuMgmtPanel;
import com.shuishou.sysmgr.ui.payway.PayWayMgmtPanel;
import com.shuishou.sysmgr.ui.printer.PrinterMgmtPanel;
import com.shuishou.sysmgr.ui.query.IndentQueryPanel;
import com.shuishou.sysmgr.ui.query.LogQueryPanel;
import com.shuishou.sysmgr.ui.query.ShiftworkQueryPanel;
import com.shuishou.sysmgr.ui.statistics.MemberStatPanel;
import com.shuishou.sysmgr.ui.statistics.StatMaterialPanel;
import com.shuishou.sysmgr.ui.statistics.StatisticsPanel;

public class MainFrame extends JFrame implements ActionListener{
	public static final Logger logger = Logger.getLogger(MainFrame.class.getName());
	public static int WINDOW_WIDTH;
	public static int WINDOW_HEIGHT;
	public static int WINDOW_LOCATIONX;
	public static int WINDOW_LOCATIONY;
	public static String language;
	public static String SERVER_URL;
	public static String functionlist;
	public static String printerName;
	public static String openhour_starttime = "00:00:00";
	public static String openhour_endtime = "23:59:59";
	private static final String CARDLAYOUT_MENUMGMT= "menumgmt"; 
	private static final String CARDLAYOUT_ACCOUNTMGMT= "accountmgmt"; 
	private static final String CARDLAYOUT_DESKMGMT= "deskmgmt"; 
	private static final String CARDLAYOUT_FLAVORMGMT= "flavormgmt"; 
	private static final String CARDLAYOUT_PRINTERMGMT= "printermgmt"; 
	private static final String CARDLAYOUT_DISCOUNTTEMPLATEMGMT= "discounttemplatemgmt"; 
	private static final String CARDLAYOUT_PAYWAYMGMT= "paywaymgmt"; 
	private static final String CARDLAYOUT_LOGQUERY= "logquery"; 
	private static final String CARDLAYOUT_INDENTQUERY= "indentquery"; 
	private static final String CARDLAYOUT_SHIFTWORKQUERY= "shiftworkquery"; 
	private static final String CARDLAYOUT_STATISTICS= "statistics"; 
	private static final String CARDLAYOUT_QUERYMATERIAL = "querymaterial";
	private static final String CARDLAYOUT_MATERIAL= "material"; 
	private static final String CARDLAYOUT_MEMBERMGMT= "membermgmt"; 
	private static final String CARDLAYOUT_QUERYMEMBERRECHARGE= "querymemberrecharge";
	private static final String CARDLAYOUT_MEMBERSTAT= "memberstat";
	private JMenuItem menuitemAccount = new JMenuItem(Messages.getString("MainFrame.Menu.AccountMgr"));
	private JMenuItem menuitemConfiguration = new JMenuItem(Messages.getString("MainFrame.Menu.Config"));
	private JMenuItem menuitemDesk = new JMenuItem(Messages.getString("MainFrame.Menu.DeskMgr"));
	private JMenuItem menuitemPayway = new JMenuItem(Messages.getString("MainFrame.Menu.PayWayMgr"));
	private JMenuItem menuitemDiscount = new JMenuItem(Messages.getString("MainFrame.Menu.DiscountTempMgr"));
	private JMenuItem menuitemPrinter = new JMenuItem(Messages.getString("MainFrame.Menu.PrinterMgr"));
	private JMenuItem menuitemMenu = new JMenuItem(Messages.getString("MainFrame.Menu.MenuMgr"));
	private JMenuItem menuitemFlavor = new JMenuItem(Messages.getString("MainFrame.Menu.FlavorMgr"));
	private JMenuItem menuitemMaterial = new JMenuItem(Messages.getString("MainFrame.Menu.MaterialMgr"));
	private JMenuItem menuitemQueryLog = new JMenuItem(Messages.getString("MainFrame.Menu.QueryLog"));
	private JMenuItem menuitemQueryIndent = new JMenuItem(Messages.getString("MainFrame.Menu.QueryIndent"));
	private JMenuItem menuitemQueryShiftwork = new JMenuItem(Messages.getString("MainFrame.Menu.QueryShiftWork"));
	private JMenuItem menuitemStatistics = new JMenuItem(Messages.getString("MainFrame.Menu.QueryStatistic"));
	private JMenuItem menuitemQueryMaterial = new JMenuItem(Messages.getString("MainFrame.Menu.QueryMaterial"));
	private JMenuItem menuitemMember = new JMenuItem(Messages.getString("MainFrame.Menu.Member"));
	private JMenuItem menuitemQueryMemberBalance = new JMenuItem(Messages.getString("MainFrame.Menu.QueryMemberBalance"));
	private JMenuItem menuitemStatMember = new JMenuItem(Messages.getString("MainFrame.Menu.StatMember"));
	private JPanel pContent = new JPanel(new CardLayout());
	
	private ArrayList<Category1> listCategory1s;
	private ArrayList<MaterialCategory> listMaterialCategory;
	private ArrayList<Printer> listPrinters;
	private static UserData loginUser;
	private HashMap<String, String> configsMap;
	
	private MemberQueryPanel pMemberMgmt;
	private MemberBalanceQueryPanel pQueryMemberBalance;
	private MemberStatPanel pMemberStat;
	private MenuMgmtPanel pMenuMgmt;
	private MaterialMgmtPanel pMaterialMgmt;
	private AccountMgmtPanel pAccount;
	private DeskMgmtPanel pDesk;
	private FlavorMgmtPanel pFlavor;
	private PrinterMgmtPanel pPrinter;
	private DiscountTemplateMgmtPanel pDiscountTemplate;
	private PayWayMgmtPanel pPayWay;
	private LogQueryPanel pQueryLog;
	private IndentQueryPanel pQueryIndent;
	private ShiftworkQueryPanel pQueryShiftwork;
	private StatisticsPanel pStatistics;
	private StatMaterialPanel pStatMaterial;
	
	private MainFrame(){
		initUI();
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocation(WINDOW_LOCATIONX, WINDOW_LOCATIONY);
		setTitle(Messages.getString("MainFrame.FrameTitle")); //$NON-NLS-1$
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initData();
		//start printer thread
        new PrintThread().startThread();
	}
	
	private void initData(){
		loadListCategory1s();
		loadListMaterialCategory();
		loadListPrinters();
		loadConfigsMap();
	}
	
	private void initUI(){
		menuitemAccount.addActionListener(this);
		menuitemConfiguration.addActionListener(this);
		menuitemDesk.addActionListener(this);
		menuitemPayway.addActionListener(this);
		menuitemDiscount.addActionListener(this);
		menuitemPrinter.addActionListener(this);
		menuitemMenu.addActionListener(this);
		menuitemFlavor.addActionListener(this);
		menuitemMaterial.addActionListener(this);
		menuitemQueryLog.addActionListener(this);
		menuitemQueryIndent.addActionListener(this);
		menuitemQueryShiftwork.addActionListener(this);
		menuitemStatistics.addActionListener(this);
		menuitemMember.addActionListener(this);
		menuitemQueryMemberBalance.addActionListener(this);
		menuitemStatMember.addActionListener(this);
		menuitemQueryMaterial.addActionListener(this);
		
		JMenuBar menubar = new JMenuBar();
		JMenu menuConfig = new JMenu(Messages.getString("MainFrame.Menu.Config"));
		menuConfig.add(menuitemAccount);
		menuConfig.add(menuitemConfiguration);
		menuConfig.add(menuitemDesk);
		menuConfig.add(menuitemPayway);
		menuConfig.add(menuitemDiscount);
		menuConfig.add(menuitemPrinter);
		JMenu menuMenu = new JMenu(Messages.getString("MainFrame.Menu.MenuMgr"));
		menuMenu.add(menuitemMenu);
		menuMenu.add(menuitemFlavor);
		JMenu menuMaterial = new JMenu(Messages.getString("MainFrame.Menu.MaterialMgr"));
		menuMaterial.add(menuitemMaterial);
		JMenu menuQuery = new JMenu(Messages.getString("MainFrame.Menu.Query"));
		menuQuery.add(menuitemQueryLog);
		menuQuery.add(menuitemQueryIndent);
		menuQuery.add(menuitemQueryShiftwork);
		menuQuery.add(menuitemQueryMaterial);
		menuQuery.add(menuitemStatMember);
		menuQuery.add(menuitemStatistics);
		
		JMenu menuMember = new JMenu(Messages.getString("MainFrame.Menu.Member"));
		menuMember.add(menuitemMember);
		menuMember.add(menuitemQueryMemberBalance);
		
		
		menubar.add(menuConfig);
		menubar.add(menuMenu);
		menubar.add(menuMaterial);
		menubar.add(menuMember);
		menubar.add(menuQuery);
		
		this.setJMenuBar(menubar);
		
		menuitemAccount.setVisible(functionlist.indexOf(ConstantValue.FUNCTION_ACCOUNT) >=0);
		menuitemConfiguration.setVisible(functionlist.indexOf(ConstantValue.FUNCTION_CONFIG) >=0);
		menuitemMenu.setVisible(functionlist.indexOf(ConstantValue.FUNCTION_MENU) >=0);
		menuitemFlavor.setVisible(functionlist.indexOf(ConstantValue.FUNCTION_FLAVOR) >=0);
		menuitemDesk.setVisible(functionlist.indexOf(ConstantValue.FUNCTION_DESK) >=0);
		menuitemPayway.setVisible(functionlist.indexOf(ConstantValue.FUNCTION_PAYWAY) >=0);
		menuitemDiscount.setVisible(functionlist.indexOf(ConstantValue.FUNCTION_DISCOUNT) >=0);
		menuitemPrinter.setVisible(functionlist.indexOf(ConstantValue.FUNCTION_PRINTER) >=0);
		menuitemMaterial.setVisible(functionlist.indexOf(ConstantValue.FUNCTION_MATERIAL) >=0);
		menuMaterial.setVisible(functionlist.indexOf(ConstantValue.FUNCTION_MATERIAL) >=0);
		menuitemQueryLog.setVisible(functionlist.indexOf(ConstantValue.FUNCTION_LOGQUERY) >=0);
		menuitemQueryIndent.setVisible(functionlist.indexOf(ConstantValue.FUNCTION_ORDERQUERY) >=0);
		menuitemQueryShiftwork.setVisible(functionlist.indexOf(ConstantValue.FUNCTION_SHIFTWORK) >=0);
		menuitemQueryMaterial.setVisible(functionlist.indexOf(ConstantValue.FUNCTION_MATERIAL) >= 0);
		menuitemStatistics.setVisible(functionlist.indexOf(ConstantValue.FUNCTION_STATISTICS) >=0);
		menuitemMember.setVisible(functionlist.indexOf(ConstantValue.FUNCTION_MEMBER) >=0);
		menuitemQueryMemberBalance.setVisible(functionlist.indexOf(ConstantValue.FUNCTION_MEMBER) >= 0);
		menuitemStatMember.setVisible(functionlist.indexOf(ConstantValue.FUNCTION_MEMBER) >= 0);
		menuMember.setVisible(functionlist.indexOf(ConstantValue.FUNCTION_MEMBER) >=0);
		
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout(0, 10));
		c.add(pContent, BorderLayout.CENTER);
	}
	
	public void startLogin(String userName, String password){
		LoginDialog dlg = new LoginDialog(this);
		dlg.setValue(userName, password);
		dlg.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == menuitemAccount){
			ArrayList<UserData> userList = loadUserList();
			ArrayList<Permission> permissionList = loadPermissionList();
			if (pAccount == null){
				pAccount = new AccountMgmtPanel(this, userList, permissionList);
				pContent.add(pAccount, CARDLAYOUT_ACCOUNTMGMT);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_ACCOUNTMGMT);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_ACCOUNTMGMT);
			}
			
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + menuitemAccount.getText());
		} else if (e.getSource() == menuitemMenu){
			if (pMenuMgmt == null){
				pMenuMgmt = new MenuMgmtPanel(this, listCategory1s);
				pContent.add(pMenuMgmt, CARDLAYOUT_MENUMGMT);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_MENUMGMT);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_MENUMGMT);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + menuitemMenu.getText());
//			reloadListCategory1s();
//			
//			pMenuMgmt = new MenuMgmtPanel(this, listCategory1s);
//			pContent.removeAll();
//			pContent.add(pMenuMgmt, CARDLAYOUT_MENUMGMT);
//			pContent.updateUI();
		} else if (e.getSource() == menuitemMaterial){
			if (pMaterialMgmt == null){
				pMaterialMgmt = new MaterialMgmtPanel(this,listMaterialCategory);
				pContent.add(pMaterialMgmt, CARDLAYOUT_MATERIAL);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_MATERIAL);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_MATERIAL);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + menuitemMaterial.getText());
		} else if (e.getSource() == menuitemDesk){
			ArrayList<Desk> deskList = loadDeskList();
			if (pDesk == null){
				pDesk = new DeskMgmtPanel(this, deskList);
				pContent.add(pDesk, CARDLAYOUT_DESKMGMT);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_DESKMGMT);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_DESKMGMT);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + menuitemDesk.getText());
		} else if (e.getSource() == menuitemPayway){
			ArrayList<PayWay> paywayList = loadPayWayList();
			if (pPayWay == null){
				pPayWay = new PayWayMgmtPanel(this, paywayList);
				pContent.add(pPayWay, CARDLAYOUT_PAYWAYMGMT);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_PAYWAYMGMT);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_PAYWAYMGMT);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + menuitemPayway.getText());
		} else if (e.getSource() == menuitemDiscount){
			ArrayList<DiscountTemplate> discountTempList = loadDiscountTemplateList();
			if (pDiscountTemplate == null){
				pDiscountTemplate = new DiscountTemplateMgmtPanel(this, discountTempList);
				pContent.add(pDiscountTemplate, CARDLAYOUT_DISCOUNTTEMPLATEMGMT);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_DISCOUNTTEMPLATEMGMT);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_DISCOUNTTEMPLATEMGMT);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + menuitemDiscount.getText());
		} else if (e.getSource() == menuitemPrinter){
			ArrayList<Printer> printerList = loadPrinterList();
			if (pPrinter == null){
				pPrinter = new PrinterMgmtPanel(this, printerList);
				pContent.add(pPrinter, CARDLAYOUT_PRINTERMGMT);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_PRINTERMGMT);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_PRINTERMGMT);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + menuitemPrinter.getText());
		} else if (e.getSource() == menuitemFlavor){
			ArrayList<Flavor> flavorList = loadFlavorList();
			if (pFlavor == null){
				pFlavor = new FlavorMgmtPanel(this, flavorList);
				pContent.add(pFlavor, CARDLAYOUT_FLAVORMGMT);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_FLAVORMGMT);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_FLAVORMGMT);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + menuitemFlavor.getText());
		} else if (e.getSource() == menuitemConfiguration){
			ConfigsDialog dlg = new ConfigsDialog(this);
			dlg.setVisible(true);
		} else if (e.getSource() == menuitemQueryLog){
			if (pQueryLog == null){
				ArrayList<String> listLogType = loadLogType();
				pQueryLog = new LogQueryPanel(this, listLogType);
				pContent.add(pQueryLog, CARDLAYOUT_LOGQUERY);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_LOGQUERY);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_LOGQUERY);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + menuitemQueryLog.getText());
		} else if (e.getSource() == menuitemQueryIndent){
			if (pQueryIndent == null){
				pQueryIndent = new IndentQueryPanel(this);
				pContent.add(pQueryIndent, CARDLAYOUT_INDENTQUERY);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_INDENTQUERY);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_INDENTQUERY);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + menuitemQueryIndent.getText());
		} else if (e.getSource() == menuitemQueryShiftwork){
			if (pQueryShiftwork == null){
				pQueryShiftwork = new ShiftworkQueryPanel(this);
				pContent.add(pQueryShiftwork, CARDLAYOUT_SHIFTWORKQUERY);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_SHIFTWORKQUERY);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_SHIFTWORKQUERY);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + menuitemQueryShiftwork.getText());
		} else if (e.getSource() == menuitemStatistics){
			if (pStatistics == null){
				pStatistics = new StatisticsPanel(this);
				pContent.add(pStatistics, CARDLAYOUT_STATISTICS);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_STATISTICS);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_STATISTICS);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + menuitemStatistics.getText());
		} else if (e.getSource() == menuitemQueryMaterial){
			if (pStatMaterial == null){
				pStatMaterial = new StatMaterialPanel(this);
				pContent.add(pStatMaterial, CARDLAYOUT_QUERYMATERIAL);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_QUERYMATERIAL);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_QUERYMATERIAL);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + menuitemQueryMaterial.getText());
		} else if (e.getSource() == menuitemMember){
			if (pMemberMgmt == null){
				pMemberMgmt = new MemberQueryPanel(this);
				pContent.add(pMemberMgmt, CARDLAYOUT_MEMBERMGMT);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_MEMBERMGMT);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_MEMBERMGMT);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + menuitemMember.getText());
		} else if (e.getSource() == menuitemQueryMemberBalance){
			if (pQueryMemberBalance == null){
				pQueryMemberBalance = new MemberBalanceQueryPanel(this);
				pContent.add(pQueryMemberBalance, CARDLAYOUT_QUERYMEMBERRECHARGE);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_QUERYMEMBERRECHARGE);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_QUERYMEMBERRECHARGE);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + menuitemQueryMemberBalance.getText());
		} else if (e.getSource() == menuitemStatMember){
			if (pMemberStat == null){
				pMemberStat = new MemberStatPanel(this);
				pContent.add(pMemberStat, CARDLAYOUT_MEMBERSTAT);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_MEMBERSTAT);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_MEMBERSTAT);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + menuitemStatMember.getText());
		}
	}

	
	public HashMap<String, String> getConfigsMap() {
		return configsMap;
	}

	public void setConfigMap(HashMap<String, String> configsMap) {
		this.configsMap = configsMap;
	}

	public static UserData getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(UserData loginUser) {
		this.loginUser = loginUser;
	}

	public ArrayList<UserData> loadUserList(){
		ArrayList<UserData> userList = HttpUtil.loadUser(this, SERVER_URL + "account/accounts?userId="+loginUser.getId());
		return userList;
	}
	
	public ArrayList<Desk> loadDeskList(){
		ArrayList<Desk> deskList = HttpUtil.loadDesk(this, SERVER_URL + "common/getdesks?userId="+loginUser.getId());
		return deskList;
	}
	
	public ArrayList<Flavor> loadFlavorList(){
		ArrayList<Flavor> deskList = HttpUtil.loadFlavor(this, SERVER_URL + "menu/queryflavor?userId="+loginUser.getId());
		return deskList;
	}
	
	public ArrayList<PayWay> loadPayWayList(){
		ArrayList<PayWay> payWayList = HttpUtil.loadPayWay(this, SERVER_URL + "common/getpayways");
		return payWayList;
	}
	
	public ArrayList<DiscountTemplate> loadDiscountTemplateList(){
		ArrayList<DiscountTemplate> discountTemplateList = HttpUtil.loadDiscountTemplate(this, SERVER_URL + "common/getdiscounttemplates");
		return discountTemplateList;
	}
	
	public ArrayList<Printer> loadPrinterList(){
		ArrayList<Printer> printerList = HttpUtil.loadPrinter(this, SERVER_URL + "common/getprinters");
		return printerList;
	}
	public ArrayList<String> loadLogType(){
		ArrayList<String> listLogType = HttpUtil.loadLogType(this, SERVER_URL + "log/log_types?userId="+loginUser.getId());
		return listLogType;
	}
	private ArrayList<Permission> loadPermissionList(){
		ArrayList<Permission> permissionList = HttpUtil.loadPermission(this, SERVER_URL + "account/querypermission");
		Collections.sort(permissionList, new Comparator<Permission>(){

			@Override
			public int compare(Permission o1, Permission o2) {
				return o1.getSequence() - o2.getSequence();
			}});
		return permissionList;
	}
	
	public ArrayList<Category1> getListCategory1s() {
		return listCategory1s;
	}

	
	public ArrayList<MaterialCategory> getListMaterialCategory() {
		return listMaterialCategory;
	}

	public void loadListCategory1s() {
		listCategory1s = HttpUtil.loadMenu(this, SERVER_URL + "menu/querymenu");
		@SuppressWarnings("rawtypes")
		Comparator comp = new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				if (o1 instanceof Category1 && o2 instanceof Category1)
					return ((Category1) o1).getSequence() - ((Category1) o2).getSequence();
				else if (o1 instanceof Category2 && o2 instanceof Category2)
					return ((Category2) o1).getSequence() - ((Category2) o2).getSequence();
				else if (o1 instanceof Dish && o2 instanceof Dish)
					return ((Dish) o1).getSequence() - ((Dish) o2).getSequence();
				return 0;
			}
		};
		if (listCategory1s != null && !listCategory1s.isEmpty()){
			Collections.sort(listCategory1s, comp);
			for (Category1 c1 : listCategory1s) {
				Collections.sort(c1.getCategory2s(), comp);
				for (Category2 c2 : c1.getCategory2s()) {
					Collections.sort(c2.getDishes(), comp);
				}
			}
		}
		
	}
	
	public void loadListMaterialCategory(){
		this.listMaterialCategory = HttpUtil.loadMaterialCategory(this, SERVER_URL + "material/querymaterialcategory");
		@SuppressWarnings("rawtypes")
		Comparator comp = new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				if (o1 instanceof MaterialCategory && o2 instanceof MaterialCategory)
					return ((MaterialCategory) o1).getSequence() - ((MaterialCategory) o2).getSequence();
				else if (o1 instanceof Material && o2 instanceof Material)
					return ((Material) o1).getSequence() - ((Material) o2).getSequence();
				return 0;
			}
		};
		if (listMaterialCategory != null && !listMaterialCategory.isEmpty()){
			Collections.sort(listMaterialCategory, comp);
			for (MaterialCategory mc : listMaterialCategory) {
				Collections.sort(mc.getMaterials(), comp);
			}
		}
		
	}
	
	public ArrayList<Printer> getListPrinters() {
		return listPrinters;
	}
	
	public void loadListPrinters() {
		listPrinters = HttpUtil.loadPrinter(this, SERVER_URL + "common/getprinters");
	}
	
	public void loadConfigsMap(){
		this.configsMap = HttpUtil.loadConfigMap(this, SERVER_URL + "common/queryconfigmap");
	}

	public static void main(String[] args){
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				MainFrame.logger.error(ConstantValue.DFYMDHMS.format(new Date()));
				MainFrame.logger.error("", e);
				e.printStackTrace();
			}
		});
		StartingWaitDialog waitDlg = new StartingWaitDialog();
		waitDlg.setVisible(true);
		
		//load properties
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = MainFrame.class.getClassLoader().getResourceAsStream("config.properties");
			// load a properties file
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Messages.initResourceBundle(prop.getProperty("language"));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//windows 格式
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		MainFrame.SERVER_URL = prop.getProperty("SERVER_URL");
		MainFrame.WINDOW_WIDTH = Integer.parseInt(prop.getProperty("mainframe.width"));
		MainFrame.WINDOW_HEIGHT = Integer.parseInt(prop.getProperty("mainframe.height"));
		MainFrame.WINDOW_LOCATIONX = Integer.parseInt(prop.getProperty("mainframe.locationx"));
		MainFrame.WINDOW_LOCATIONY = Integer.parseInt(prop.getProperty("mainframe.locationy"));
		MainFrame.language = prop.getProperty("language");
		MainFrame.functionlist = prop.getProperty("mainframe.functionlist");
		MainFrame.printerName = prop.getProperty("printerName");
		if (prop.getProperty("openhour.starttime") != null)
			MainFrame.openhour_starttime = prop.getProperty("openhour.starttime");
		if (prop.getProperty("openhour.endtime") != null)
			MainFrame.openhour_endtime = prop.getProperty("openhour.endtime");
		MainFrame f = new MainFrame();
		waitDlg.setVisible(false);
		f.setVisible(true);
		f.startLogin(prop.getProperty("defaultuser.name"), prop.getProperty("defaultuser.password"));
	}
}
