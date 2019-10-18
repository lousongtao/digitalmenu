package com.shuishou.sysmgr.ui.config;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.ConstantValue;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.Dish;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.NumberTextField;
import com.shuishou.sysmgr.ui.menu.DishPanel;

public class ConfigsDialog extends JDialog implements ActionListener{
	private final Logger logger = Logger.getLogger(ConfigsDialog.class.getName());
	private JButton btnSaveConfirmCode = new JButton("Save");
	private JButton btnSaveOpenCashdrawerCode = new JButton("Save");
	private JButton btnSaveCancelOrderCode = new JButton("Save");
	private JButton btnSaveClearTableCode = new JButton("Save");
	private JButton btnSaveRefundCode = new JButton("Save");
	private JTextField tfOldConfirmCode;
	private NumberTextField tfNewConfirmCode;
	private JTextField tfOldOpenCashdrawerCode;
	private NumberTextField tfNewOpenCashdrawerCode;
	private JTextField tfOldCancelOrderCode;
	private NumberTextField tfNewCancelOrderCode;
	private JTextField tfOldClearTableCode;
	private NumberTextField tfNewClearTableCode;
	private JTextField tfOldRefundCode;
	private NumberTextField tfNewRefundCode;

	private JRadioButton rbLanguageAmount1 = new JRadioButton("1");
	private JRadioButton rbLanguageAmount2 = new JRadioButton("2");
	private JTextField tfFirstLanguageName = new JTextField();
	private JTextField tfSecondLanguageName = new JTextField();
	private JCheckBox cbPrint2ndLanguage = new JCheckBox("Print second language name on ticket");
	
	private JButton btnSaveLanguageSet = new JButton("Save");
	private JCheckBox cbMemberScore = new JCheckBox("Score");
	private JCheckBox cbMemberDeposit = new JCheckBox("Deposit");
	private JCheckBox cbNeedPassword = new JCheckBox("Need Password while Consuming");
	private JButton btnSaveMember = new JButton("Save");
	private NumberTextField tfScorePerDollar = new NumberTextField(true);
	private JTextField tfOldMemberBalanceCode;
	private NumberTextField tfNewMemberBalanceCode;
	private JButton btnSaveMemberBalanceCode = new JButton("Save");
	
	private JTextField tfBranchName = new JTextField();
	private JButton btnSaveBranchName = new JButton("Save");
	
	private JRadioButton rbPrintTicketAfterOrder = new JRadioButton("After order created", true);
	private JRadioButton rbPrintTicketAfterPaid = new JRadioButton("After order paid");
	private JButton btnSavePrintTicket = new JButton("Save");
	
	private MainFrame mainFrame;
	public ConfigsDialog(MainFrame mainFrame){
		super(mainFrame, Messages.getString("ConfigsDialog.Config"), true);
		this.mainFrame = mainFrame;
		initUI();
		initData();
	}
	
