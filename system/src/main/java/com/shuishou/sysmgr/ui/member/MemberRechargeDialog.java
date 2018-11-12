package com.shuishou.sysmgr.ui.member;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.ConstantValue;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.Member;
import com.shuishou.sysmgr.beans.PayWay;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.NumberTextField;

public class MemberRechargeDialog extends JDialog implements ActionListener {

	private final Logger logger = Logger.getLogger(ChangePasswordDialog.class.getName());
	
	private MainFrame mainFrame;
	private MemberQueryPanel parent;
	private Member member;
	
	private JRadioButton rbCash = new JRadioButton("Cash", true);
	private JRadioButton rbBankTransfer = new JRadioButton("Bank Transfer");
	private JPasswordField tfPassword = new JPasswordField();
	private NumberTextField tfNewValue = new NumberTextField(true);
	private JButton btnSave = new JButton("Save");
	private JButton btnCancel = new JButton("Cancel");
	private ButtonGroup buttonGroup = new ButtonGroup();
	public MemberRechargeDialog(MainFrame mainFrame, MemberQueryPanel parent, Member member){
		super(mainFrame, "Member Recharge", true);
		this.mainFrame = mainFrame;
		this.parent = parent;
		this.member = member;
		initUI();
	}
	
	private void initUI(){
		JLabel lbPassword = new JLabel("Password");
		JLabel lbNewValue = new JLabel("Recharge Value");
		buttonGroup.add(rbBankTransfer);
		buttonGroup.add(rbCash);
		JPanel pPay = new JPanel();
		pPay.setBorder(BorderFactory.createTitledBorder("Pay Method"));
		pPay.add(rbCash);
		pPay.add(rbBankTransfer);
		ArrayList<PayWay> otherpw = mainFrame.loadPayWayList();
		if (otherpw != null && !otherpw.isEmpty()){
			for (int i = 0; i < otherpw.size(); i++) {
				PayWay pw = otherpw.get(i);
				JRadioButton rb = new JRadioButton(pw.getName());
				buttonGroup.add(rb);
				pPay.add(rb);
			}
		}
		Container c = this.getContentPane();
		c.setLayout(new GridBagLayout());
		c.add(lbPassword, 	new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		c.add(tfPassword, 	new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		c.add(lbNewValue, 	new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		c.add(tfNewValue, 	new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		c.add(pPay, 		new GridBagConstraints(0, 2, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		c.add(btnSave, 			new GridBagConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		c.add(btnCancel, 		new GridBagConstraints(1, 3, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		btnSave.addActionListener(this);
		btnCancel.addActionListener(this);
		
		this.setSize(500,240);
		this.setLocation((int)(mainFrame.getWidth() / 2 - this.getWidth() /2 + mainFrame.getLocation().getX()), 
				(int)(mainFrame.getHeight() / 2 - this.getHeight() / 2 + mainFrame.getLocation().getY()));
	}
	
	
	@SuppressWarnings("deprecation")
	private void doSave(){
		if (tfPassword.getText() == null || !tfPassword.getText().equals(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_UPDATE_MEMBERBALANCECODE))){
			JOptionPane.showMessageDialog(this, "The password input is wrong.");
			return;
		}
		double recharge = 0;
		try{
			recharge = Double.parseDouble(tfNewValue.getText());
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(this, "Input value is not available.");
			return;
		}
		JRadioButton rb = null;
		Enumeration<AbstractButton> rbs = buttonGroup.getElements();
		while(rbs.hasMoreElements()){
			JRadioButton rbi = (JRadioButton)rbs.nextElement();
			if (rbi.isSelected()){
				rb = rbi;
				break;
			}
		}
		String url = "member/memberrecharge";
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId() + "");
		params.put("id",String.valueOf(member.getId()));
		params.put("rechargeValue", String.valueOf(recharge));
		params.put("payway", rb.getText());
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for member recharge. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for member recharge. URL = " + url);
			return;
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMDHMS).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			logger.error("return false while member recharge. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		member.setBalanceMoney(result.data.getBalanceMoney());
		parent.getTable().updateUI();//here is low efficient, buy using model.fireDataChange will occur an exception if existing a rowSorter.
		parent.doPrintRechargeTicket("Customer Recharge", member.getMemberCard(), member.getName(), recharge, member.getBalanceMoney(), rb.getText(), 2);
		JOptionPane.showMessageDialog(this, "Member recharge successfully!");
		setVisible(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCancel){
			setVisible(false);
		} else if (e.getSource() == btnSave){
			doSave();			
		}
	}
	
	private String toSHA1(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException ex) {
			throw ex;
		}
		return toHex(md.digest(data));
	}

	/**
	 * Bytes array to string.
	 * 
	 * @param digest
	 * @return final string.
	 */
	private String toHex(byte[] digest) {
		StringBuilder sb = new StringBuilder();
		for (byte b : digest) {
			sb.append(String.format("%1$02X", b));
		}

		return sb.toString();
	}
	
}