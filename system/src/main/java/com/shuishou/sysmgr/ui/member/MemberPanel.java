package com.shuishou.sysmgr.ui.member;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.ConstantValue;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.Member;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.CommonDialogOperatorIFC;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.JDatePicker;
import com.shuishou.sysmgr.ui.components.NumberTextField;

public class MemberPanel extends JPanel implements CommonDialogOperatorIFC{

	private final Logger logger = Logger.getLogger(MemberPanel.class.getName());
	private MemberQueryPanel parent;
	private JTextField tfName= new JTextField(155);
	private JTextField tfMemberCard= new JTextField(155);
	private JTextField tfTelephone= new JTextField(155);
	private JTextField tfAddress= new JTextField(155);
	private JPasswordField tfPassword = new JPasswordField(155);
	private JPasswordField tfConfirmPwd = new JPasswordField(155);
	private NumberTextField tfPostcode= new NumberTextField(false);
	private NumberTextField tfDiscountRate= new NumberTextField(true);
	private JDatePicker dpBirthday = new JDatePicker();
	private JLabel lbPassword = new JLabel("Password");
	private JLabel lbConfirmPwd = new JLabel("Confirm Pwd");
	private Member member;
	public MemberPanel(MemberQueryPanel parent){
		this.parent = parent;
		initUI();
	}
	
	private void initUI(){
		JLabel lbName = new JLabel("Name");
		JLabel lbMemberCard = new JLabel("Member Card");
		JLabel lbTelephone = new JLabel("Telephone");
		JLabel lbAddress = new JLabel("Address");
		JLabel lbPostcode = new JLabel("Postcode");
		JLabel lbBirthday = new JLabel("Birthday");
		JLabel lbDiscountRate = new JLabel("Discount Rate");
		
		
		this.setLayout(new GridBagLayout());
		add(lbName, 		new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfName, 		new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));
		add(lbMemberCard, 	new GridBagConstraints(0, 1, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfMemberCard, 	new GridBagConstraints(1, 1, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));
		add(lbTelephone, 	new GridBagConstraints(0, 2, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfTelephone, 	new GridBagConstraints(1, 2, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));
		add(lbAddress, 		new GridBagConstraints(0, 3, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfAddress, 		new GridBagConstraints(1, 3, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));
		add(lbPostcode, 	new GridBagConstraints(0, 4, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfPostcode, 	new GridBagConstraints(1, 4, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));
		add(lbBirthday, 	new GridBagConstraints(0, 5, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(dpBirthday, 	new GridBagConstraints(1, 5, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));
		add(lbDiscountRate, new GridBagConstraints(0, 6, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfDiscountRate, new GridBagConstraints(1, 6, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));
		add(lbPassword, 	new GridBagConstraints(0, 7, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfPassword, 	new GridBagConstraints(1, 7, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));
		add(lbConfirmPwd, 	new GridBagConstraints(0, 8, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfConfirmPwd, 	new GridBagConstraints(1, 8, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));
		tfName.setMinimumSize(new Dimension(180,25));
		
		tfDiscountRate.setText("1.00");
	}
	@Override
	public boolean doSave() {
		if (!doCheckInput())
			return false;
		
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("name", tfName.getText());
		params.put("memberCard", tfMemberCard.getText());
		params.put("discountRate", tfDiscountRate.getText());
		if (tfTelephone.getText() != null && tfTelephone.getText().length() > 0){
			params.put("telephone", tfTelephone.getText());
		}
		if (tfAddress.getText() != null && tfAddress.getText().length() > 0){
			params.put("address", tfAddress.getText());
		}
		if (tfPostcode.getText() != null && tfPostcode.getText().length() > 0){
			params.put("postCode", tfPostcode.getText());
		}
		if (dpBirthday.getModel() != null && dpBirthday.getModel().getValue() != null){
			Calendar c = (Calendar)dpBirthday.getModel().getValue();
			params.put("birth", ConstantValue.DFYMDHMS.format(c.getTime()));
		}
		if (tfPassword.getPassword().length == 0){
			params.put("password", "");
		} else {
			try {
				params.put("password", toSHA1(tfPassword.getText().getBytes()));
			} catch (NoSuchAlgorithmException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
				return false;
			}
		}
		String url = "member/addmember";
		if (member != null){
			url = "member/updatemember";
			params.put("id", member.getId() + "");
		}
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for add/update member. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for add/update member. URL = " + url);
			return false;
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMDHMS).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			logger.error("return false while add/update member. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return false;
		}
		if (member == null){
			parent.insertRow(result.data);
		} else {
			parent.updateRow(result.data);
		}
		return true;
	}

	private boolean doCheckInput(){
		if (tfName.getText() == null || tfName.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Name");
			return false;
		}
		if (tfMemberCard.getText() == null || tfMemberCard.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Member card");
			return false;
		}
		if (tfDiscountRate.getText() == null || tfDiscountRate.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Discount Rate");
			return false;
		}
		//只有添加状态, 且配置项中要求使用密码时, 判断密码的输入
		if (Boolean.valueOf(parent.getMainFrame().getConfigsMap().get(ConstantValue.CONFIGS_MEMBERMGR_NEEDPASSWORD))
				&& this.member == null){
			if (tfPassword.getPassword() == null || tfPassword.getPassword().length == 0){
				JOptionPane.showMessageDialog(this, "Please input Password");
				return false;
			}
			if (tfConfirmPwd.getPassword() == null || tfConfirmPwd.getPassword().length == 0){
				JOptionPane.showMessageDialog(this, "Please input Confirm Password");
				return false;
			}
			if (!tfConfirmPwd.getText().equals(tfPassword.getText())){
				JOptionPane.showMessageDialog(this, "Input Password two times are different");
				return false;
			}
		}
		return true;
	}
	
	public void setObjectValue(Member m){
		this.member = m;
		tfName.setText(m.getName());
		tfMemberCard.setText(m.getMemberCard());
		tfDiscountRate.setText(String.valueOf(m.getDiscountRate()));
		if (m.getTelephone() != null)
			tfTelephone.setText(m.getTelephone()); 
		if (m.getAddress() != null)
			tfAddress.setText(m.getAddress()); 
		if (m.getPostCode() != null)
			tfPostcode.setText(m.getPostCode()); 
		if (m.getBirth() != null){
			Calendar c = Calendar.getInstance();
			c.setTime(m.getBirth());
			dpBirthday.getModel().setYear(c.get(Calendar.YEAR));
			dpBirthday.getModel().setMonth(c.get(Calendar.MONTH));
			dpBirthday.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpBirthday.getModel().setSelected(true);
		}
	}
	
	public void hidePassword(){
		lbPassword.setVisible(false);
		tfPassword.setVisible(false);
		lbConfirmPwd.setVisible(false);
		tfConfirmPwd.setVisible(false);
	}
	private String toSHA1(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException ex) {
			logger.error("Can't get SHA-1 algorithm message digest.");
			throw ex;
		}
		return toHex(md.digest(data));
	}

	private String toHex(byte[] digest) {
		StringBuilder sb = new StringBuilder();
		for (byte b : digest) {
			sb.append(String.format("%1$02X", b));
		}

		return sb.toString();
	}
}