	private void initUI(){
		
		JLabel lbOldConfirmCode = new JLabel("old code");
		JLabel lbNewConfirmCode = new JLabel("new code");
		tfOldConfirmCode = new JTextField();
		tfNewConfirmCode = new NumberTextField(false);
		JPanel pConfirmCode = new JPanel(new GridBagLayout());
		pConfirmCode.add(lbOldConfirmCode, new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pConfirmCode.add(tfOldConfirmCode, new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pConfirmCode.add(lbNewConfirmCode, new GridBagConstraints(2, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pConfirmCode.add(tfNewConfirmCode, new GridBagConstraints(3, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pConfirmCode.add(btnSaveConfirmCode, new GridBagConstraints(4, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pConfirmCode.setBorder(BorderFactory.createTitledBorder("Confirm Code"));
		
		JLabel lbOldOpenCashdrawerCode = new JLabel("old code");
		JLabel lbNewOpenCashdrawerCode = new JLabel("new code");
		tfOldOpenCashdrawerCode = new JTextField();
		tfNewOpenCashdrawerCode = new NumberTextField(false);
		JPanel pOpenCashdrawerCode = new JPanel(new GridBagLayout());
		pOpenCashdrawerCode.add(lbOldOpenCashdrawerCode, new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.add(tfOldOpenCashdrawerCode, new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.add(lbNewOpenCashdrawerCode, new GridBagConstraints(2, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.add(tfNewOpenCashdrawerCode, new GridBagConstraints(3, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.add(btnSaveOpenCashdrawerCode, new GridBagConstraints(4, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.setBorder(BorderFactory.createTitledBorder("Open Cashdrawer Code"));
		
		JLabel lbOldCancelOrderCode = new JLabel("old code");
		JLabel lbNewCancelOrderCode = new JLabel("new code");
		tfOldCancelOrderCode = new JTextField();
		tfNewCancelOrderCode = new NumberTextField(false);
		JPanel pCancelOrderCode = new JPanel(new GridBagLayout());
		pCancelOrderCode.add(lbOldCancelOrderCode, new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pCancelOrderCode.add(tfOldCancelOrderCode, new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pCancelOrderCode.add(lbNewCancelOrderCode, new GridBagConstraints(2, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pCancelOrderCode.add(tfNewCancelOrderCode, new GridBagConstraints(3, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pCancelOrderCode.add(btnSaveCancelOrderCode, new GridBagConstraints(4, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pCancelOrderCode.setBorder(BorderFactory.createTitledBorder("Cancel Order Code"));
		
		JLabel lbOldClearTableCode = new JLabel("old code");
		JLabel lbNewClearTableCode = new JLabel("new code");
		tfOldClearTableCode = new JTextField();
		tfNewClearTableCode = new NumberTextField(false);
		JPanel pClearTableCode = new JPanel(new GridBagLayout());
		pClearTableCode.add(lbOldClearTableCode, new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pClearTableCode.add(tfOldClearTableCode, new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pClearTableCode.add(lbNewClearTableCode, new GridBagConstraints(2, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pClearTableCode.add(tfNewClearTableCode, new GridBagConstraints(3, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pClearTableCode.add(btnSaveClearTableCode, new GridBagConstraints(4, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pClearTableCode.setBorder(BorderFactory.createTitledBorder("Clear Table Code"));

		JLabel lbOldRefundCode = new JLabel("old code");
		JLabel lbNewRefundCode = new JLabel("new code");
		tfOldRefundCode = new JTextField();
		tfNewRefundCode = new NumberTextField(false);
		JPanel pRefundCode = new JPanel(new GridBagLayout());
		pRefundCode.add(lbOldRefundCode, new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pRefundCode.add(tfOldRefundCode, new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pRefundCode.add(lbNewRefundCode, new GridBagConstraints(2, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pRefundCode.add(tfNewRefundCode, new GridBagConstraints(3, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pRefundCode.add(btnSaveRefundCode, new GridBagConstraints(4, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pRefundCode.setBorder(BorderFactory.createTitledBorder("Refund Code"));

		ButtonGroup bgLanguageAmount = new ButtonGroup();
		bgLanguageAmount.add(rbLanguageAmount1);
		bgLanguageAmount.add(rbLanguageAmount2);
		rbLanguageAmount2.setSelected(true);
		JPanel pLanguageSet = new JPanel(new GridBagLayout());
		pLanguageSet.setBorder(BorderFactory.createTitledBorder("Language Setting"));
		pLanguageSet.add(new JLabel("Language Amount"), new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pLanguageSet.add(rbLanguageAmount1, 			new GridBagConstraints(1, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pLanguageSet.add(rbLanguageAmount2, 			new GridBagConstraints(2, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pLanguageSet.add(new JLabel("First Language"), 	new GridBagConstraints(0, 1, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pLanguageSet.add(tfFirstLanguageName, 			new GridBagConstraints(1, 1, 2, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pLanguageSet.add(new JLabel("Second Language"), new GridBagConstraints(0, 2, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pLanguageSet.add(tfSecondLanguageName, 			new GridBagConstraints(1, 2, 2, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,10,0,0), 0, 0));;
		pLanguageSet.add(btnSaveLanguageSet, 			new GridBagConstraints(3, 0, 2, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,10,0,0), 0, 0));;
		pLanguageSet.add(cbPrint2ndLanguage, 			new GridBagConstraints(1, 3, 2, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,10,0,0), 0, 0));;
		
		JLabel lbScoreInfo = new JLabel("How many score by consuming 1$");
		tfScorePerDollar.setPreferredSize(new Dimension(60, 25));
		tfScorePerDollar.setText("1");
		JPanel pMember = new JPanel(new GridBagLayout());
		pMember.setBorder(BorderFactory.createTitledBorder("Member Management"));
		pMember.add(cbMemberScore, 		new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		pMember.add(cbMemberDeposit,	new GridBagConstraints(0, 1, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		pMember.add(lbScoreInfo,		new GridBagConstraints(1, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		pMember.add(tfScorePerDollar, 	new GridBagConstraints(2, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		pMember.add(btnSaveMember, 		new GridBagConstraints(2, 2, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		pMember.add(cbNeedPassword,		new GridBagConstraints(0, 2, 2, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		
		JPanel pBranchName = new JPanel();
		tfBranchName.setPreferredSize(new Dimension(180, 25));
		JLabel lbBranchName = new JLabel("Branch Name");
		pBranchName.setBorder(BorderFactory.createTitledBorder("Branch Name"));
		pBranchName.add(lbBranchName);
		pBranchName.add(tfBranchName);
		pBranchName.add(btnSaveBranchName);
		
		JLabel lbOldMemberBalanceCode = new JLabel("old code");
		JLabel lbNewMemberBalanceCode = new JLabel("new code");
		tfOldMemberBalanceCode = new JTextField();
		tfNewMemberBalanceCode = new NumberTextField(false);
		JPanel pMemberBalanceCode = new JPanel(new GridBagLayout());
		pMemberBalanceCode.add(lbOldMemberBalanceCode, new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pMemberBalanceCode.add(tfOldMemberBalanceCode, new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pMemberBalanceCode.add(lbNewMemberBalanceCode, new GridBagConstraints(2, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pMemberBalanceCode.add(tfNewMemberBalanceCode, new GridBagConstraints(3, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pMemberBalanceCode.add(btnSaveMemberBalanceCode, new GridBagConstraints(4, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pMemberBalanceCode.setBorder(BorderFactory.createTitledBorder("Member Balance Code"));
		
		JPanel pPrintTicket = new JPanel(new GridBagLayout());
		pPrintTicket.setBorder(BorderFactory.createTitledBorder("Print Ticket Time"));
		ButtonGroup bgPrintTicket = new ButtonGroup();
		bgPrintTicket.add(rbPrintTicketAfterOrder);
		bgPrintTicket.add(rbPrintTicketAfterPaid);
		pPrintTicket.add(rbPrintTicketAfterOrder);
		pPrintTicket.add(rbPrintTicketAfterPaid);
		pPrintTicket.add(btnSavePrintTicket);
		
		
		JPanel tabPassword = new JPanel(new GridBagLayout());
		JPanel tabLanguage = new JPanel(new GridBagLayout());
		JPanel tabMember = new JPanel(new GridBagLayout());
		JPanel tabOther = new JPanel(new GridBagLayout());
		tabPassword.add(pConfirmCode, 		new GridBagConstraints(0, 0, 1, 1,1,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));;
		tabPassword.add(pOpenCashdrawerCode, new GridBagConstraints(0, 1, 1, 1,1,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));;
		tabPassword.add(pClearTableCode, 	new GridBagConstraints(0, 2, 1, 1,1,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));;
		tabPassword.add(pCancelOrderCode, 	new GridBagConstraints(0, 3, 1, 1,1,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));;
		tabPassword.add(pRefundCode, 		new GridBagConstraints(0, 4, 1, 1,1,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));;
		tabLanguage.add(pLanguageSet, 		new GridBagConstraints(0, 1, 1, 1,1,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));;
		tabMember.add(pMember, 				new GridBagConstraints(0, 1, 1, 1,1,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));;
		tabMember.add(pBranchName, 			new GridBagConstraints(0, 2, 1, 1,1,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));;
		tabMember.add(pMemberBalanceCode, 	new GridBagConstraints(0, 3, 1, 1,1,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));;
		tabOther.add(pPrintTicket, 			new GridBagConstraints(0, 1, 1, 1,1,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));;
		
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.add("Passwords", tabPassword);
		tabPane.add("Language", tabLanguage);
		tabPane.add("Member", tabMember);
		tabPane.add("Other", tabOther);
		
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(tabPane,  BorderLayout.CENTER);
		
		btnSaveConfirmCode.addActionListener(this);
		btnSaveOpenCashdrawerCode.addActionListener(this);
		btnSaveLanguageSet.addActionListener(this);
		btnSaveClearTableCode.addActionListener(this);
		btnSaveCancelOrderCode.addActionListener(this);
		btnSaveMember.addActionListener(this);
		btnSaveBranchName.addActionListener(this);
		btnSavePrintTicket.addActionListener(this);
		btnSaveMemberBalanceCode.addActionListener(this);
		btnSaveRefundCode.addActionListener(this);

		setSize(500, 500);
		this.setLocation((int)(mainFrame.getWidth() / 2 - this.getWidth() /2 + mainFrame.getLocation().getX()), 
				(int)(mainFrame.getHeight() / 2 - this.getHeight() / 2 + mainFrame.getLocation().getY()));
	}

	private void initData(){
//		tfOldConfirmCode.setText(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_CONFIRMCODE));
//		tfOldOpenCashdrawerCode.setText(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_OPENCASHDRAWERCODE));
		if ("1".equals(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_LANGUAGEAMOUNT))){
			rbLanguageAmount1.setSelected(true);
		} else if ("2".equals(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_LANGUAGEAMOUNT))){
			rbLanguageAmount2.setSelected(true);
		}
		tfFirstLanguageName.setText(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_FIRSTLANGUAGENAME));
		tfSecondLanguageName.setText(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_SECONDLANGUAGENAME));
		cbPrint2ndLanguage.setSelected(Boolean.parseBoolean(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_PRINT2NDLANGUAGENAME)));
		if (mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_MEMBERMGR_BYSCORE) != null)
			cbMemberScore.setSelected(Boolean.valueOf(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_MEMBERMGR_BYSCORE)));
		if (mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_MEMBERMGR_BYDEPOSIT) != null)
			cbMemberDeposit.setSelected(Boolean.valueOf(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_MEMBERMGR_BYDEPOSIT)));
		if (mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_MEMBERMGR_SCOREPERDOLLAR) != null)
			tfScorePerDollar.setText(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_MEMBERMGR_SCOREPERDOLLAR));
		if (mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_BRANCHNAME) !=null)
			tfBranchName.setText(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_BRANCHNAME));
		if (mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_MEMBERMGR_NEEDPASSWORD) != null)
			cbNeedPassword.setSelected(Boolean.valueOf(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_MEMBERMGR_NEEDPASSWORD)));
		if (ConstantValue.CONFIGS_PRINTTICKET_AFTERPAY.equals(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_PRINTTICKET))){
			rbPrintTicketAfterPaid.setSelected(true);
		} else {
			rbPrintTicketAfterOrder.setSelected(true);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSaveConfirmCode){
			if (tfNewConfirmCode.getText() == null || tfNewConfirmCode.getText().length() == 0){
				JOptionPane.showMessageDialog(this, "No input new code");
				return;
			}
			doSaveCode(tfOldConfirmCode.getText(), tfNewConfirmCode.getText(), ConstantValue.CONFIGS_CONFIRMCODE);
		} else if (e.getSource() == btnSaveOpenCashdrawerCode){
			if (tfNewOpenCashdrawerCode.getText() == null || tfNewOpenCashdrawerCode.getText().length() == 0){
				JOptionPane.showMessageDialog(this, "No input new code");
				return;
			}
			doSaveCode(tfOldOpenCashdrawerCode.getText(), tfNewOpenCashdrawerCode.getText(), ConstantValue.CONFIGS_OPENCASHDRAWERCODE);
		} else if (e.getSource() == btnSaveRefundCode){
			if (tfNewRefundCode.getText() == null || tfNewRefundCode.getText().length() == 0){
				JOptionPane.showMessageDialog(this, "No input new code");
				return;
			}
			doSaveCode(tfOldRefundCode.getText(), tfNewRefundCode.getText(), ConstantValue.CONFIGS_REFUNDCODE);
		} else if (e.getSource() == btnSaveLanguageSet){
			doSaveLanguageSet();
		} else if (e.getSource() == btnSaveCancelOrderCode){
			if (tfNewCancelOrderCode.getText() == null || tfNewCancelOrderCode.getText().length() == 0){
				JOptionPane.showMessageDialog(this, "No input new code");
				return;
			}
			doSaveCode(tfOldCancelOrderCode.getText(), tfNewCancelOrderCode.getText(), ConstantValue.CONFIGS_CANCELORDERCODE);
		} else if (e.getSource() == btnSaveClearTableCode){
			if (tfNewClearTableCode.getText() == null || tfNewClearTableCode.getText().length() == 0){
				JOptionPane.showMessageDialog(this, "No input new code");
				return;
			}
			doSaveCode(tfOldClearTableCode.getText(), tfNewClearTableCode.getText(), ConstantValue.CONFIGS_CLEARTABLECODE);
		} else if (e.getSource() == btnSaveMember){
			doSaveMemberMgmt();
		} else if (e.getSource() == btnSaveBranchName){
			doSaveBranchName();
		} else if (e.getSource() == btnSavePrintTicket){
			doSavePrintTicket();
		} else if (e.getSource() == btnSaveMemberBalanceCode){
			if (tfNewMemberBalanceCode.getText() == null || tfNewMemberBalanceCode.getText().length() == 0){
				JOptionPane.showMessageDialog(this, "No input new code");
				return;
			}
			doSaveCode(tfOldMemberBalanceCode.getText(), tfNewMemberBalanceCode.getText(), ConstantValue.CONFIGS_UPDATE_MEMBERBALANCECODE);
		}
	}
	
	/**
	 * 保存密码的统一函数
	 */
	private void doSaveCode(String oldPassword, String newPassword, String key){
		String url = "common/savecode";
		HashMap<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("code", newPassword);
		params.put("oldCode", oldPassword);
		params.put("key", key);
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for save code for " + key + ". URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for save code for " + key + ". URL = " + url);
			return;
		}
		
		HttpResult<String> result = new Gson().fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
		if (!result.success){
			logger.error("return false while save code for " + key + ". URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		mainFrame.getConfigsMap().put(key, newPassword);
		JOptionPane.showMessageDialog(this, "Change password successfully!");
		this.setVisible(false);
	}
	
	private void doSavePrintTicket(){
		String url = "common/saveprintticket";
		HashMap<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		if (rbPrintTicketAfterOrder.isSelected())
			params.put(ConstantValue.CONFIGS_PRINTTICKET, ConstantValue.CONFIGS_PRINTTICKET_AFTERMAKEORDER);
		else if (rbPrintTicketAfterPaid.isSelected())
			params.put(ConstantValue.CONFIGS_PRINTTICKET, ConstantValue.CONFIGS_PRINTTICKET_AFTERPAY);
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for save print ticket. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for save print ticket. URL = " + url);
			return;
		}
		
		HttpResult<String> result = new Gson().fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
		if (!result.success){
			logger.error("return false while save print ticket. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		mainFrame.getConfigsMap().put(ConstantValue.CONFIGS_PRINTTICKET, params.get(ConstantValue.CONFIGS_PRINTTICKET));
		this.setVisible(false);
	}
	
	private void doSaveLanguageSet(){
		if (tfFirstLanguageName.getText() == null || tfFirstLanguageName.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "No input first language name");
			return;
		}
		if (tfSecondLanguageName.getText() == null || tfSecondLanguageName.getText().length() == 0){
			if (rbLanguageAmount2.isSelected()){
				JOptionPane.showMessageDialog(this, "No input second language name");
				return;
			}
		}
		if (cbPrint2ndLanguage.isSelected() && rbLanguageAmount1.isSelected()){
			JOptionPane.showMessageDialog(this, "Cannot print second language name on ticket.");
			return;
		}
		String url = "common/savelanguageset";
		HashMap<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		if (rbLanguageAmount2.isSelected()){
			params.put("amount", "2");
			params.put("secondName", tfSecondLanguageName.getText());
		} else {
			params.put("amount", "1");
		}
		params.put("firstName", tfFirstLanguageName.getText());
		params.put("print2ndLanguage", String.valueOf(cbPrint2ndLanguage.isSelected()));
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for save language. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for save language. URL = " + url);
			return;
		}
		
		HttpResult<String> result = new Gson().fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
		if (!result.success){
			logger.error("return false while save language. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		mainFrame.getConfigsMap().put(ConstantValue.CONFIGS_LANGUAGEAMOUNT, params.get("amount"));
		mainFrame.getConfigsMap().put(ConstantValue.CONFIGS_FIRSTLANGUAGENAME, tfFirstLanguageName.getText());
		mainFrame.getConfigsMap().put(ConstantValue.CONFIGS_SECONDLANGUAGENAME, tfSecondLanguageName.getText());
		mainFrame.getConfigsMap().put(ConstantValue.CONFIGS_PRINT2NDLANGUAGENAME, String.valueOf(cbPrint2ndLanguage.isSelected()));
		this.setVisible(false);
	}
	
//	private void doSaveCancelOrderCode(){
//		if (tfNewCancelOrderCode.getText() == null || tfNewCancelOrderCode.getText().length() == 0){
//			JOptionPane.showMessageDialog(this, "No input new code");
//			return;
//		}
//		String url = "common/savecancelordercode";
//		HashMap<String, String> params = new HashMap<>();
//		params.put("userId", MainFrame.getLoginUser().getId()+"");
//		params.put("code", tfNewCancelOrderCode.getText());
//		params.put("oldCode", tfOldCancelOrderCode.getText());
//		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
//		if (response == null){
//			logger.error("get null from server for save cancel order code. URL = " + url + ", param = "+ params);
//			JOptionPane.showMessageDialog(this, "get null from server for save cancel order code. URL = " + url);
//			return;
//		}
//		
//		HttpResult<String> result = new Gson().fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
//		if (!result.success){
//			logger.error("return false while save cancel order code. URL = " + url + ", response = "+response);
//			JOptionPane.showMessageDialog(this, result.result);
//			return;
//		}
//		mainFrame.getConfigsMap().put(ConstantValue.CONFIGS_CANCELORDERCODE, tfNewCancelOrderCode.getText());
//		this.setVisible(false);
//	}
//	
//	private void doSaveClearTableCode(){
//		if (tfNewClearTableCode.getText() == null || tfNewClearTableCode.getText().length() == 0){
//			JOptionPane.showMessageDialog(this, "No input new code");
//			return;
//		}
//		String url = "common/savecleartablecode";
//		HashMap<String, String> params = new HashMap<>();
//		params.put("userId", MainFrame.getLoginUser().getId()+"");
//		params.put("code", tfNewClearTableCode.getText());
//		params.put("oldCode", tfOldClearTableCode.getText());
//		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
//		if (response == null){
//			logger.error("get null from server for save clear table code. URL = " + url + ", param = "+ params);
//			JOptionPane.showMessageDialog(this, "get null from server for save clear table code. URL = " + url);
//			return;
//		}
//		
//		HttpResult<String> result = new Gson().fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
//		if (!result.success){
//			logger.error("return false while save clear table code. URL = " + url + ", response = "+response);
//			JOptionPane.showMessageDialog(this, result.result);
//			return;
//		}
//		mainFrame.getConfigsMap().put(ConstantValue.CONFIGS_CLEARTABLECODE, tfNewClearTableCode.getText());
//		this.setVisible(false);
//	}
//	
//	private void doSaveConfirmCode(){
//		if (tfNewConfirmCode.getText() == null || tfNewConfirmCode.getText().length() == 0){
//			JOptionPane.showMessageDialog(this, "No input new code");
//			return;
//		}
//		String url = "common/saveconfirmcode";
//		HashMap<String, String> params = new HashMap<>();
//		params.put("userId", MainFrame.getLoginUser().getId()+"");
//		params.put("code", tfNewConfirmCode.getText());
//		params.put("oldCode", tfOldConfirmCode.getText());
//		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
//		if (response == null){
//			logger.error("get null from server for save confirm code. URL = " + url + ", param = "+ params);
//			JOptionPane.showMessageDialog(this, "get null from server for save confirm code. URL = " + url);
//			return;
//		}
//		
//		HttpResult<String> result = new Gson().fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
//		if (!result.success){
//			logger.error("return false while save confirm code. URL = " + url + ", response = "+response);
//			JOptionPane.showMessageDialog(this, result.result);
//			return;
//		}
//		mainFrame.getConfigsMap().put(ConstantValue.CONFIGS_CONFIRMCODE, tfNewConfirmCode.getText());
//		this.setVisible(false);
//	}
//	
//	private void doSaveOpenCashdrawerCode(){
//		if (tfNewOpenCashdrawerCode.getText() == null || tfNewOpenCashdrawerCode.getText().length() == 0){
//			JOptionPane.showMessageDialog(this, "No input new code");
//			return;
//		}
//		String url = "common/saveopencashdrawercode";
//		HashMap<String, String> params = new HashMap<>();
//		params.put("userId", MainFrame.getLoginUser().getId()+"");
//		params.put("code", tfNewOpenCashdrawerCode.getText());
//		params.put("oldCode", tfOldOpenCashdrawerCode.getText());
//		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
//		if (response == null){
//			logger.error("get null from server for save open cashdrawer code. URL = " + url + ", param = "+ params);
//			JOptionPane.showMessageDialog(this, "get null from server for save open cashdrawer code. URL = " + url);
//			return;
//		}
//		
//		HttpResult<String> result = new Gson().fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
//		if (!result.success){
//			logger.error("return false while save open cashdrawer code. URL = " + url + ", response = "+response);
//			JOptionPane.showMessageDialog(this, result.result);
//			return;
//		}
//		mainFrame.getConfigsMap().put(ConstantValue.CONFIGS_OPENCASHDRAWERCODE, tfNewOpenCashdrawerCode.getText());
//		this.setVisible(false);
//	}
	
	private void doSaveBranchName(){
		if (tfBranchName.getText() == null || tfBranchName.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input branch name");
			return;
		}
		String url = "common/savebranchname";
		HashMap<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("branchName", tfBranchName.getText());
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for save branch name. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for save branch name. URL = " + url);
			return;
		}
		
		HttpResult<String> result = new Gson().fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
		if (!result.success){
			logger.error("return false while save branch name. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		mainFrame.getConfigsMap().put(ConstantValue.CONFIGS_BRANCHNAME, tfBranchName.getText());
		this.setVisible(false);
	}
	
	private void doSaveMemberMgmt(){
		if (cbMemberScore.isSelected() && (tfScorePerDollar.getText() == null || tfScorePerDollar.getText().length() == 0)){
			JOptionPane.showMessageDialog(this, "Please input the scores for member consumption.");
			return;
		}
		if (!cbMemberScore.isSelected() && !cbMemberDeposit.isSelected()){
			JOptionPane.showMessageDialog(this, "Please choose at least one option for member.");
			return;
		}
		String url = "common/savemembermanagementway";
		HashMap<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("byScore", String.valueOf(cbMemberScore.isSelected()));
		params.put("byDeposit", String.valueOf(cbMemberDeposit.isSelected()));
		params.put("scorePerDollar", tfScorePerDollar.getText());
		params.put("needPassword", String.valueOf(cbNeedPassword.isSelected()));
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for save member management way. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for save member management way. URL = " + url);
			return;
		}
		
		HttpResult<String> result = new Gson().fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
		if (!result.success){
			logger.error("return false while save member management way. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		mainFrame.loadConfigsMap();
		this.setVisible(false);
	}
}
